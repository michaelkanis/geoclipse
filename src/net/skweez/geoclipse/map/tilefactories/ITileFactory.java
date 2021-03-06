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
package net.skweez.geoclipse.map.tilefactories;

import net.skweez.geoclipse.map.internal.Tile;

/**
 * The interface for all tile factories, which are responsible of fetching image
 * data for the map. You probably shouldn't implment this interface directly,
 * but extend {@link BaseTileFactory}.
 * 
 * @author Michael Kanis
 */
public interface ITileFactory {

	/**
	 * Returns a Dimension containing the width and height of the map, in tiles
	 * at the given zoom level. So a Dimension that returns 10x20 would be 10
	 * tiles wide and 20 tiles tall. These values can be multiplied by
	 * getTileSize() to determine the pixel width/height for the map at the
	 * given zoom level.
	 */
	public abstract int getMapSize(int zoom);

	/**
	 * Convenient method. This is the same as {@link #getMapSize(int)} *
	 * {@link #getTileSize()}.
	 */
	public abstract int getMapSizeInPixels(int zoom);

	/**
	 * Return the Tile at a given TilePoint and zoom level. This method must not
	 * return null. However, it can return dummy tiles that contain no data if
	 * it wants. This is appropriate, for example, for tiles which are outside
	 * of the bounds of the map and if the factory doesn't implement wrapping.
	 */
	public abstract Tile getTile(int x, int y, int zoom);

	/** Returns the size of the tiles in pixels. All tiles must be square. */
	public abstract int getTileSize();

	/** Returns the human readable name for that factory. */
	public abstract String getName();

	/** The factory's minimum zoom. */
	public abstract int getMinimumZoom();

	/** The factory's maximum zoom. */
	public abstract int getMaxZoomLevel();

	/** Implementing classes can get rid of used resources in this method. */
	public void dispose();
}