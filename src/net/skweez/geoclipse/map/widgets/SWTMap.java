/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: SWTMap.java 657 2009-12-30 12:27:37Z damumbl $
 *
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
 ******************************************************************************/
package net.skweez.geoclipse.map.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * A SWT widget to show a world map. It draws on a SWT canvas.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public class SWTMap extends MapBase {

	/** The components parent. */
	protected Composite parent;

	/** Creates a new map widget. */
	public SWTMap(final Composite parent) {
		this.parent = parent;
		setupWidget();
		setupListeners();
	}

	/** Setup the widget. */
	protected void setupWidget() {
		canvas = new SWTMapCanvas(this, parent);
	}

	/** Setup the listeners. */
	protected void setupListeners() {
		MapMouseListener mouseListener = new MapMouseListener(this, canvas);
		canvas.addListener(SWT.MouseDown, mouseListener);
		canvas.addListener(SWT.MouseUp, mouseListener);
		canvas.addListener(SWT.MouseDoubleClick, mouseListener);
		canvas.addListener(SWT.MouseMove, mouseListener);
		canvas.addListener(SWT.MouseWheel, mouseListener);

		MapKeyListener keyListener = new MapKeyListener(this);
		canvas.addListener(SWT.KeyDown, keyListener);

		canvas.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				viewport.width = canvas.getClientArea().width;
				viewport.height = canvas.getClientArea().height;
				updateViewport();
				queueRedraw();
			}
		});
	}

	/** Get rid of SWT resoures. */
	public void dispose() {
		canvas.dispose();
	}
}
