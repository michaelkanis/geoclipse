/* *****************************************************************************
 *  Copyright (C) 2008 Michael Kanis and others
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
 *******************************************************************************/

package net.skweez.geoclipse.util;

import net.skweez.geoclipse.model.GeoPoint;

/**
 * A utility class for geographical calculations.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 464 $
 * @levd.rating YELLOW Rev: 464
 */
public class GeoUtils {

	/**
	 * The earth is no perfect sphere but a bit flattened. It's radius is,
	 * according to different sources between 6336 and 6399 km. For Europe and
	 * North America 6380 km might be a good value.
	 */
	private static final double EARTH_RADIUS = 6380d;

	/** Returns the distance between two points on earth, pos1 and pos2 in km. */
	public static double getDistance(final GeoPoint pos1,
			final GeoPoint pos2) {

		final double lat1 = Math.toRadians(pos1.getLatitude());
		final double lon1 = Math.toRadians(pos1.getLongitude());
		final double lat2 = Math.toRadians(pos2.getLatitude());
		final double lon2 = Math.toRadians(pos2.getLongitude());

		return (Math.acos(Math.sin(lat2) * Math.sin(lat1) + Math.cos(lat2)
				* Math.cos(lat1) * Math.cos(lon2 - lon1)) * EARTH_RADIUS);
	}
}
