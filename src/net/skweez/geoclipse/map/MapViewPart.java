/* *****************************************************************************
 * $Id: MapView.java 657 2009-12-30 12:27:37Z damumbl $
 * de.byteholder.geoclipse.map
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

package net.skweez.geoclipse.map;

import java.util.List;

import net.skweez.geoclipse.Activator;
import net.skweez.geoclipse.Constants;
import net.skweez.geoclipse.map.tilefactories.ITileFactory;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

/**
 * An Eclipse view that shows a map.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating YELLOW Rev: 484
 */
public class MapViewPart extends ViewPart {

	/** The key used to persist the longitude. */
	private static final String LON_KEY = "x";

	/** The key used to persist the latitude. */
	private static final String LAT_KEY = "y";

	/** The key used to persist the zoom. */
	private static final String ZOOM_KEY = "zoom";

	/** The key used to persist the class name of the tile factory. */
	private static final String TILEFACTORY_KEY = "tilefactory";

	/** The map widget. */
	private MapView map;

	/** Holds all the registered tile factories. */
	private List<ITileFactory> factories;

	/** The zoom to initiatlize the map with. */
	private Integer initalZoom = null;

	/** The latitude to initiatlize the map with. */
	private Float initialLongitude = null;

	/** The longitude to initiatlize the map with. */
	private Float initialLatitude = null;

	/** The tile factory to initiatlize the map with. */
	private String initialTileFactory = null;

	/** Action for zooming into the map. */
	private Action zoomInAction;

	/** Action for zooming out of the map. */
	private Action zoomOutAction;

	/** {@inheritDoc} */
	@Override
	public void createPartControl(Composite parent) {
		map = new MapView(parent);
		factories = Activator.getDefault().getTileFactories();

		Assert.isTrue(factories != null && factories.size() > 0,
				"At least one tile factory is needed to run Geoclipse.");

		// Add actions to toolbar
		makeActions();

		IActionBars bars = getViewSite().getActionBars();

		bars.getToolBarManager().add(zoomInAction);
		bars.getToolBarManager().add(zoomOutAction);

		// Add an entry to the drop down menu for each tile factory
		for (ITileFactory factory : factories) {
			bars.getMenuManager().add(new SwitchMapAction(factory));
		}

		// Initialize the map
		if (initialTileFactory != null) {
			map.setTileFactory(Activator.getDefault().getTileFactory(
					initialTileFactory));
		} else {
			map.setTileFactory(factories.get(0));
		}

		if (initialLongitude != null && initialLatitude != null) {
			map.setMapCenter(new GeoPoint(initialLatitude, initialLongitude));
		} else {
			map.setMapCenter(Constants.START_POSITION);
		}

		if (initalZoom != null) {
			map.setZoom(initalZoom);
		} else {
			map.setZoom(Constants.START_ZOOM);
		}

		Activator.getDefault().makeDefaultTileImages(
				map.getTileFactory().getTileSize());
	}

	/** Create the toolbar actions. */
	private void makeActions() {
		zoomInAction = new Action("Zoom in",
				Activator.getImageDescriptor("icons/zoom_in.png")) {
			/** {@inheritDoc} */
			@Override
			public void run() {
				map.getController().zoomIn();
			}
		};

		zoomOutAction = new Action("Zoom out",
				Activator.getImageDescriptor("icons/zoom_out.png")) {
			/** {@inheritDoc} */
			@Override
			public void run() {
				map.getController().zoomOut();
			}
		};
	}

	/** {@inheritDoc} */
	@Override
	public void setFocus() {
		map.setFocus();
	}

	/**
	 * save the current map information including zoom, latitude and longitude
	 * into the {@link IMemento}
	 */
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);

		memento.putFloat(LON_KEY, (float) map.getMapCenter().getLongitude());
		memento.putFloat(LAT_KEY, (float) map.getMapCenter().getLatitude());
		memento.putInteger(ZOOM_KEY, map.getZoomLevel());
		memento.putString(TILEFACTORY_KEY, map.getTileFactory().getClass()
				.getName());
	}

	/**
	 * restores the map information from the {@link IMemento} values must be set
	 * on the map in the createpart control, because the controls are not yet
	 * instanciated in init().
	 */
	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);

		if (memento != null) {
			initalZoom = memento.getInteger(ZOOM_KEY);
			initialLongitude = memento.getFloat(LON_KEY);
			initialLatitude = memento.getFloat(LAT_KEY);
			initialTileFactory = memento.getString(TILEFACTORY_KEY);
		}
	}

	/** An Action that switches the current tile factory. */
	private class SwitchMapAction extends Action {

		/** The factory to switch to. */
		private final ITileFactory factory;

		/** Constructor. */
		public SwitchMapAction(ITileFactory factory) {
			super(factory.getName());
			this.factory = factory;
		}

		/** {@inheritDoc} */
		@Override
		public void run() {
			ITileFactory oldFactory = map.getTileFactory();
			map.setTileFactory(factory);
			oldFactory.dispose();
		}
	}
}
