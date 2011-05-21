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
// @ConQAT.Rating YELLOW Hash: 573FE1A5B576321F051672C75FA8CE22
package net.skweez.geoclipse;

import org.eclipse.osgi.util.NLS;

/**
 * Contains constants for accessing the NLS strings of this bundle.
 * 
 * @author Michael Kanis
 */
/* package */abstract class Messages extends NLS {

	/** Name of this bundle. */
	private static final String BUNDLE_NAME = "net.skweez.geoclipse.messages"; //$NON-NLS-1$

	/** Shown on tiles, while loading. */
	public static String MAP_EXTENSIONS_LOADING;

	/** Shown on tiles, that failed loading. */
	public static String MAP_EXTENSIONS_LOADING_FAILED;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
