/*
 *  Copyright (C) 2008-2011 Michael Kanis and others
 *  
 *  This file is part of Geoclipse.
 *
 *  Geoclipse is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Geoclipse is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Geoclipse.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.skweez.geoclipse.map;

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
 * @author Michael Kanis
 */
public class MapView extends Canvas {

	/** The extension id. */
	public static final String ID = "net.skweez.geoclipse.mapview";

	/** The zoom level. Normally a value between around 0 and 20. */
	private int zoomLevel = 1;

	/** The offset of the visible part from the top left corner of the map. */
	private final Point mapCenter;

	/** Factory used to grab the tiles necessary for painting the map. */
	private ITileFactory tileFactory;

	private final Projection projection;

	/** Buffer image that'll hold the visible part of the map. */
	private Image mapImage;

	/** Queues redraws when a tile has been fully loaded. */
	private final TileLoadListener tileLoadListener;

	/** Used to decide wether a redraw request needs to be executed or not. */
	private int lastRedrawRequest = 0;

	private final List<Overlay> overlays;

	private final MapController controller;

	/** Default constructor. */
	public MapView(final Composite parent, MapController controller) {
		super(parent, SWT.DOUBLE_BUFFERED);

		tileLoadListener = new TileLoadListener(this);

		// FIXME net.skweez.map.Projection must be an extension point
		// and TileFactory extensions must reference one
		projection = new MercatorProjection(this);

		mapCenter = new Point(0, 0);

		overlays = Activator.getDefault().getOverlays();

		this.controller = controller;
		registerController(controller);
	}

