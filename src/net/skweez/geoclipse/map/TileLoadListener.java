/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: TileLoadListener.java 657 2009-12-30 12:27:37Z damumbl $
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

import java.util.Observable;
import java.util.Observer;

import edu.tum.cs.commons.assertion.CCSMAssert;

/**
 * This listener puts a redraw request in the draw queue of the widget when a
 * tile has been loaded completely. This is necessary because tiles are loaded
 * asynchronously.
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class TileLoadListener implements Observer {

	private final MapCanvas canvas;

	public TileLoadListener(MapCanvas canvas) {
		this.canvas = canvas;
	}

	/** Informs the map that it must redraw. */
	public void update(final Observable o, final Object arg) {
		CCSMAssert.isInstanceOf(o, Tile.class);
		canvas.queueRedraw();
		((Tile) o).deleteObserver(this);
	}
}
