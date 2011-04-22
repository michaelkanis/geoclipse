/* *****************************************************************************
 * de.byteholder.geoclipse.core
 * $Id: GpxElementBase.java 506 2009-04-11 18:10:37Z damumbl $
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
package net.skweez.geoclipse.model.gpx;

import static net.skweez.geoclipse.model.gpx.XmlTokens.XML_ELEMENT_NAME;

import org.w3c.dom.Element;

import edu.tum.cs.commons.xml.XMLUtils;
import edu.tum.cs.eclipse.commons.xmlmodel.ElementBase;

/**
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 506 $
 * @levd.rating RED Rev:
 */
public abstract class GpxElementBase extends ElementBase<GpxDocument> {

	/** Constructor. */
	public GpxElementBase(Element domNode, GpxDocument model) {
		super(domNode, model);
	}

	/**
	 * Given the name of an attribute, returns the value for that attribute or
	 * an empty {@link String} if it is not set and no default value is defined.
	 */
	protected String getAttribute(String attributeName) {
		return domNode.getAttribute(attributeName);
	}

	/** Returns the value of a child of this element. */
	protected String getElementValue(String elementName) {
		if (XMLUtils.getNamedChildren(domNode, elementName).size() > 0) {
			return XMLUtils.getNamedChildren(domNode, elementName).get(0)
					.getTextContent();
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		if (getElementValue(XML_ELEMENT_NAME) != null) {
			return getElementValue(XML_ELEMENT_NAME);
		}
		return getClass().getSimpleName();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		if (!getName().equals("")) {
			return getName();
		}
		return super.toString();
	}
}