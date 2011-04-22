/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: SWTMapCanvas.java 657 2009-12-30 12:27:37Z damumbl $
 *
 *  Copyright (C) 2008 Michael Kanis and others
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
 ******************************************************************************/
package net.skweez.geoclipse.map.widgets;

import java.awt.Rectangle;

import net.skweez.geoclipse.Activator;
import net.skweez.geoclipse.map.Constants;
import net.skweez.geoclipse.map.Tile;
import net.skweez.geoclipse.map.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


/**
 * A widget for SWT to show a map of the world (or a part of it).
 * <p>
 * This class is not designed to be subclassed.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class SWTMapCanvas extends Canvas implements IMapCanvas {

	/** The map component this canvas belongs to. */
	final MapBase map;

	/** Buffer image that'll hold the visible part of the map. */
	private Image mapImage;

	/** The graphics context for the map image that we will draw on. */
	GC gc;

	/** Queues redraws when a tile has been fully loaded. */
	final TileLoadListener tileLoadListener;

	/** Used to decide wether a redraw request needs to be executed or not. */
	private int lastRedrawRequest = 0;

	/** Creates a new map widget. */
	public SWTMapCanvas(final MapBase map, final Composite parent) {

		super(parent, SWT.DOUBLE_BUFFERED);
		this.map = map;
		tileLoadListener = new TileLoadListener(this);

		/*
		 * We could implement the listener interfaces directly, but that would
		 * make the interface methods public. Instead, the SWT convention is to
		 * use anonymous inner classes to forward the functionality to
		 * non-public methods of the same name.
		 */
		addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent e) {
				SWTMapCanvas.this.paintControl(e);
			}
		});
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

		final Rectangle mapRect = map.getViewport();

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
			map.drawMapTiles();
		} catch (final Exception e) {
			// map image is corrupt
			mapImage.dispose();
		} finally {
			Util.disposeResource(gc);
		}

		redraw();
	}

	/** {@inheritDoc} */
	public void drawTile(Tile tile, Rectangle targetRectangle) {

		switch (tile.getStatus()) {
		case READY:
			Image tileImage = tile.getImage();
			gc.drawImage(tileImage, targetRectangle.x, targetRectangle.y);
			break;
		case ERROR:
			gc.drawImage(Activator.getDefault().getImageRegistry().get(
					Constants.ERROR_IMG_KEY), targetRectangle.x,
					targetRectangle.y);
			break;
		case LOADING:
			gc.drawImage(Activator.getDefault().getImageRegistry().get(
					Constants.LOADING_IMG_KEY), targetRectangle.x,
					targetRectangle.y);
			tile.addObserver(tileLoadListener);
			break;
		}
	}

	/** {@inheritDoc} */
	public void drawBackground(final Rectangle targetRectangle) {
		gc.setBackground(getBackground());
		gc.fillRectangle(targetRectangle.x, targetRectangle.y, map
				.getTileFactory().getTileSize(), map.getTileFactory()
				.getTileSize());
	}

	/** {@inheritDoc} */
	@Override
	public void dispose() {
		Util.disposeResource(mapImage);
		Util.disposeResource(gc);
		super.dispose();
	}

	/** {@inheritDoc} */
	public void queueRedraw() {

		if (isDisposed()) {
			return;
		}

		final Runnable imageRunnable = new Runnable() {
			// The access to this doesn't need to be atomic, because it's not
			// so bad, if a few render requests are handled that wouldn't have
			// to be
			final int requestNumber = ++lastRedrawRequest;

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
