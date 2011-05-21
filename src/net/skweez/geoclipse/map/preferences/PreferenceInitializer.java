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

import net.skweez.geoclipse.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initialize preferences for the mapping plugin.
 * 
 * @author Wolfgang Schramm
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(IMappingPreferences.OFFLINE_CACHE_USE_OFFLINE, false);
		store.setDefault(
				IMappingPreferences.OFFLINE_CACHE_USE_DEFAULT_LOCATION, true);

		store.setDefault(IMappingPreferences.OFFLINE_CACHE_PERIOD_OF_VALIDITY,
				7);
		store.setDefault(IMappingPreferences.OFFLINE_CACHE_MAX_SIZE, 100);

	}
}
