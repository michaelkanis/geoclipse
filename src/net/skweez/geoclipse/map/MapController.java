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

import java.util.Timer;
import java.util.TimerTask;

import net.skweez.geoclipse.Constants;
import net.skweez.geoclipse.map.internal.PanAnimation;

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
 * A utility class to manage mouse and keyboard interactions. It implements all
 * the listener interfaces for mouse and keyboard interactions with the map.
 * This includes panning and zooming.
 * 
 * @author Michael Kanis
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

	/** Time for animations. */
	private Timer timer;

	/** Set the MapView this controller belongs to. */
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
	public boolean zoomInFixing(int xPixel, int yPixel) {
		animateTo(map.getProjection().pixelToGeo(xPixel, yPixel));
		return zoomIn();
	}

	/** Increase the zoom level by one. */
	public boolean zoomIn() {
		final GeoPoint center = map.getMapCenter();
		boolean b = setZoom(map.getZoomLevel() + 1);
		map.setMapCenter(center);
		return b;
	}

	/** Decrease the zoom level by one. */
	public boolean zoomOut() {
		final GeoPoint center = map.getMapCenter();
		boolean b = setZoom(map.getZoomLevel() - 1);
		map.setMapCenter(center);
		return b;
	}

	/**
	 * Attempts to adjust the zoom of the map so that the given span of latitude
	 * and longitude will be displayed.
	 */
	public void zoomToSpan(double latSpan, double lonSpan) {

		double currentLatSpan = map.getLatitudeSpan();
		double currentLonSpan = map.getLongitudeSpan();
		while (currentLatSpan > latSpan | currentLonSpan > lonSpan) {
			zoomIn();
			currentLatSpan = map.getLatitudeSpan();
			currentLonSpan = map.getLongitudeSpan();
		}
	}

	/** Sets the zoomlevel of the map. */
	public boolean setZoom(int zoomLevel) {
		return map.setZoomLevel(zoomLevel);
	}

	/**
	 * Only saves state and position of the cursor. This is needed for panning.
	 */
	@Override
	public void mouseDown(final MouseEvent e) {

		stopAnimation(false);

		if (e.button == 1) {
			oldPosition = new Point(e.x, e.y);
			isLeftMouseButtonPressed = true;
		} else if (e.button == 2) {
			Projection projection = map.getProjection();
			Point offset = map.getOffset();
			animateTo(projection.pixelToGeo(offset.x + e.x, offset.y + e.y));
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

	/** Resets state for primary mouse button. */
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

		stopAnimation(false);

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
	public void animateTo(final GeoPoint point) {
		timer = new Timer();
		TimerTask task = new PanAnimation(map.getMapCenter(), point, this, 600);
		timer.scheduleAtFixedRate(task, 0, 30);
	}

	/**
	 * Stops any animation that may be in progress, and conditionally update the
	 * map center to whatever offset the partial animation had achieved.
	 * 
	 * @param jumpToFinish
	 *            - if true, we'll shortcut the animation to its endpoint. if
	 *            false, we'll cut it off where it stands. <strong>This is not
	 *            yet implemented!</strong>
	 */
	public void stopAnimation(boolean jumpToFinish) {
		if (timer != null) {
			timer.cancel();
		}
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
