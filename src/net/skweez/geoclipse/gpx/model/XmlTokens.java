/* *****************************************************************************
 * de.byteholder.geoclipse.core
 * $Id: XmlTokens.java 506 2009-04-11 18:10:37Z damumbl $
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
package net.skweez.geoclipse.gpx.model;

/**
 * Holds all the names for the XML elements and attributes of GPX files.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 506 $
 * @levd.rating RED Rev:
 */
public abstract class XmlTokens {

	public static final String XML_ELEMENT_WAYPOINT = "wpt";

	public static final String XML_ELEMENT_TRACK = "trk";

	public static final String XML_ELEMENT_TRACK_SEGMENT = "trkseg";

	public static final String XML_ELEMENT_TRACK_POINT = "trkpt";

	public static final String XML_ELEMENT_ROUTE = "rte";

	public static final String XML_ELEMENT_ROUTE_POINT = "rtept";

	public static final String XML_ATTRIBUTE_LATITUDE = "lat";

	public static final String XML_ATTRIBUTE_LONGITUDE = "lon";

	public static final String XML_ELEMENT_SYMBOL = "sym";

	public static final String XML_ELEMENT_NAME = "name";

	public static final String XML_ELEMENT_BOUNDS = "bounds";

	public static final String XML_ATTRIBUTE_MINLAT = "minlat";

	public static final String XML_ATTRIBUTE_MAXLAT = "maxlat";

	public static final String XML_ATTRIBUTE_MINLON = "minlon";

	public static final String XML_ATTRIBUTE_MAXLON = "maxlon";
}
