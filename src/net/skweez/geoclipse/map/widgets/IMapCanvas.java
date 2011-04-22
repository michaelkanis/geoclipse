/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: IMapCanvas.java 657 2009-12-30 12:27:37Z damumbl $
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

import net.skweez.geoclipse.map.Tile;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Listener;


/**
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public interface IMapCanvas {

	/** Draw the tile at the tilePosition. */
	public void drawTile(Tile tile, Rectangle targetRectangle);

	/** Fill the given rectangle with the background color. */
	public void drawBackground(final Rectangle targetRectangle);

	/**
	 * Put a map redraw into the GUI thread queue. Only the last entry in the
	 * queue will be executed.
	 */
	public void queueRedraw();

	/** See {@link org.eclipse.swt.widgets.Control#setCursor(Cursor)}. */
	public void setCursor(Cursor cursor);

	/** See {@link org.eclipse.swt.widgets.Widget#addListener(int, Listener)}. */
	public void addListener(int eventType, Listener listener);

	/** See {@link org.eclipse.swt.widgets.Composite#getClientArea()}. */
	public org.eclipse.swt.graphics.Rectangle getClientArea();

	/** See {@link org.eclipse.swt.widgets.Composite#setFocus()}. */
	public boolean setFocus();

	/** See {@link org.eclipse.swt.widgets.Widget#dispose()}. */
	public void dispose();

	/** See {@link org.eclipse.swt.widgets.Widget#isDisposed()}. */
	public boolean isDisposed();
}
