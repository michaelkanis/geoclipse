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

package net.skweez.geoclipse.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 463 $
 * @levd.rating YELLOW Rev: 463
 */
public class Activator extends AbstractUIPlugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "net.skweez.geoclipse";

	/** The shared instance. */
	private static Activator plugin;

	/** Constructor. */
	public Activator() {
		plugin = this;
	}

	/** {@inheritDoc} */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
	}

	/** {@inheritDoc} */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/** Returns the shared instance. */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for images relative to the plug-in's icons
	 * path.
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, "icons/" + path); //$NON-NLS-1$
	}
}
