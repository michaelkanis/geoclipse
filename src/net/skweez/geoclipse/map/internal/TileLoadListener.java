/*
 *  Copyright (C) 2009-2011 Michael Kanis and others
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
package net.skweez.geoclipse.map.internal;

import java.util.Observable;
import java.util.Observer;

import net.skweez.geoclipse.map.MapView;

import org.eclipse.core.runtime.Assert;

/**
 * This listener puts a redraw request in the draw queue of the widget when a
 * tile has been loaded completely. This is necessary because tiles are loaded
 * asynchronously.
 * 
 * @author Michael Kanis
 */
public class TileLoadListener implements Observer {

	private final MapView canvas;

	public TileLoadListener(MapView canvas) {
		this.canvas = canvas;
	}

	/** Informs the map that it must redraw. */
	public void update(final Observable o, final Object arg) {
		Assert.isTrue(o instanceof Tile);
		canvas.queueRedraw();
		((Tile) o).deleteObserver(this);
	}
}
