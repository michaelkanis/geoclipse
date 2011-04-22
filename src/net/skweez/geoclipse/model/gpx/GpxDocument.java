/* *****************************************************************************
 *  Copyright (C) 2008-2009 Michael Kanis and others
 *  $Id: GpxDocument.java 506 2009-04-11 18:10:37Z damumbl $
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.tum.cs.commons.xml.XMLUtils;
import edu.tum.cs.eclipse.commons.xmlmodel.ElementCache;
import edu.tum.cs.eclipse.commons.xmlmodel.XmlRootModelBase;

/**
 * GPX documents contain a metadata header, followed by waypoints, routes, and
 * tracks. You can add your own elements to the extensions section of the GPX
 * document.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 506 $
 * @levd.rating RED Rev:
 */
public class GpxDocument extends XmlRootModelBase {

	/** Element cache for the contained waypoints. */
	private final ElementCache<Waypoint> waypointCache = new ElementCache<Waypoint>() {
		/** {@inheritDoc} */
		@Override
		protected Waypoint createFromXmlElement(Element e) {
			return new Waypoint(e, GpxDocument.this);
		}
	};

	/** Element cache for the contained tracks. */
	private final ElementCache<Track> trackCache = new ElementCache<Track>() {
		/** {@inheritDoc} */
		@Override
		protected Track createFromXmlElement(Element e) {
			return new Track(e, GpxDocument.this);
		}
	};

	/** Element cache for the contained routes. */
	private final ElementCache<Route> routeCache = new ElementCache<Route>() {
		/** {@inheritDoc} */
		@Override
		protected Route createFromXmlElement(Element e) {
			return new Route(e, GpxDocument.this);
		}
	};

	/** Element cache for the metadata. */
	private final ElementCache<GpxElementBase> metaCache = new ElementCache<GpxElementBase>() {
		/** {@inheritDoc} */
		@Override
		protected GpxElementBase createFromXmlElement(Element e) {
			if (e.getLocalName().equals(XmlTokens.XML_ELEMENT_BOUNDS)) {
				return new Bounds(e, GpxDocument.this);
			}
			return null;
		}
	};

	/** Creates a new instance backed by the given DOM {@link Document}. */
	public GpxDocument(Document document) {
		super(document);
	}

	/** Returns the waypoints of this GPX document. */
	public List<Waypoint> getWaypoints() {
		return waypointCache.getModelElementsFor(XMLUtils.getNamedChildren(
				getRootElement(), XmlTokens.XML_ELEMENT_WAYPOINT));
	}

	/** Returns the waypoints of this GPX document. */
	public List<Track> getTracks() {
		return trackCache.getModelElementsFor(XMLUtils.getNamedChildren(
				getRootElement(), XmlTokens.XML_ELEMENT_TRACK));
	}

	/** Returns the waypoints of this GPX document. */
	public List<Route> getRoutes() {
		return routeCache.getModelElementsFor(XMLUtils.getNamedChildren(
				getRootElement(), XmlTokens.XML_ELEMENT_ROUTE));
	}

	public Bounds getBounds() {
		return (Bounds) metaCache.getModelElementsFor(
				XMLUtils.getNamedChildren(getRootElement(),
						XmlTokens.XML_ELEMENT_BOUNDS)).get(0);
	}
}
