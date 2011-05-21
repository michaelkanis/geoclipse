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
package net.skweez.geoclipse.map.tilefactories;

import java.awt.Dimension;

/**
 * A base class for tile factories. You can extend this class instead of
 * implementing {@link ITileFactory} directly.
 * 
 * @author Michael Kanis
 */
public abstract class BaseTileFactory implements ITileFactory {

	/**
	 * Dispose of all of the images and other SWT
	 * {@link org.eclipse.swt.graphics.Resource}s.
	 * 
	 * This should be called when a tile factory is no longer used.
	 */
	public void dispose() {
		// do nothing by default
	}

	/** {@inheritDoc} */
	public String getName() {
		return getClass().getSimpleName();
	}

	/** {@inheritDoc} */
	@Override
	public Dimension getMapSizeInPixels(int zoom) {
		return new Dimension(getMapSize(zoom).width * getTileSize(),
				getMapSize(zoom).height * getTileSize());
	}
}