	/** Setup the listeners. */
	private void registerController(MapController controller) {
		controller.setMapView(this);

		addPaintListener(controller);

		addMouseListener(controller);
		addMouseMoveListener(controller);
		addMouseWheelListener(controller);
		addKeyListener(controller);

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				queueRedraw();
			}
		});
	}

	/** Returns controller. */
	public MapController getController() {
		return controller;
	}

	/**
	 * Get a projection for converting between screen-pixel coordinates and
	 * latitude/longitude coordinates.
	 * 
	 * @return The Projection of the map in its current state. You should not
	 *         hold on to this object for more than one draw, since the
	 *         projection of the map could change.
	 */
	public Projection getProjection() {
		return projection;
	}

	/** Returns the current factory used to draw the map tiles. */
	public ITileFactory getTileFactory() {
		return tileFactory;
	}

	/** Returns offset. */
	/* package */Point getOffset() {
		Rectangle bounds = getBounds();
		int x = mapCenter.x - bounds.width / 2;
		int y = mapCenter.y - bounds.height / 2;
		return new Point(x, y);
	}

	public int getMapSizeInPixels() {
		return getTileFactory().getMapSizeInPixels(getZoomLevel());
	}

	/** Returns the maximum zoom level for the currently drawn map. */
	public int getMaxZoomLevel() {
		return getTileFactory().getMaxZoomLevel();
	}

	/** Returns the center of the visible part of the map as {@link GeoPoint}. */
	public GeoPoint getMapCenter() {
		Point center = getCenter();
		return getProjection().pixelToGeo(center.x, center.y);
	}

	/** Returns the center of the visible map in absolute pixel coordinates. */
	/* package */Point getCenter() {
		Rectangle bounds = getBounds();
		int x = getOffset().x + bounds.width / 2;
		int y = getOffset().y + bounds.height / 2;

		return new Point(x, y);
	}

	/** Recenter the map to the given location. */
	public void setMapCenter(final GeoPoint position) {
		setMapCenter(getProjection().geoToPixel(position));
	}

	/** Sets the new center of the map in pixel coordinates. */
	/* package */void setMapCenter(Point center) {
		setMapCenter(center.x, center.y);
	}

	/** Sets the new center of the map in pixel coordinates. */
	/* package */void setMapCenter(int x, int y) {
		mapCenter.x = x;
		mapCenter.y = y;
		queueRedraw();
	}

	/** Set the tile factory for the map. Causes a redraw of the map. */
	public void setTileFactory(final ITileFactory factory) {
		if (factory == null) {
			return;
		}

		if (tileFactory != null) {
			GeoPoint center = getMapCenter();
			tileFactory = factory;
			setZoomLevel(getZoomLevel());
			setMapCenter(center);
		} else {
			tileFactory = factory;
			setZoomLevel(factory.getMinimumZoom());
		}

		Activator.getDefault().makeDefaultTileImages(factory.getTileSize());

		queueRedraw();
	}

	/** Returns the current zoom level. */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * Set a new zoom level keeping the current center position. Causes a redraw
	 * of the map.
	 */
	/* package */boolean setZoomLevel(int zoom) {
		// Restrict zoom to the min and max values of the factory
		if (zoom < tileFactory.getMinimumZoom() || zoom > getMaxZoomLevel()) {
			return false;
		}

		this.zoomLevel = zoom;
		queueRedraw();

		return true;
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

	public double getLatitudeSpan() {
		Point offset = getOffset();
		Rectangle bounds = getBounds();

		GeoPoint top = getProjection().pixelToGeo(offset.x, offset.y);
		GeoPoint bottom = getProjection().pixelToGeo(offset.x + bounds.width,
				offset.y + bounds.height);

		return Math.abs(bottom.getLatitude() - top.getLatitude());
	}

	public double getLongitudeSpan() {
		Point offset = getOffset();
		Rectangle bounds = getBounds();

		GeoPoint left = getProjection().pixelToGeo(offset.x, offset.y);
		GeoPoint right = getProjection().pixelToGeo(offset.x + bounds.width,
				offset.y + bounds.height);

		return Math.abs(right.getLongitude() - left.getLongitude());
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

		GC gc = null;
		Transform transform = null;
		try {
			mapImage = checkOrCreateImage(mapImage);

			gc = new GC(mapImage);

			drawBackground(gc);

			transform = new Transform(getDisplay());
			transform.translate(-getOffset().x, -getOffset().y);
			gc.setTransform(transform);

			drawGroundLayer(gc);
			drawOverlays(gc);

			gc.setTransform(null);
			Util.disposeResource(transform);

		} catch (Exception e) {
			// map image is corrupt
			Util.disposeResource(mapImage);
		} finally {
			Util.disposeResource(transform);
			Util.disposeResource(gc);
		}

		redraw();
	}

	/** Fill the background. */
	private void drawBackground(GC gc) {
		gc.setBackground(getBackground());
		drawBackground(gc, 0, 0, getBounds().width, getBounds().height);
	}

	/** Draws all visible tiles of the map. */
	private void drawGroundLayer(GC gc) {

		Transform transform = new Transform(gc.getDevice());
		gc.getTransform(transform);

		Rectangle bounds = gc.getClipping();
		int tileSize = getTileFactory().getTileSize();

		int x0 = calculateTileOffset(bounds.x, false);
		int x1 = calculateTileOffset(bounds.x + bounds.width, false);
		int y0 = calculateTileOffset(bounds.y, true);
		int y1 = calculateTileOffset(bounds.y + bounds.height, true);

		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				transform.translate(x * tileSize, y * tileSize);
				gc.setTransform(transform);
				drawTile(gc, getTile(x, y));
				transform.translate(-x * tileSize, -y * tileSize);
				gc.setTransform(transform);
			}
		}

		Util.disposeResource(transform);
	}

	/**
	 * Calculates the offset of a tile's x- or y-component to the origin of the
	 * map.
	 * 
	 * @param pixel
	 *            the value of the pixel coordinate of the tile.
	 */
	private int calculateTileOffset(int pixel, boolean limitToMapSize) {
		if (limitToMapSize) {
			pixel = Math.min(getMapSizeInPixels(), pixel);
			pixel = Math.max(0, pixel - 1);
		}
		return (int) Math.floor((double) pixel / tileFactory.getTileSize());
	}

	/** Retrieve a tile from the given position. */
	private Tile getTile(int x, int y) {
		Tile tile = tileFactory.getTile(x, y, zoomLevel);
		Assert.isTrue(tile != null, "Tile may never be null.");
		return tile;
	}

	/** Draw one specific tile at its position. */
	private void drawTile(GC gc, Tile tile) {
		if (tile.getStatus() == Tile.Status.LOADING) {
			tile.addObserver(tileLoadListener);
		}

		tile.draw(gc);
	}

	/** Draws overlays as described in {@link #getOverlays()}. */
	private void drawOverlays(GC gc) {
		synchronized (overlays) {
			Iterator<Overlay> iterator = overlays.iterator();
			while (iterator.hasNext()) {
				iterator.next().draw(gc, this);
			}
		}
	}

	/**
	 * This method gets called whenever SWT wants to redraw the widget. It gets
	 * called by the private {@link PaintListener} in this class. For
	 * performance reasons {@link #draw()} is called directly from the methods
	 * that change the view of the map (e.g. zoom, pan, etc.). This method only
	 * paints the offscreen buffer image produced by {@link #draw()} to the
	 * screen.
	 */
	/* package */void paintControl(final PaintEvent event) {
		if (mapImage == null || mapImage.isDisposed()) {
			return;
		}
		event.gc.drawImage(mapImage, 0, 0);
	}

	/** Check the image or create it when it has the wrong size. */
	private Image checkOrCreateImage(Image image) {

		final Rectangle mapRect = getBounds();

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
		super.dispose();
	}

}
