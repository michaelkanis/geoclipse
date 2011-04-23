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
package net.skweez.geoclipse.map;

import java.awt.Dimension;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.skweez.geoclipse.Activator;
import net.skweez.geoclipse.map.internal.Tile;
import net.skweez.geoclipse.map.internal.TileLoadListener;
import net.skweez.geoclipse.map.internal.Util;
import net.skweez.geoclipse.map.tilefactories.ITileFactory;
import net.skweez.geoclipse.projections.MercatorProjection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * This class does all the actual map drawing.
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class MapView extends Canvas {

	public static final String ID = "net.skweez.geoclipse.mapview";

	/** The zoom level. Normally a value between around 0 and 20. */
	private int zoomLevel = 1;

	/** The viewport, i.e. the part of the map that is shown. */
	private Rectangle viewport = new Rectangle(0, 0, 0, 0);

	private final Point offset;

	/** The position of the map, this is the center of the shown part. */
	private GeoPoint mapCenter = new GeoPoint(0.0, 0.0);

	/** Factory used to grab the tiles necessary for painting the map. */
	private ITileFactory tileFactory;

	private final Projection projection;

	/** Buffer image that'll hold the visible part of the map. */
	private Image mapImage;

	/** The graphics context for the map image that we will draw on. */
	private GC gc;

	/** Queues redraws when a tile has been fully loaded. */
	private final TileLoadListener tileLoadListener;

	/** Used to decide wether a redraw request needs to be executed or not. */
	private int lastRedrawRequest = 0;

	private final List<Overlay> overlays;

	/** Default constructor. */
	public MapView(final Composite parent) {
		super(parent, SWT.DOUBLE_BUFFERED);

		tileLoadListener = new TileLoadListener(this);

		// FIXME net.skweez.map.Projection must be an extension point
		// and TileFactory extensions must reference one
		projection = new MercatorProjection(this);

		offset = new Point(0, 0);

		overlays = Activator.getDefault().getOverlays();

		/*
		 * We could implement the listener interfaces directly, but that would
		 * make the interface methods public. Instead, the SWT convention is to
		 * use anonymous inner classes to forward the functionality to
		 * non-public methods of the same name.
		 */
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(final PaintEvent e) {
				MapView.this.paintControl(e);
			}
		});

		setupListeners();
	}

	/** Setup the listeners. */
	private void setupListeners() {
		MapController controller = new MapController(this);
		addMouseListener(controller);
		addMouseMoveListener(controller);
		addMouseWheelListener(controller);
		addKeyListener(controller);

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				updateViewport();
				queueRedraw();
			}
		});
	}

	/** Returns the center of the current map viewport. */
	public GeoPoint getMapCenter() {
		return mapCenter;
	}

	public Projection getProjection() {
		return projection;
	}

	/** Returns the current factory used to draw the map tiles. */
	public ITileFactory getTileFactory() {
		return tileFactory;
	}

	/**
	 * Set the currently displayed viewport. This gets normalized to be
	 * centered, if it is bigger than the map and is inside the map if it is
	 * smaller.
	 * 
	 * @deprecated
	 */
	@Deprecated
	public void setViewport(Rectangle viewport) {
		final Dimension mapSize = tileFactory
				.getMapSizeInPixels(getZoomLevel());

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

		setOffset(viewport.x, viewport.y);

		queueRedraw();
		updatePosition();
	}

	/** Sets offset. */
	public void setOffset(int x, int y) {
		offset.x = x;
		offset.y = y;

		queueRedraw();
		updatePosition();
	}

	/**
	 * Returns the viewport. <code>x</code> and <code>y</code> contains the
	 * position in world pixel, <code>width</code> and <code>height</code>
	 * contains the visible area in device pixel
	 * 
	 * @deprecated Use {@link #getBounds()} for width and height. Use getOffset
	 *             for x and y.
	 */
	@Deprecated
	public Rectangle getViewport() {
		return viewport;
	}

	/** Returns offset. */
	public Point getOffset() {
		return offset;
	}

	/**
	 * Returns the bounds of the viewport in pixels. This can be used to
	 * transform points into the world bitmap coordinate space. The viewport is
	 * the part of the map, that you can currently see on the screen.
	 */
	private Rectangle calculateViewport(final Point center) {
		// calculate the visible viewport area in pixels
		final int width = getBounds().width;
		final int height = getBounds().height;

		final int x = center.x - width / 2;
		final int y = center.y - height / 2;

		return new Rectangle(x, y, width, height);
	}

	public Dimension getMapSizeInPixels() {
		return getTileFactory().getMapSizeInPixels(getZoomLevel());
	}

	/** Returns the current zoom level. */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/** Sets the new center of the map in pixel coordinates. */
	/* package */void setMapCenter(Point center) {
		setViewport(calculateViewport(center));
		updatePosition();
		queueRedraw();
	}

	/** Recenter the map to the given location. */
	/* package */void setPosition(final GeoPoint position) {
		this.mapCenter = position;
	}

	/** Set the tile factory for the map. Causes a redraw of the map. */
	/* package */void setTileFactory(final ITileFactory factory) {
		if (factory == null) {
			return;
		}

		if (tileFactory != null) {
			tileFactory = factory;
			setZoom(getZoomLevel());
			setPosition(getMapCenter());
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
	/* package */void setZoom(int zoom) {
		// Restrict zoom to the min and max values of the factory
		if (zoom < tileFactory.getMinimumZoom()
				|| zoom > tileFactory.getMaximumZoom()) {
			return;
		}

		this.zoomLevel = zoom;
		queueRedraw();
	}

	/**
	 * Increase the zoom level by one. This is exactly the same as calling
	 * {@link #setZoom(int)} with <code>{@link #getZoomLevel()} + 1</code> as
	 * argument.
	 */
	/* package */void zoomIn() {
		final GeoPoint center = getMapCenter();
		setZoom(getZoomLevel() + 1);
		setPosition(center);
		updateViewport();
	}

	/**
	 * Decrease the zoom level by one. This is exactly the same as calling
	 * {@link #setZoom(int)} with <code>{@link #getZoomLevel()} - 1</code> as
	 * argument.
	 */
	/* package */void zoomOut() {
		final GeoPoint center = getMapCenter();
		setZoom(getZoomLevel() - 1);
		setPosition(center);
		updateViewport();
	}

	/** Update the viewport based on a set position. */
	private void updateViewport() {
		Dimension dim = tileFactory.getMapSizeInPixels(getZoomLevel());
		setMapCenter(pointToPoint(getProjection().geoToPixel(getMapCenter())));
	}

	private Point pointToPoint(java.awt.Point point) {
		return new Point(point.x, point.y);
	}

	/** Update the position based on the currently displayed viewport. */
	private void updatePosition() {

		Rectangle bounds = getBounds();
		int centerX = offset.x + bounds.width / 2;
		int centerY = offset.y + bounds.height / 2;

		setPosition(getProjection().pixelToGeo(centerX, centerY));
	}

	/**
	 * Access the overlay list. Any Overlays in this list will be drawn (in
	 * increasing order) and will receive events (in decreasing order, until one
	 * returns true). If you modify the list, you will probably want to call
	 * View.postInvalidate() so that the change will be made visible to the
	 * user.
	 * 
	 * @return The list of overlays. This has been run through
	 *         Collections.synchronizedList(java.util.List); thus feel free to
	 *         query it or modify it as you see fit and the changes will be
	 *         reflected on the next draw or event. However, if you iterate over
	 *         it, the entire loop should be enclosed in a block synchronizing
	 *         on the list.
	 */
	public List<Overlay> getOverlays() {
		return Collections.synchronizedList(overlays);
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

				draw();
			}
		};

		getDisplay().asyncExec(imageRunnable);
	}

	/**
	 * This method is called every time, the viewport content has changed. Draws
	 * everything to an offscreen image, which is then rendered to the screen by
	 * {@link #paintControl(PaintEvent)}. This method <b>must</b> be called from
	 * the UI thread!
	 */
	private void draw() {
		if (isDisposed()) {
			return;
		}

		try {
			mapImage = checkOrCreateImage(mapImage);
			gc = new GC(mapImage);
			drawBackground();
			drawGroundLayer();
			drawOverlays();
		} catch (Exception e) {
			// map image is corrupt
			Util.disposeResource(mapImage);
		} finally {
			Util.disposeResource(gc);
		}

		redraw();
	}

	/** Fill the background. */
	private void drawBackground() {
		gc.setBackground(getBackground());
		drawBackground(gc, 0, 0, getBounds().width, getBounds().height);
	}

	/** Draws all visible tiles of the map. */
	private void drawGroundLayer() {

		org.eclipse.swt.graphics.Rectangle bounds = getBounds();

		final int tileSize = getTileFactory().getTileSize();

		// get the visible tiles in the viewport area
		final int tilesWide = calculateTileNumber(bounds.width);
		final int tilesHigh = Math.min(calculateTileNumber(bounds.height),
				getTileFactory().getMapSize(getZoomLevel()).height);

		System.out.println("tilesWide:" + tilesWide);
		System.out.println("tilesHigh:" + tilesHigh);

		// the offset of the visible screen to the origin of the map in tiles
		final Point tileOffset = new Point(calculateTileOffset(getOffset().x),
				calculateTileOffset(getOffset().y));

		System.out.println("tileOffset:" + tileOffset);

		Transform transform = new Transform(getDisplay());
		transform.translate(-getOffset().x, -getOffset().y);
		System.out.println("transform:" + transform);
		// gc.setTransform(transform);

		// draw all visible tiles
		for (int x = 0; x <= tilesWide; x++) {
			for (int y = 0; y <= tilesHigh; y++) {

				Point position = new Point(tileOffset.x + x, tileOffset.y + y);
				System.out.println("position:" + position);

				Point pixelPosition = new Point(position.x * tileSize,
						position.y * tileSize);
				System.out.println("pixelPosition:" + pixelPosition);

				// draw tile according to its state
				transform.translate(pixelPosition.x, pixelPosition.y);
				System.out.println("transform:" + transform);
				gc.setTransform(transform);

				gc.setClipping(0, 0, tileSize, tileSize);

				drawTile(getTile(position));

				transform.translate(-pixelPosition.x, -pixelPosition.y);
				gc.setTransform(transform);
			}
		}

		Util.disposeResource(transform);
		gc.setTransform(null);
	}

	/** Retrieve a tile from the given position. */
	private Tile getTile(Point tilePosition) {
		Tile tile = tileFactory.getTile(tilePosition.x, tilePosition.y,
				zoomLevel);
		Assert.isTrue(tile != null, "Tile may never be null.");
		return tile;
	}

	/** Draw one specific tile at its position. */
	private void drawTile(Tile tile) {
		if (tile.getStatus() == Tile.Status.LOADING) {
			tile.addObserver(tileLoadListener);
		}

		tile.draw(gc);
	}

	/** Draws overlays as described in {@link #getOverlays()}. */
	private void drawOverlays() {
		gc.setClipping((org.eclipse.swt.graphics.Rectangle) null);

		Transform transform = new Transform(getDisplay());
		transform.translate(-getOffset().x, -getOffset().y);
		gc.setTransform(transform);

		synchronized (overlays) {
			Iterator<Overlay> iterator = overlays.iterator();
			while (iterator.hasNext()) {
				iterator.next().draw(gc, this);
			}
		}

		Util.disposeResource(transform);
		gc.setTransform(null);
	}

	/**
	 * This method gets called whenever SWT wants to redraw the widget. It gets
	 * called by the private {@link PaintListener} in this class. For
	 * performance reasons {@link #draw()} is called directly from the methods
	 * that change the view of the map (e.g. zoom, pan, etc.). This method only
	 * paints the offscreen buffer image produced by {@link #draw()} to the
	 * screen.
	 */
	private void paintControl(final PaintEvent event) {
		if (mapImage == null || mapImage.isDisposed()) {
			return;
		}
		event.gc.drawImage(mapImage, 0, 0);
	}

	/** Check the image or create it when it has the wrong size. */
	private Image checkOrCreateImage(Image image) {

		final org.eclipse.swt.graphics.Rectangle mapRect = getBounds();

		// create map image
		if (!(Util.canReuseImage(image, mapRect))) {
			image = Util.createImage(getDisplay(), image, mapRect);
		}

		return image;
	}

	/** Get rid of SWT resoures. */
	@Override
	public void dispose() {
		Util.disposeResource(mapImage);
		Util.disposeResource(gc);
		super.dispose();
	}

}
