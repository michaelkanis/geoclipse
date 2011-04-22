/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: DefaultTileFactory.java 558 2009-06-27 08:24:48Z damumbl $
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
package net.skweez.geoclipse.map.tilefactories;

import java.awt.Dimension;

import net.skweez.geoclipse.projections.IProjection;


/**
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating RED Rev:
 */
public class DefaultTileFactory extends CachedTileFactoryBase {

	public DefaultTileFactory(IProjection projection, MapInfo info) {
		super(projection, info);
	}

	/** Returns the {@link MapInfo} describing this tile factory. */
	public MapInfo getInfo() {
		return mapInfo;
	}

	/** Returns the size of the world bitmap at the given zoom in <b>tiles</b>. */
	@Override
	public Dimension getMapSize(final int zoom) {
		return mapInfo.getMapSize(zoom);
	}

	/** Gets the size of the tiles in pixels. Tiles are square. */
	@Override
	public int getTileSize() {
		return mapInfo.getTileSize();
	}

	/** {@inheritDoc} */
	@Override
	public int getMaximumZoom() {
		return mapInfo.getMaximumZoom();
	}

	/** {@inheritDoc} */
	@Override
	public int getMinimumZoom() {
		return mapInfo.getMinimumZoom();
	}
}
