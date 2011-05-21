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
// @ConQAT.Rating YELLOW Hash: 9A9E134C36393AD08FE75E78F45F4E79
package net.skweez.geoclipse.map;

/**
 * An immutable coordinate in the real (geographic) world, composed of a
 * latitude and a longitude.
 * 
 * @author Michael Kanis
 */
public class GeoPoint {

	/**
	 * Denotes the location of a place on Earth (or other planetary body) north
	 * or south of the equator.
	 */
	private final double latitude;

	/**
	 * Longitude is the geographic coordinate most commonly used in cartography
	 * and global navigation for east-west measurement. The meaning of zero
	 * degrees of longitude is established by the line of longitude (meridian)
	 * that passes through the Royal Observatory, Greenwich, in England.
	 */
	private final double longitude;

	/**
	 * Creates a new instance of GeoPosition from the specified latitude and
	 * longitude. These are double values in decimal degrees, not degrees,
	 * minutes, and seconds. Use the other constructor for those.
	 * 
	 * @param latitude
	 *            a latitude value in decmial degrees
	 * @param longitude
	 *            a longitude value in decimal degrees
	 */
	public GeoPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Creates a new instance of GeoPosition from the specified latitude and
	 * longitude. Each are specified as degrees, minutes, and seconds; not as
	 * decimal degrees. Use the other constructor for those.
	 * 
	 * @param latDegrees
	 *            the degrees part of the current latitude
	 * @param latMinutes
	 *            the minutes part of the current latitude
	 * @param latSeconds
	 *            the seconds part of the current latitude
	 * @param lonDegrees
	 *            the degrees part of the current longitude
	 * @param lonMinutes
	 *            the minutes part of the current longitude
	 * @param lonSeconds
	 *            the seconds part of the current longitude
	 */
	public GeoPoint(double latDegrees, double latMinutes, double latSeconds,
			double lonDegrees, double lonMinutes, double lonSeconds) {
		this(latDegrees + (latMinutes + latSeconds / 60.0) / 60.0, lonDegrees
				+ (lonMinutes + lonSeconds / 60.0) / 60.0);
	}

	/**
	 * Get the latitude as decimal degrees
	 * 
	 * @return the latitude as decimal degrees
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Get the longitude as decimal degrees
	 * 
	 * @return the longitude as decimal degrees
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Returns true the specified GeoPosition and this GeoPosition represent the
	 * exact same latitude and longitude coordinates.
	 * 
	 * @param obj
	 *            a GeoPosition to compare this GeoPosition to
	 * @return returns true if the specified GeoPosition is equal to this one
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GeoPoint) {
			GeoPoint coord = (GeoPoint) obj;
			return latitude == coord.latitude && longitude == coord.longitude;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");

		builder.append("latitude:").append(latitude).append(",");
		builder.append("longitude:").append(longitude).append("}");

		return builder.toString();
	}
}
