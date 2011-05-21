/*
 * Copyright (C) 2009-2011 Michael Kanis and others
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
 */
package net.skweez.geoclipse.map.tilefactories.internal;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;

import net.skweez.geoclipse.map.internal.Tile;

/**
 * A {@link MapInfo} for WMS based servers. It is, however, currently pretty
 * specific to the {@link NasaBlueMarbleFactory}.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 561 $
 * @levd.rating YELLOW Rev: 561
 */
public class WmsMapInfo extends MapInfo {

	/** The minimal zoom. */
	private static final int MIN_ZOOM = 1;

	/** The maximal zoom. */
	private static final int MAX_ZOOM = 10;

	/** The file extension for the tile image files. */
	private static final String FILE_EXT = "jpg";

	/** Constructor. */
	public WmsMapInfo(String baseUrl) {
		super(MIN_ZOOM, MAX_ZOOM, 480, baseUrl);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This is in the ratio 2:1, except for the zoom level 0, where it is 1 tile
	 * wide and 1 tile high.
	 */
	@Override
	public Dimension getMapSize(final int zoom) {
		return new Dimension(getMapWidthInTilesAtZoom(zoom), Math.max(1,
				getMapWidthInTilesAtZoom(zoom) / 2));
	}

	/** {@inheritDoc} */
	@Override
	public URL getTileUrl(Tile tile) {
		final StringBuilder url = new StringBuilder(this.getBaseURL());

		final Dimension mapSize = getMapSize(tile.getZoom());

		final int y = mapSize.height - 1 - tile.getY();

		// 0° = 0
		// 360° = mapSize + 1

		final double lonPerTile = 360.0 / (mapSize.getWidth());
		final double latPerTile = 180.0 / (mapSize.getHeight());

		final double startLon = tile.getX() * lonPerTile - 180.0;
		final double endLon = startLon + lonPerTile;
		final double startLat = y * latPerTile - 90.0;
		final double endLat = startLat + latPerTile;

		url.append(startLon).append(",");
		url.append(startLat).append(",");
		url.append(endLon).append(",");
		url.append(endLat);

		try {
			return new URL(url.toString());
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getFileExtension() {
		return FILE_EXT;
	}
}