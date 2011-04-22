/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: ETileStatus.java 657 2009-12-30 12:27:37Z damumbl $
 *
 * Copyright (C) 2009 Michael Kanis and others
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

/**
 * The different states a tile can have.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public enum ETileStatus {

	/** The tile could not be loaded because of some error. */
	ERROR,

	/** The tile is currently still loading. */
	LOADING,

	/** The tile has finished loading. */
	READY,

	/** The tile has just been instantiated, but is not yet loading. */
	NEW
}
