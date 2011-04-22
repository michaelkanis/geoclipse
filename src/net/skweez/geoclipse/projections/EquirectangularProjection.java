/* *****************************************************************************
 * Copyright (C) 2008-2009 Michael Kanis and others
 * $Id: EquirectangularProjection.java 649 2009-12-29 10:22:00Z damumbl $
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

import java.awt.Point;

import net.skweez.geoclipse.model.GeoPoint;

/**
 * This is an equirectangular cylindrical projection. This means that any pair
 * of neighbour longitudes and any pair of neighbour latitudes always have the
 * same distance on the projected map. The projection is neither equal area nor
 * conformal, but equidistant.
 * <p>
 * It is often used in digital maps because you can directly map geographical
 * positions to pixels and vice versa. E.g. NASA uses this projection for their
 * Blue Marble Next Genereation (BMNG) maps.
 * <p>
 * See http://en.wikipedia.org/wiki/Equirectangular_projection for a more
 * detailed description.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 649 $
 * @levd.rating YELLOW Rev: 503
 */
public class EquirectangularProjection implements IProjection {

	/** {@inheritDoc} */
	@Override
	public Point geoToPixel(GeoPoint c, int width, int height) {
		return new Point((int) lonToX(c.getLongitude(), width), (int) latToY(c
				.getLatitude(), height));
	}

	/** Converts a given longitude to a x coordinate using a given map width. */
	protected double lonToX(double longitude, int mapWidth) {
		final double pixelsPerLon = mapWidth / 360d;
		return mapWidth / 2 + longitude * pixelsPerLon;
	}

	/** Converts a given latitude to a y coordinate using a given map height. */
	protected double latToY(double latitude, int mapHeight) {
		final double pixelsPerLat = mapHeight / 180d;
		return mapHeight / 2 - latitude * pixelsPerLat;
	}

	/** {@inheritDoc} */
	public GeoPoint pixelToGeo(int pixelX, int pixelY, int width, int height) {
		return new GeoPoint(yToLat(pixelY, height), xToLon(pixelX, width));
	}

	/** Converts a given x coordinate to a longitude using a given map width. */
	protected double xToLon(double x, int mapWidth) {
		final double pixelsPerLon = mapWidth / 360d;
		return (x - mapWidth / 2) / pixelsPerLon;
	}

	/** Converts a given y coordinate to a latitude using a given map height. */
	protected double yToLat(double y, int mapHeight) {
		final double pixelsPerLat = mapHeight / 180d;
		return (mapHeight / 2 - y) / pixelsPerLat;
	}
}
