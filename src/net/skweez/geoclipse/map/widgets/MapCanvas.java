/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: MapBase.java 657 2009-12-30 12:27:37Z damumbl $
 *
 * Copyright (C) 2009 mks and others
 *  
 * This file is part of Geoclipse.
 *
 * Geoclipse is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License, or
 * (at your option) any later version.
 *
 * Geoclipse is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Geoclipse.  If not, see <http://www.gnu.org/licenses/>. 
 ******************************************************************************/
package net.skweez.geoclipse.map.widgets;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import net.skweez.geoclipse.Activator;
import net.skweez.geoclipse.gpx.model.GeoPoint;
import net.skweez.geoclipse.map.ETileStatus;
import net.skweez.geoclipse.map.Tile;
import net.skweez.geoclipse.map.Util;
import net.skweez.geoclipse.map.tilefactories.ITileFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import edu.tum.cs.commons.assertion.CCSMAssert;

/**
 * This class does all the actual map drawing.
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class MapCanvas extends Canvas {

	/** The zoom level. Normally a value between around 0 and 20. */
	protected int zoom = 1;

	/** The viewport, i.e. the part of the map that is shown. */
	protected Rectangle viewport = new Rectangle();

	/** The position of the map, this is the center of the shown part. */
	protected GeoPoint position = new GeoPoint(0.0, 0.0);

	/** Factory used to grab the tiles necessary for painting the map. */
	protected ITileFactory tileFactory;

	/** Buffer image that'll hold the visible part of the map. */
	private Image mapImage;

	/** The graphics context for the map image that we will draw on. */
	private GC gc;

	/** Queues redraws when a tile has been fully loaded. */
	final TileLoadListener tileLoadListener;

	/** Used to decide wether a redraw request needs to be executed or not. */
	private int lastRedrawRequest = 0;

	/** Default constructor. */
	public MapCanvas(final Composite parent) {
		super(parent, SWT.DOUBLE_BUFFERED);
		tileLoadListener = new TileLoadListener(this);

		/*
		 * We could implement the listener interfaces directly, but that would
		 * make the interface methods public. Instead, the SWT convention is to
		 * use anonymous inner classes to forward the functionality to
		 * non-public methods of the same name.
		 */
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				MapCanvas.this.paintControl(e);
			}
		});

		setupListeners();
	}

	/** Setup the listeners. */
	protected void setupListeners() {
		MapController controller = new MapController(this);
		addMouseListener(controller);
		addMouseMoveListener(controller);
		addMouseWheelListener(controller);
		addKeyListener(controller);

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				viewport.width = getClientArea().width;
				viewport.height = getClientArea().height;
				updateViewport();
				queueRedraw();
			}
		});
	}

	/** Retrieve a tile from the given position. */
	private Tile getTile(Point tilePosition) {
		Tile tile = tileFactory.getTile(tilePosition.x, tilePosition.y, zoom);
		CCSMAssert.isFalse(tile == null, "Tile may nerver be null.");
		return tile;
	}

	/** Returns the center of the current map viewport. */
	public GeoPoint getPosition() {
		return position;
	}

	/** Returns the current factory used to draw the map tiles. */
	public ITileFactory getTileFactory() {
		return tileFactory;
	}

	/**
	 * Set the currently displayed viewport. This gets normalized to be
	 * centered, if it is bigger than the map and is inside the map if it is
	 * smaller.
	 */
	public void setViewport(Rectangle viewport) {
		final Dimension mapSize = tileFactory.getMapSizeInPixels(getZoom());

		// normalize x
		viewport.x %= mapSize.width;
		if (viewport.x < 0) {
			viewport.x += mapSize.width;
		}

		// normalize y
		if (viewport.height > mapSize.height) {
			viewport.y = (mapSize.height - viewport.height) / 2;
		} else {
			viewport.y = Math.min(viewport.y, mapSize.height - viewport.height);
			viewport.y = Math.max(viewport.y, 0);
		}

		this.viewport = viewport;
		queueRedraw();
	}

	/**
	 * Returns the viewport. <code>x</code> and <code>y</code> contains the
	 * position in world pixel, <code>width</code> and <code>height</code>
	 * contains the visible area in device pixel
	 */
	public Rectangle getViewport() {
		return new Rectangle(viewport);
	}

	/**
	 * Returns the bounds of the viewport in pixels. This can be used to
	 * transform points into the world bitmap coordinate space. The viewport is
	 * the part of the map, that you can currently see on the screen.
	 */
	public Rectangle calculateViewport(final Point center) {
		// calculate the visible viewport area in pixels
		final int width = viewport.width;
		final int height = viewport.height;

		final int x = center.x - width / 2;
		final int y = center.y - height / 2;

		return new Rectangle(x, y, width, height);
	}

	/** Returns the current zoom level. */
	public int getZoom() {
		return zoom;
	}

	/** Sets the new center of the map in pixel coordinates. */
	public void setCenter(int x, int y) {
		setCenter(new Point(x, y));
	}

	/** Sets the new center of the map in pixel coordinates. */
	private void setCenter(Point center) {
		setViewport(calculateViewport(center));
		updatePosition();
		queueRedraw();
	}

	/** Recenter the map to the given location. Causes a redraw of the map. */
	public void setPosition(final GeoPoint position) {
		this.position = position;
	}

	/** Set the tile factory for the map. Causes a redraw of the map. */
	public void setTileFactory(final ITileFactory factory) {
		if (factory == null) {
			return;
		}

		if (tileFactory != null) {
			tileFactory = factory;
			setZoom(getZoom());
			setPosition(getPosition());
		} else {
			tileFactory = factory;
			setZoom(factory.getMinimumZoom());
		}

		Activator.getDefault().makeDefaultTileImages(factory.getTileSize());

		updateViewport();
		queueRedraw();
	}

	/**
	 * Set a new zoom level keeping the current center position. Causes a redraw
	 * of the map.
	 */
	public void setZoom(int zoom) {
		// Restrict zoom to the min and max values of the factory
		if (zoom < tileFactory.getMinimumZoom()
				|| zoom > tileFactory.getMaximumZoom()) {
			return;
		}

		this.zoom = zoom;
		queueRedraw();
	}

	/**
	 * Increase the zoom level by one. This is exactly the same as calling
	 * {@link #setZoom(int)} with <code>{@link #getZoom()} + 1</code> as
	 * argument.
	 */
	public void zoomIn() {
		final GeoPoint center = getPosition();
		setZoom(getZoom() + 1);
		setPosition(center);
		updateViewport();
	}

	/**
	 * Decrease the zoom level by one. This is exactly the same as calling
	 * {@link #setZoom(int)} with <code>{@link #getZoom()} - 1</code> as
	 * argument.
	 */
	public void zoomOut() {
		final GeoPoint center = getPosition();
		setZoom(getZoom() - 1);
		setPosition(center);
		updateViewport();
	}

	/** Update the viewport based on a set position. */
	public void updateViewport() {
		Dimension dim = tileFactory.getMapSizeInPixels(getZoom());
		setCenter(tileFactory.getProjection().geoToPixel(getPosition(),
				dim.width, dim.height));
	}

	/** Update the position based on the currently displayed viewport. */
	public void updatePosition() {
		setPosition(tileFactory.getProjection().pixelToGeo(
				(int) getViewport().getCenterX(),
				(int) getViewport().getCenterY(),
				tileFactory.getMapSize(getZoom()).width
						* tileFactory.getTileSize(),
				tileFactory.getMapSize(getZoom()).height
						* tileFactory.getTileSize()));
	}

	/**
	 * Calculates the offset of a tile's x- or y-component to the origin of the
	 * map.
	 * 
	 * @param pixel
	 *            is the value of the pixel coordinate of the tile.
	 */
	private int calculateTileOffset(int pixel) {
		return (int) Math.floor((double) pixel
				/ (double) tileFactory.getTileSize());
	}

	/** Calculates the number of tiles we need for a given number of pixels. */
	private int calculateTileNumber(int pixelNumber) {
		return (int) Math.ceil((double) pixelNumber
				/ (double) tileFactory.getTileSize());
	}

	/** Draws all visible tiles of the map. */
	protected void drawMapTiles() {

		final Rectangle viewport = getViewport();

		final Rectangle targetViewport = new Rectangle(viewport);
		targetViewport.setLocation(0, 0);

		final int tileSize = getTileFactory().getTileSize();
		final Dimension tileMapSize = getTileFactory().getMapSize(getZoom());

		// get the visible tiles in the viewport area
		final int tilesWide = calculateTileNumber(viewport.width);
		final int tilesHigh = calculateTileNumber(viewport.height);

		// the offset of the visible screen to the origin of the map
		final Point offset = new Point(calculateTileOffset(viewport.x),
				calculateTileOffset(viewport.y));

		// draw all visible tiles
		for (int x = 0; x <= tilesWide; x++) {
			for (int y = 0; y <= tilesHigh; y++) {

				final Point position = new Point(offset.x + x, offset.y + y);

				// get onscreen rectangle for this tile
				final Rectangle targetRectangle = new Rectangle(position.x
						* tileSize - viewport.x, position.y * tileSize
						- viewport.y, tileSize, tileSize);

				// only draw something that is within the painting area
				if (targetRectangle.intersects(targetViewport)) {

					if (Util.isTileOnMap(position, tileMapSize)) {
						// draw tile according to its state
						drawTile(getTile(position), targetRectangle);
					} else {
						// fill space above and under the map
						drawBackground(targetRectangle);
					}
				}
			}
		}
	}

	/**
	 * This method gets called whenever SWT wants to redraw the widget. It gets
	 * called by the private {@link PaintListener} in this class. For
	 * performance reasons {@link #renderMapImage()} is called directly from the
	 * methods that change the view of the map (e.g. zoom, pan, etc.). This
	 * method only paints the offscreen buffer image produced by
	 * {@link #renderMapImage()} to the screen.
	 */
	private void paintControl(final PaintEvent event) {
		if (mapImage == null || mapImage.isDisposed()) {
			return;
		}
		event.gc.drawImage(mapImage, 0, 0);
	}

	/** Check the image or create it when it has the wrong size. */
	private Image checkOrCreateImage(Image image) {

		final Rectangle mapRect = getViewport();

		// create map image
		if (!(Util.canReuseImage(image, mapRect))) {
			image = Util.createImage(getDisplay(), image, mapRect);
		}

		return image;
	}

	/**
	 * This method is called every time, the viewport content has changed. Draws
	 * everything to an offscreen image, which is then rendered to the screen by
	 * {@link #paintControl(PaintEvent)}. This method <b>must</b> be called from
	 * the UI thread!
	 */
	private void renderMapImage() {
		if (isDisposed()) {
			return;
		}

		try {
			mapImage = checkOrCreateImage(mapImage);
			gc = new GC(mapImage);
			drawMapTiles();
		} catch (final Exception e) {
			// map image is corrupt
			mapImage.dispose();
		} finally {
			Util.disposeResource(gc);
		}

		redraw();
	}

	public void drawTile(Tile tile, Rectangle where) {
		Transform translation = new Transform(getDisplay());
		translation.translate(where.x, where.y);
		gc.setTransform(translation);

		if (tile.getStatus() == ETileStatus.LOADING) {
			tile.addObserver(tileLoadListener);
		}

		tile.draw(gc);

		translation.dispose();
	}

	public void drawBackground(final Rectangle targetRectangle) {
		gc.setBackground(getBackground());
		gc.fillRectangle(targetRectangle.x, targetRectangle.y, getTileFactory()
				.getTileSize(), getTileFactory().getTileSize());
	}

	/** Get rid of SWT resoures. */
	@Override
	public void dispose() {
		Util.disposeResource(mapImage);
		Util.disposeResource(gc);
		super.dispose();
	}

	/**
	 * Put a map redraw into the GUI thread queue. Only the last entry in the
	 * queue will be executed.
	 */
	public void queueRedraw() {

		if (isDisposed()) {
			return;
		}

		final Runnable imageRunnable = new Runnable() {
			// The access to this doesn't need to be atomic, because it's not
			// so bad, if a few render requests are handled that wouldn't have
			// to be
			final int requestNumber = ++lastRedrawRequest;

			@Override
			public void run() {
				// check if a newer runnable is available
				if (isDisposed() || requestNumber != lastRedrawRequest) {
					return;
				}

				renderMapImage();
			}
		};

		getDisplay().asyncExec(imageRunnable);
	}

}