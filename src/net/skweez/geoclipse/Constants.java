/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: Constants.java 657 2009-12-30 12:27:37Z damumbl $
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
package net.skweez.geoclipse;

import net.skweez.geoclipse.model.GeoPoint;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

/**
 * A class containing constants used throughout the program.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public abstract class Constants {

	/** The mouse cursor that is shown when the user moves the map. */
	public static final Cursor CURSOR_PAN = new Cursor(Display.getCurrent(),
			SWT.CURSOR_SIZEALL);

	/** The default mouse cursor. */
	public static final Cursor CURSOR_DEFAULT = new Cursor(
			Display.getCurrent(), SWT.CURSOR_ARROW);

	/**
	 * This is the color that is used to draw the background of error and
	 * loading tiles.
	 */
	public static final Color BACKGROUND_COLOR = Display.getCurrent()
			.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

	/** The key for the loading image in the {@link ImageRegistry}. */
	public static final String LOADING_IMG_KEY = "loading-img";

	/** The key for the error image in the {@link ImageRegistry}. */
	public static final String ERROR_IMG_KEY = "error-img";

	/** The position to start at. */
	public static final GeoPoint START_POSITION = new GeoPoint(0, 0);

	/** The zoom level to start at. */
	public static final int START_ZOOM = 0;

}
