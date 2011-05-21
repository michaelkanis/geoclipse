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

import org.eclipse.swt.graphics.GC;

/**
 * Base class for overlays that may be drawn on top of the map. To add an
 * overlay, use the extension point <code>net.skweez.geoclipse.overlays</code>
 * and subclass this class.
 * 
 * @author Michael Kanis
 */
public abstract class Overlay {

	/** The extension point ID for map overlays. */
	public static final String EXTENSION_POINT = "net.skweez.geoclipse.overlays";

	/** Draw the overlay over the map. */
	public void draw(GC gc, MapView mapView) {
		draw(gc, mapView, false);
	}

	/**
	 * Draw the overlay over the map. This will be called on all active overlays
	 * with shadow=true, to lay down the shadow layer, and then again on all
	 * overlays with shadow=false. By default, draws nothing.
	 * 
	 * This is private for now, because shadow layers are not yet supported.
	 */
	@SuppressWarnings("unused")
	private void draw(GC gc, MapView mapView, boolean shadowLayer) {
		// Draw nothing by default
	}

}
