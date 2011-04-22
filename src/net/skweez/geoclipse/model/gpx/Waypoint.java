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

package net.skweez.geoclipse.model.gpx;

import static net.skweez.geoclipse.model.gpx.XmlTokens.XML_ATTRIBUTE_LATITUDE;
import static net.skweez.geoclipse.model.gpx.XmlTokens.XML_ATTRIBUTE_LONGITUDE;

import org.w3c.dom.Element;

/**
 * A Waypoint represents a waypoint, point of interest, or named feature on a
 * map.
 * 
 * @author Michael Kanis
 */
public class Waypoint extends GpxElementBase {

	protected Waypoint(Element domNode, GpxDocument model) {
		super(domNode, model);
	}

	public double getLongitude() {
		return Double
				.parseDouble(domNode.getAttribute(XML_ATTRIBUTE_LONGITUDE));
	}

	public double getLatitude() {
		return Double.parseDouble(domNode.getAttribute(XML_ATTRIBUTE_LATITUDE));
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return Waypoint.class.getSimpleName() + " [" + getLongitude() + ","
				+ getLatitude() + ": " + getName() + "]";
	}
}
