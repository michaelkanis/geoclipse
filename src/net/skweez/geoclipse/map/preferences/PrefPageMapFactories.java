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

import java.util.List;

import net.skweez.geoclipse.Activator;
import net.skweez.geoclipse.map.tilefactories.ITileFactory;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This preference page shows all available tile factories. It will allow the
 * user to enable and disable them in the future.
 * 
 * @author Wolfgang Schramm
 * @author Michael Kanis
 */
public class PrefPageMapFactories extends PreferencePage implements
		IWorkbenchPreferencePage {

	/** The {@link TableViewer} that holds the available tile factories, */
	private TableViewer viewer;

	/** Simple content provider for the viewer. */
	private static class MapContentProvider implements
			IStructuredContentProvider {

		/** {@inheritDoc} */
		@SuppressWarnings("unchecked")
		public Object[] getElements(final Object inputElement) {
			return ((List<ITileFactory>) inputElement).toArray();
		}

		/** {@inheritDoc} */
		public void dispose() {
			// do nothing
		}

		/** {@inheritDoc} */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}
	}

	/** Simple label provider for the viewer. */
	private static class TileFactoryLabelProvider extends BaseLabelProvider
			implements ILabelProvider {

		/** {@inheritDoc} */
		@Override
		public Image getImage(Object element) {
			return null;
		}

		/** {@inheritDoc} */
		@Override
		public String getText(Object element) {
			return ((ITileFactory) element).getName();
		}
	}

	/** {@inheritDoc} */
	@Override
	protected Control createContents(final Composite parent) {

		final Composite uiContainer = new Composite(parent, SWT.NONE);
		uiContainer.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(uiContainer);

		Label factoriesLabel = new Label(uiContainer, SWT.NONE);
		factoriesLabel.setText(Messages.pref_map_lbl_avail_map_provider);

		viewer = new TableViewer(uiContainer);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(viewer.getTable());

		viewer.setLabelProvider(new TileFactoryLabelProvider());
		viewer.setContentProvider(new MapContentProvider());

		List<ITileFactory> factories = Activator.getDefault()
				.getTileFactories();
		viewer.setInput(factories);
		viewer.setSelection(new StructuredSelection(factories.get(0)));

		return uiContainer;
	}

	/** {@inheritDoc} */
	public void init(final IWorkbench workbench) {
		// do nothing
	}
}
