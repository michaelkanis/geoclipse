/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: MapImageCache.java 558 2009-06-27 08:24:48Z damumbl $
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
package net.skweez.geoclipse.map.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Image;

/**
 * This cache manages map images by saving the images for the offline mode.
 * 
 * @author Joshua Marinacci
 * @author Michael Kanis
 * @author Wolfgang Schramm
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating RED Rev:
 */
public abstract class MapImageCache {

	/** OS path for storing offline map image files. */
	public static final String TILE_OFFLINE_CACHE_OS_PATH = "offline-map"; //$NON-NLS-1$

	/** Max. number of images in the image cache. */
	private static int MAX_CACHE_ENTRIES = 128;

	private final Map<String, Image> imageCache = new HashMap<String, Image>();

	private final Map<String, Tile> tileCache = new HashMap<String, Tile>();

	private final Queue<String> imageCacheFifo = new ArrayBlockingQueue<String>(
			MAX_CACHE_ENTRIES, true);

	/** Dispose all cached images and clear the cache. */
	public synchronized void dispose() {

		final Collection<Image> images = imageCache.values();
		for (final Image image : images) {
			if (image != null && !image.isDisposed()) {
				image.dispose();
			}
		}

		imageCache.clear();
		imageCacheFifo.clear();
	}

	/** Used to store the tile image. */
	protected abstract IPath getCachePath(int x, int y, int zoom);

	/** Actually creates a tile. (Including starting to load it from anywhere) */
	protected abstract Tile createTile(int x, int y, int zoom);

	public synchronized Tile obtainTile(final int x, final int y, final int zoom) {
		Tile tile = tileCache.get(getTileKey(x, y, zoom));
		if (tile == null) {
			tile = createTile(x, y, zoom);
			put(tile);
		}

		return tile;
	}

	private void put(Tile tile) {
		final String key = getTileKey(tile.getX(), tile.getY(), tile.getZoom());

		if (imageCacheFifo.size() >= MAX_CACHE_ENTRIES) {

			// remove and dispose oldest image
			final Tile oldestTile = tileCache.remove(imageCacheFifo.poll());

			if (oldestTile != null && oldestTile.getImage() != null) {
				oldestTile.getImage().dispose();
			}
		}

		tileCache.put(key, tile);
		imageCacheFifo.add(key);

		Assert.isTrue(tileCache.size() <= MAX_CACHE_ENTRIES,
				"Tile cache is too big.");
		Assert.isTrue(tileCache.size() == imageCacheFifo.size(),
				"Tile cache and image age queue are out of sync.");
	}

	private String getTileKey(final int x, final int y, final int zoom) {
		return getCachePath(x, y, zoom).toString();
	}
}
