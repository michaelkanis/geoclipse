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
 * A tile factory to load tiles from the OpenStreetMap Tiles@home servers. These
 * are rendered using the Osmarender program.
 * 
 * @author Michael Kanis
 */
public class OsmTilesAtHomeFactory extends DefaultTileFactory {

	private static final String NAME = "OSM Osmarender";

	private static final String URL = "http://tah.openstreetmap.org/Tiles/tile";

	/** Constructor. */
	public OsmTilesAtHomeFactory() {
		super(new OpenStreetMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}
