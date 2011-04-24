/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: MapMouseListener.java 657 2009-12-30 12:27:37Z damumbl $
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

import net.skweez.geoclipse.Constants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;

/**
 * This class implements all the listener interfaces for mouse interactions with
 * the map. This includes panning by dragging, double click to zoom, mouse wheel
 * to zoom and middle click to center.
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class MapController implements MouseListener, MouseMoveListener,
		MouseWheelListener, KeyListener, PaintListener {

	/** The amount by which the map is moved, when pressing the arrow keys. */
	private static final int OFFSET = 10;

	/** The Map this listener is handling. */
	private MapView map;

	/** Holds the mouse position for the previous event. */
	private Point oldPosition;

	/** Holds the state of the left mouse button. */
	private boolean isLeftMouseButtonPressed = false;

	public void setMapView(MapView mapView) {
		this.map = mapView;
	}

	/** Zoom in or out when the user scrolls on the canvas. */
	@Override
	public void mouseScrolled(final MouseEvent event) {
		if (event.count < 0) {
			zoomOut();
		} else {
			zoomIn();
		}
	}

	/** Zooms in to the position clicked on. */
	@Override
	public void mouseDoubleClick(final MouseEvent event) {
		if (event.button == 1) {
			Point offset = map.getOffset();
			zoomInFixing(offset.x + event.x, offset.y + event.y);
		}
	}

	/**
	 * Zoom in by one zoom level.
	 * 
	 * @param xPixel
	 *            offset, in pixels from the left of the map, where the fixed
	 *            point of our zoom will be.
	 * @param yPixel
	 *            offset, in pixels from the top of the map, where the fixed
	 *            point of our zoom will be.
	 */
	public void zoomInFixing(int xPixel, int yPixel) {
		animateTo(map.getProjection().pixelToGeo(xPixel, yPixel));
		zoomIn();
	}

	/** Increase the zoom level by one. */
	public void zoomIn() {
		final GeoPoint center = map.getMapCenter();
		setZoom(map.getZoomLevel() + 1);
		map.setMapCenter(center);
	}

	/** Decrease the zoom level by one. */
	public void zoomOut() {
		final GeoPoint center = map.getMapCenter();
		setZoom(map.getZoomLevel() - 1);
		map.setMapCenter(center);
	}

	/** Sets the zoomlevel of the map. */
	public void setZoom(int zoomLevel) {
		map.setZoom(zoomLevel);
	}

	/**
	 * Only saves state and position of the cursor. This is needed for panning.
	 */
	@Override
	public void mouseDown(final MouseEvent e) {
		if (e.button == 1) {
			oldPosition = new Point(e.x, e.y);
			isLeftMouseButtonPressed = true;
		}
	}

	/**
	 * If the mouse is moved with the primary mouse button pressed, the map is
	 * moved by the amount the user moves the mouse.
	 */
	@Override
	public void mouseMove(final MouseEvent e) {
		if ((isLeftMouseButtonPressed) == false) {
			return;
		}

		map.setCursor(Constants.CURSOR_PAN);

		scrollBy(oldPosition.x - e.x, oldPosition.y - e.y);

		oldPosition = new Point(e.x, e.y);
	}

	/**
	 * Centers the map to the position that was clicked on for the second button
	 * (normally the middle one). Resets state for primary mouse button.
	 */
	@Override
	public void mouseUp(final MouseEvent e) {
		if (e.button == 1) {
			isLeftMouseButtonPressed = false;
			map.setCursor(Constants.CURSOR_DEFAULT);
		}
	}

	/** Handle key presses. */
	@Override
	public void keyPressed(final KeyEvent event) {
		int deltaX = 0;
		int deltaY = 0;

		switch (event.keyCode) {
		case SWT.ARROW_LEFT:
			deltaX -= OFFSET;
			break;
		case SWT.ARROW_RIGHT:
			deltaX += OFFSET;
			break;
		case SWT.ARROW_UP:
			deltaY -= OFFSET;
			break;
		case SWT.ARROW_DOWN:
			deltaY += OFFSET;
			break;
		}

		switch (event.character) {
		case '+':
			zoomIn();
			break;
		case '-':
			zoomOut();
			break;
		}

		if (deltaX != 0 || deltaY != 0) {
			scrollBy(deltaX, deltaY);
		}
	}

	/** Scroll by a given amount, in pixels. There will be no animation. */
	public void scrollBy(int x, int y) {
		map.setMapCenter(map.getCenter().x + x, map.getCenter().y + y);
	}

	/** Start animating the map towards the given point. */
	public void animateTo(GeoPoint point) {
		setCenter(point);
	}

	/** Set the map view to the given center. There will be no animation. */
	public void setCenter(GeoPoint point) {
		map.setMapCenter(point);
	}

	/** {@inheritDoc} */
	@Override
	public void keyReleased(KeyEvent e) {
		// Do nothing
	}

	/**
	 * We could implement the listener interfaces directly in {@link MapView},
	 * but that would make the interface methods public.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void paintControl(PaintEvent event) {
		map.paintControl(event);
	}
}
