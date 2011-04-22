/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: MapKeyListener.java 657 2009-12-30 12:27:37Z damumbl $
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

import java.awt.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * Used to pan using the arrow keys.
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class MapKeyListener implements Listener {

	/** The amount by which the map is moved, when pressing the arrow keys. */
	private static final int OFFSET = 10;

	/** The Map this listener is handling. */
	private final MapBase map;

	/** Constructor. */
	public MapKeyListener(MapBase map) {
		this.map = map;
	}

	/** {@inheritDoc} */
	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.KeyDown:
			keyDown(event);
		}
	}

	/** Handle key presses. */
	private void keyDown(final Event event) {
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
}
