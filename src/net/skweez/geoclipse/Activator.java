/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: Activator.java 657 2009-12-30 12:27:37Z damumbl $
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
package net.skweez.geoclipse;

import static net.skweez.geoclipse.Constants.ERROR_IMG_KEY;
import static net.skweez.geoclipse.Constants.LOADING_IMG_KEY;

import java.util.ArrayList;
import java.util.List;

import net.skweez.geoclipse.map.Overlay;
import net.skweez.geoclipse.map.tilefactories.ITileFactory;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating YELLOW Rev: 484
 */
public class Activator extends AbstractUIPlugin {

	/** The plug-in ID. */
	private static final String PLUGIN_ID = "net.skweez.geoclipse";

	/** The extension point ID for tile factories. */
	/* package */static final String TILE_FACTORY_EXTENSION_POINT = "net.skweez.geoclipse.TileFactory";

	/** The extension point ID for map overlays. */
	/* package */static final String OVERLAY_EXTENSION_POINT = "net.skweez.geoclipse.Overlay";

	/** The shared instance. */
	private static Activator plugin;

	/** The extension registry. */
	private final IExtensionRegistry registry = RegistryFactory.getRegistry();

	/** Holds all registered tile factories. */
	private List<ITileFactory> tileFactories;

	/** Holds all registered tile map overlays. */
	private List<Overlay> overlays;

	/** {@inheritDoc} */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		tileFactories = readExtensionList(TILE_FACTORY_EXTENSION_POINT);
		overlays = readExtensionList(OVERLAY_EXTENSION_POINT);
	}

	/** {@inheritDoc} */
	@Override
	public void stop(final BundleContext context) throws Exception {

		for (ITileFactory factory : getTileFactories()) {
			factory.dispose();
		}

		plugin = null;
		super.stop(context);
	}

	/** Reads all the registered extensions for the given extension point. */
	@SuppressWarnings("unchecked")
	private <T> List<T> readExtensionList(String extensionPoint) {
		List<T> extensionList = new ArrayList<T>();

		for (IExtension extension : registry.getExtensionPoint(extensionPoint)
				.getExtensions()) {

			// The extension point has only one configuration element
			IConfigurationElement element = extension
					.getConfigurationElements()[0];

			try {
				Object obj = element.createExecutableExtension("class");
				extensionList.add((T) obj);
			} catch (final CoreException e) {
				Assert.isTrue(false, e.getMessage());
			}
		}

		return extensionList;
	}

	/** Returns a list with all registered tile factories. */
	public List<ITileFactory> getTileFactories() {
		return tileFactories;
	}

	/** Returns overlays. */
	public List<Overlay> getOverlays() {
		return overlays;
	}

	/**
	 * Returns the tile factory with the given class name or null if none such
	 * is registered.
	 */
	public ITileFactory getTileFactory(final String className) {
		for (final ITileFactory factory : tileFactories) {
			if (factory.getClass().getName().equals(className)) {
				return factory;
			}
		}
		return null;
	}

	/**
	 * Creates images for loading and error tiles and registers them with the
	 * {@link ImageRegistry}.
	 */
	public void makeDefaultTileImages(int size) {
		getImageRegistry().remove(LOADING_IMG_KEY);
		getImageRegistry().remove(ERROR_IMG_KEY);

		getImageRegistry().put(LOADING_IMG_KEY,
				getTileImage(size, Messages.MAP_EXTENSIONS_LOADING));

		getImageRegistry().put(ERROR_IMG_KEY,
				getTileImage(size, Messages.MAP_EXTENSIONS_LOADING_FAILED));
	}

	/**
	 * Returns a image in the given size with an ocean-blue background an the
	 * given message in the upper left corner.
	 */
	private Image getTileImage(int size, final String message) {
		final Image img = new Image(Display.getCurrent(), size, size);
		final GC gc = new GC(img);

		gc.setBackground(Constants.BACKGROUND_COLOR);
		gc.fillRectangle(0, 0, size, size);
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.drawString(message, 5, 5);

		gc.dispose();
		return img;
	}

	/** Returns the shared instance. */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
