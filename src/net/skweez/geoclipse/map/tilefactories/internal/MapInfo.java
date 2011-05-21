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

import java.net.URL;

import net.skweez.geoclipse.map.internal.Tile;

/**
 * A MapInfo encapsulates all information specific to a map server. This
 * includes everything from the url to load the map tiles from to the size and
 * depth of the tiles. Theoretically any map server can be used by installing a
 * customized TileFactoryInfo.
 * 
 * @author Michael Kanis
 */
public abstract class MapInfo {

	private final int minimumZoomLevel;

	private final int maximumZoomLevel;

	/** The size of each tile (assumes they are square). */
	private final int tileSize;

	/** Base url for loading tiles. */
	private final String baseURL;

	private int defaultZoomLevel;

	/**
	 * Creates a new instance of MapInfo. Note that MapInfo should be considered
	 * invariate, meaning that subclasses should ensure all of the properties
	 * stay the same after the class is constructed. Returning different values
	 * of getTileSize() for example is considered an error and may result in
	 * unexpected behavior.
	 * 
	 * @param baseURL
	 *            the base url for grabbing tiles
	 */
	public MapInfo(final int minimumZoomLevel, final int maximumZoomLevel,
			final int tileSize, final String baseURL) {

		this.minimumZoomLevel = minimumZoomLevel;
		this.maximumZoomLevel = maximumZoomLevel;
		this.baseURL = baseURL;
		this.tileSize = tileSize;
	}

	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * Returns the size of the map at the given zoom, in tiles (num tiles tall
	 * by num tiles wide).
	 */
	public int getMapSize(final int zoom) {
		return getMapWidthInTilesAtZoom(zoom);
	}

	/** Returns the number of tiles the map is wide at the given zoom level. */
	public int getMapWidthInTilesAtZoom(int zoom) {
		return (int) Math.pow(2, zoom);
	}

	public int getMaximumZoom() {
		return maximumZoomLevel;
	}

	public int getMinimumZoom() {
		return minimumZoomLevel;
	}

	/** Returns the tile size. */
	public int getTileSize() {
		return tileSize;
	}

	/**
	 * Returns the tile url for the specified tile at the specified zoom level.
	 * By default it will generate a tile url using the base url and parameters
	 * 
	 * Note that the URL can be a <code>file:</code> url.
	 */
	public abstract URL getTileUrl(final Tile tile);

	/**
	 * Returns the file extension for the tile image files. E.g. "jpg" or "png".
	 */
	public abstract String getFileExtension();
}
