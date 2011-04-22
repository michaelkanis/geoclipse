/* *****************************************************************************
 * $Id: Messages.java 657 2009-12-30 12:27:37Z damumbl $
 * de.byteholder.geoclipse.map
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
package net.skweez.geoclipse.map;

import org.eclipse.osgi.util.NLS;

/**
 * Contains constants for accessing the NLS strings of this bundle.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating YELLOW Rev: 475
 */
/* package */abstract class Messages extends NLS {

	/** Name of this bundle. */
	private static final String BUNDLE_NAME = "net.skweez.geoclipse.map.messages"; //$NON-NLS-1$

	/** Shown on tiles, while loading. */
	public static String MAP_EXTENSIONS_LOADING;

	/** Shown on tiles, that failed loading. */
	public static String MAP_EXTENSIONS_LOADING_FAILED;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
