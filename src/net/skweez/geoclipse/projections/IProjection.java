/* *****************************************************************************
 *  Copyright (C) 2008-2009 Michael Kanis and others
 *  $Id: IProjection.java 649 2009-12-29 10:22:00Z damumbl $
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

package net.skweez.geoclipse.projections;

import java.awt.Point;

import net.skweez.geoclipse.model.GeoPoint;

/**
 * A projection is a method to represent the surface of the three dimensional
 * earth to a two dimensional map. Projections always distort the surface in one
 * way or another.
 *<p>
 * There are a number of characteristics for preserving metric properties of
 * projections. Among them are:
 * <li><i>Conformal</i> - preserving angles
 * <li><i>Equal-area</i> - preserving area ratios
 * <li><i>Equidistant</i> - preserving distance ratios
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 649 $
 * @levd.rating YELLOW Rev: 503
 */
public interface IProjection {

	/**
	 * Given a position (latitude/longitude pair) and a zoom level, returns the
	 * appropriate point in <em>pixels</em>. The zoom level is necessary because
	 * pixel coordinates are in terms of the zoom level.
	 */
	public abstract Point geoToPixel(GeoPoint c, int width, int height);

	/** Converts an on screen pixel coordinate to a geo position. */
	public abstract GeoPoint pixelToGeo(int pixelX, int pixelY, int width,
			int height);

}
