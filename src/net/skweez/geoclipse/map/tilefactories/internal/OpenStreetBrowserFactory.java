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
 * A tile factory to load tiles from the OpenStreetBrowser server. These are
 * limited to Europe atm.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating YELLOW Rev: 483
 */
public class OpenStreetBrowserFactory extends DefaultTileFactory {

	private static final String NAME = "OpenStreetBrowser (Europe only)";

	private static final String URL = "http://www.openstreetbrowser.org/tiles/base";

	/** Constructor. */
	public OpenStreetBrowserFactory() {
		super(new OpenStreetMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}
