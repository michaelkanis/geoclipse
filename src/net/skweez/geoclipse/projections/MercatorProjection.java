/* *****************************************************************************
 * Copyright (C) 2009 Michael Kanis and others
 * $Id: MercatorProjection.java 503 2009-04-11 08:36:01Z damumbl $
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

package net.skweez.geoclipse.projections;

import net.skweez.geoclipse.util.MathUtils;

/**
 * The Mercartor projection is a cylindrical projection, but differing from the
 * {@link EquirectangularProjection} it is conformal, but not equidistant and
 * not equal area. The poles can not be depicted with this projection, because
 * the scale increases from the equator and becomes infinite at the poles.
 * <p>
 * Google and OpenStreetMap use this projection for their maps. It is also
 * widely used in nautical applications, because of it's character to preserve
 * angles.
 * <p>
 * Please see http://en.wikipedia.org/wiki/Mercator_projection for a detailed
 * description.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 503 $
 * @levd.rating YELLOW Rev: 503
 */
public final class MercatorProjection extends EquirectangularProjection {

	/** {@inheritDoc} */
	@Override
	protected double latToY(double latitude, int mapHeight) {
		// (mkanis) I don't know, why this must be 360°, as the projected height
		// should really be 180°. But with 180° the result is twice as big as it
		// should be.
		final double pixelsPerLat = mapHeight / 360d;
		double y = MathUtils.asinh(Math.tan(Math.toRadians(latitude)));
		return mapHeight / 2 - Math.toDegrees(y) * pixelsPerLat;
	}

	/** {@inheritDoc} */
	@Override
	protected double yToLat(double y, int mapHeight) {
		final double pixelsPerLat = mapHeight / 360d;
		double latitude = Math.toRadians((mapHeight / 2 - y) / pixelsPerLat);
		return Math.toDegrees(Math.asin(Math.tanh(latitude)));
	}
}
