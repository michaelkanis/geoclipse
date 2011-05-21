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
package net.skweez.geoclipse.map.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Category page for the map.
 * 
 * @author Wolfgang Schramm
 * @author Author: Michael Kanis
 */
public class PrefPageMap extends PreferencePage implements
		IWorkbenchPreferencePage {

	/** Constructor. Suppresses the creation of the default buttons. */
	public PrefPageMap() {
		noDefaultAndApplyButton();
	}

	/** {@inheritDoc} */
	@Override
	protected Control createContents(final Composite parent) {
		return null;
	}

	/** {@inheritDoc} */
	public void init(final IWorkbench workbench) {
		// do nothing
	}
}
