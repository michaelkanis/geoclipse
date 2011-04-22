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

import java.util.List;

import org.w3c.dom.Element;

import edu.tum.cs.commons.xml.XMLUtils;
import edu.tum.cs.eclipse.commons.xmlmodel.ElementCache;

/**
 * A Track Segment holds a list of Track Points which are logically connected in
 * order. To represent a single GPS track where GPS reception was lost, or the
 * GPS receiver was turned off, start a new Track Segment for each continuous
 * span of track data.
 * 
 * @author Michael Kanis
 */
public class TrackSegment extends GpxElementBase {

	/** Element cache for the contained {@link Waypoint}s. */
	private final ElementCache<Waypoint> waypointCache = new ElementCache<Waypoint>() {
		/** {@inheritDoc} */
		@Override
		protected Waypoint createFromXmlElement(Element e) {
			return new Waypoint(e, model);
		}
	};

	public TrackSegment(Element domNode, GpxDocument model) {
		super(domNode, model);
	}

	public List<Waypoint> getPoints() {
		return waypointCache.getModelElementsFor(XMLUtils.getNamedChildren(
				domNode, XmlTokens.XML_ELEMENT_TRACK_POINT));
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getName() + " (" + getPoints().size() + " points)";
	}
}
