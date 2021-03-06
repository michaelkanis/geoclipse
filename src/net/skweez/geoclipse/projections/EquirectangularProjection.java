/*
 * Copyright (C) 2008-2011 Michael Kanis and others
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
 */
package net.skweez.geoclipse.projections;

import net.skweez.geoclipse.map.GeoPoint;
import net.skweez.geoclipse.map.MapView;
import net.skweez.geoclipse.map.Projection;

import org.eclipse.swt.graphics.Point;

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
 */
public class EquirectangularProjection extends Projection {

	private final MapView mapView;

	public EquirectangularProjection(MapView mapView) {
		this.mapView = mapView;
	}

	/** {@inheritDoc} */
	@Override
	public Point geoToPixel(GeoPoint c) {
		int mapSize = mapView.getMapSizeInPixels();
		return new Point((int) lonToX(c.getLongitude(), mapSize), (int) latToY(
				c.getLatitude(), mapSize));
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
	@Override
	public GeoPoint pixelToGeo(int pixelX, int pixelY) {
		int mapSize = mapView.getMapSizeInPixels();
		return new GeoPoint(yToLat(pixelY, mapSize), xToLon(pixelX, mapSize));
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
