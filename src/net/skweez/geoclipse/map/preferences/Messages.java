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

import org.eclipse.osgi.util.NLS;

/**
 * Contains constants for accessing the NLS strings of this bundle.
 * 
 * @author Wolfgang Schramm
 * @author Michael Kanis
 */
/* package */abstract class Messages extends NLS {

	/** Name of this bundle. */
	private static final String BUNDLE_NAME = "net.skweez.geoclipse.map.preferences.messages"; //$NON-NLS-1$

	public static String pref_cache_clear_cache;
	public static String pref_cache_location;
	public static String pref_cache_max_cache_size;
	public static String pref_cache_max_cache_size_unit;
	public static String pref_cache_message_box_text;
	public static String pref_cache_message_box_title;
	public static String pref_cache_period_of_validity;
	public static String pref_cache_period_of_validity_tooltip;
	public static String pref_cache_period_of_validity_unit;
	public static String pref_cache_use_default_location;
	public static String pref_cache_use_offline;

	public static String pref_error_invalid_path;
	public static String pref_error_invalid_period_of_validity;

	public static String pref_map_factory_message;

	public static String pref_map_factory_title;

	public static String pref_map_lbl_avail_map_provider;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
