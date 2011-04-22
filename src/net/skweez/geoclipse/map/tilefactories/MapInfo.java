/*
 * TileFactoryInfo.java
 *
 * Created on June 26, 2006, 10:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.skweez.geoclipse.map.tilefactories;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.net.URL;

import net.skweez.geoclipse.map.Tile;


/**
 * A MapInfo encapsulates all information specific to a map server. This
 * includes everything from the url to load the map tiles from to the size and
 * depth of the tiles. Theoretically any map server can be used by installing a
 * customized TileFactoryInfo.
 * 
 * @author Joshua Marinacci
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating RED Rev:
 */
public abstract class MapInfo {

	private final int minimumZoomLevel;

	private final int maximumZoomLevel;

	/** The size of each tile (assumes they are square). */
	private final int tileSize;

	/** Base url for loading tiles. */
	private final String baseURL;

	private int defaultZoomLevel;

	/**
	 * Creates a new instance of MapInfo. Note that MapInfo should be considered
	 * invariate, meaning that subclasses should ensure all of the properties
	 * stay the same after the class is constructed. Returning different values
	 * of getTileSize() for example is considered an error and may result in
	 * unexpected behavior.
	 * 
	 * @param baseURL
	 *            the base url for grabbing tiles
	 */
	public MapInfo(final int minimumZoomLevel, final int maximumZoomLevel,
			final int tileSize, final String baseURL) {

		this.minimumZoomLevel = minimumZoomLevel;
		this.maximumZoomLevel = maximumZoomLevel;
		this.baseURL = baseURL;
		this.tileSize = tileSize;
	}

	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * Returns a {@link Point2D} that points to the center of the map at the
	 * given zoom.
	 */
	private Point2D getMapCenterInPixelsAtZoom(final int zoom) {
		Dimension dimension = getMapSize(zoom);
		dimension.height *= getTileSize();
		dimension.width *= getTileSize();
		return new Point2D.Double(dimension.getWidth() / 2d, dimension
				.getHeight() / 2d);
	}

	/**
	 * Returns the size of the map at the given zoom, in tiles (num tiles tall
	 * by num tiles wide).
	 */
	public Dimension getMapSize(final int zoom) {
		return new Dimension(getMapWidthInTilesAtZoom(zoom),
				getMapWidthInTilesAtZoom(zoom));
	}

	/** Returns the number of tiles the map is wide at the given zoom level. */
	public int getMapWidthInTilesAtZoom(int zoom) {
		return (int) Math.pow(2, zoom);
	}

	public int getMaximumZoom() {
		return maximumZoomLevel;
	}

	public int getMinimumZoom() {
		return minimumZoomLevel;
	}

	/** Returns the tile size. */
	public int getTileSize() {
		return tileSize;
	}

	/**
	 * Returns the tile url for the specified tile at the specified zoom level.
	 * By default it will generate a tile url using the base url and parameters
	 * 
	 * Note that the URL can be a <code>file:</code> url.
	 */
	public abstract URL getTileUrl(final Tile tile);

	/**
	 * Returns the file extension for the tile image files. E.g. "jpg" or "png".
	 */
	public abstract String getFileExtension();
}
