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
 * A Track is an ordered list of points describing a path. Tracks consist of one
 * ore more {@link TrackSegment}s.
 * <p>
 * It has the following parameters (which may not all be available in the
 * current implementation).
 * <li><i>name</i>
 * <li><i>comment</i>
 * <li><i>description</i>
 * <li><i>source</i> - Source of data. Included to give user some idea of
 * reliability and accuracy of data.
 * <li><i>link</i> - Links to external information about track.
 * <li><i>number</i> - Track number.
 * <li><i>type</i> - Classification of track.
 * 
 * @author Michael Kanis
 */
public class Track extends GpxElementBase {

	/** Element cache for the contained {@link TrackSegment}s. */
	private final ElementCache<TrackSegment> segmentCache = new ElementCache<TrackSegment>() {
		/** {@inheritDoc} */
		@Override
		protected TrackSegment createFromXmlElement(Element e) {
			return new TrackSegment(e, model);
		}
	};

	public Track(Element domNode, GpxDocument model) {
		super(domNode, model);
	}

	public List<TrackSegment> getSegments() {
		return segmentCache.getModelElementsFor(XMLUtils.getNamedChildren(
				domNode, XmlTokens.XML_ELEMENT_TRACK_SEGMENT));
	}
}
