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
package net.skweez.geoclipse.map.tilefactories.internal;


/**
 * TODO
 * 
 * @author Michael Kanis
 */
public class DefaultTileFactory extends CachedTileFactoryBase {

	public DefaultTileFactory(MapInfo info) {
		super(info);
	}

	/** Returns the {@link MapInfo} describing this tile factory. */
	public MapInfo getInfo() {
		return mapInfo;
	}

	/** Returns the size of the world bitmap at the given zoom in <b>tiles</b>. */
	@Override
	public int getMapSize(final int zoom) {
		return mapInfo.getMapSize(zoom);
	}

	/** Gets the size of the tiles in pixels. Tiles are square. */
	@Override
	public int getTileSize() {
		return mapInfo.getTileSize();
	}

	/** {@inheritDoc} */
	@Override
	public int getMaxZoomLevel() {
		return mapInfo.getMaximumZoom();
	}

	/** {@inheritDoc} */
	@Override
	public int getMinimumZoom() {
		return mapInfo.getMinimumZoom();
	}
}
