/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: GLMap.java 657 2009-12-30 12:27:37Z damumbl $
 *
 * Copyright (C) 2009 mks and others
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
package net.skweez.geoclipse.map.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class GLMap extends SWTMap {

	/** Constructor. */
	public GLMap(Composite parent) {
		super(parent);
	}

	/** {@inheritDoc} */
	@Override
	protected void setupWidget() {
		GLData data = new GLData();
		data.doubleBuffer = true;
		canvas = new GLMapCanvas(parent, SWT.NONE, data);
	}
}
