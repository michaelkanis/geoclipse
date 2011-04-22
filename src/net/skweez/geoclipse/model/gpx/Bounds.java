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

import java.util.ArrayList;
import java.util.List;

import net.skweez.geoclipse.model.GeoPoint;

import org.w3c.dom.Element;

/**
 * Two lat/lon pairs defining the extent of an element.
 * 
 * @author Michael Kanis
 */
public class Bounds extends GpxElementBase {

	public Bounds(Element domNode, GpxDocument model) {
		super(domNode, model);
	}

	public double getMaxLat() {
		return Double.parseDouble(getAttribute(XmlTokens.XML_ATTRIBUTE_MAXLAT));
	}

	public double getMaxLon() {
		return Double.parseDouble(getAttribute(XmlTokens.XML_ATTRIBUTE_MAXLON));
	}

	public double getMinLat() {
		return Double.parseDouble(getAttribute(XmlTokens.XML_ATTRIBUTE_MINLAT));
	}

	public double getMinLon() {
		return Double.parseDouble(getAttribute(XmlTokens.XML_ATTRIBUTE_MINLON));
	}

	public List<GeoPoint> toList() {
		List<GeoPoint> positions = new ArrayList<GeoPoint>();
		positions.add(new GeoPoint(getMinLat(), getMinLon()));
		positions.add(new GeoPoint(getMaxLat(), getMaxLon()));
		return positions;
	}
}
