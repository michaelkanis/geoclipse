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
package net.skweez.geoclipse.map.widgets;

import java.awt.Point;
import java.awt.Rectangle;

import net.skweez.geoclipse.map.Constants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;

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
public class MapMouseListener implements MouseListener, MouseMoveListener,
		MouseWheelListener, KeyListener {

	/** The amount by which the map is moved, when pressing the arrow keys. */
	private static final int OFFSET = 10;

	/** The Map this listener is handling. */
	private final MapBase map;

	/** The canvas to draw on. */
	private final SWTMapCanvas canvas;

	/** Holds the mouse position for the previous event. */
	private Point oldPosition;

	/** Holds the state of the left mouse button. */
	private boolean isLeftMouseButtonPressed = false;

	/** Constructor. */
	public MapMouseListener(MapBase map, SWTMapCanvas canvas) {
		this.map = map;
		this.canvas = canvas;
	}

	/** Zoom in or out when the user scrolls on the canvas. */
	@Override
	public void mouseScrolled(final MouseEvent event) {
		if (event.count < 0) {
			map.zoomOut();
		} else {
			map.zoomIn();
		}
	}

	/** Zooms in to the position clicked on. */
	@Override
	public void mouseDoubleClick(final MouseEvent event) {
		if (event.button == 1) {
			Rectangle viewport = map.getViewport();
			map.setCenter(viewport.x + event.x, viewport.y + event.y);
			map.zoomIn();
		}
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

		canvas.setCursor(Constants.CURSOR_PAN);

		final Rectangle viewport = map.getViewport();
		viewport.x += oldPosition.x - e.x;
		viewport.y += oldPosition.y - e.y;
		map.setViewport(viewport);

		map.updatePosition();

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
			canvas.setCursor(Constants.CURSOR_DEFAULT);
		}
	}

	/** Handle key presses. */
	@Override
	public void keyPressed(final KeyEvent event) {
		int delta_x = 0;
		int delta_y = 0;

		switch (event.keyCode) {
		case SWT.ARROW_LEFT:
			delta_x -= OFFSET;
			break;
		case SWT.ARROW_RIGHT:
			delta_x += OFFSET;
			break;
		case SWT.ARROW_UP:
			delta_y -= OFFSET;
			break;
		case SWT.ARROW_DOWN:
			delta_y += OFFSET;
			break;
		}

		switch (event.character) {
		case '+':
			map.zoomIn();
			break;
		case '-':
			map.zoomOut();
			break;
		}

		if (delta_x != 0 || delta_y != 0) {
			final Rectangle viewport = map.getViewport();

			viewport.x += delta_x;
			viewport.y += delta_y;
			map.setViewport(viewport);
			map.updatePosition();
		}

		map.queueRedraw();
	}

	/** {@inheritDoc} */
	@Override
	public void keyReleased(KeyEvent e) {
		// Do nothing
	}
}
