/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: OpenStreetMapInfo.java 558 2009-06-27 08:24:48Z damumbl $
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

import java.net.MalformedURLException;
import java.net.URL;

import net.skweez.geoclipse.map.internal.Tile;


/**
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating RED Rev:
 */
public class OpenStreetMapInfo extends MapInfo {

	private static final char URL_SEPARATOR = '/';

	private static final int MIN_ZOOM = 0;

	private static final int MAX_ZOOM = 17;

	private static final int TILE_SIZE = 256;

	private static final String FILE_EXT = "png";

	public OpenStreetMapInfo(String baseUrl) {
		super(MIN_ZOOM, MAX_ZOOM, TILE_SIZE, baseUrl);
	}

	/** {@inheritDoc} */
	@Override
	public URL getTileUrl(Tile tile) {
		final StringBuilder url = new StringBuilder(this.getBaseURL()).append(
				URL_SEPARATOR).append(tile.getZoom()).append(URL_SEPARATOR)
				.append(tile.getX()).append(URL_SEPARATOR).append(tile.getY())
				.append('.').append(FILE_EXT);

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
