/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: CachedTileFactoryBase.java 561 2009-06-27 14:16:16Z damumbl $
 *
 * Copyright (C) 2009 Michael Kanis and others
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
package net.skweez.geoclipse.map.tilefactories;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import net.skweez.geoclipse.map.ETileStatus;
import net.skweez.geoclipse.map.MapImageCache;
import net.skweez.geoclipse.map.Tile;
import net.skweez.geoclipse.projections.IProjection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;


/**
 * TODO class comment
 * 
 * @author Joshua Marinacci
 * @author Michael Kanis
 * @author Wolfgang Schramm
 * @author Alfred Barten
 * @author $Author: damumbl $
 * @version $Rev: 561 $
 * @levd.rating RED Rev:
 */
public abstract class CachedTileFactoryBase extends TileFactoryBase {

	/** The number of threads to start for downloading. */
	private final static int THREAD_POOL_SIZE = 20;

	private ExecutorService service;

	/** The associated MapInfo. */
	protected final MapInfo mapInfo;

	/** Holds the tiles which are currently being loaded. */
	private final HashMap<URL, Tile> loadingTiles = new HashMap<URL, Tile>();

	/** Thread pool for loading the tiles. */
	private final BlockingQueue<Tile> tileQueue = new LinkedBlockingQueue<Tile>();

	/** Cache for tile images. */
	private final MapImageCache mapImageCache;

	/**
	 * An inner class which actually loads the tiles. Used by the thread queue.
	 * Subclasses can override this if necessary.
	 */
	protected class TileRunner implements Runnable {

		/** {@inheritDoc} */
		public void run() {
			final Tile tile = tileQueue.remove();

			final URL url = mapInfo.getTileUrl(tile);
			try {
				final InputStream stream = url.openStream();
				final ImageData[] data = new ImageLoader().load(stream);
				final Image image = new Image(Display.getDefault(), data[0]);

				tile.setImage(image);
			} catch (final Exception e) {
				tile.setError(e);
			} finally {
				loadingTiles.remove(url);
			}
		}
	}

	/**
	 * Creates a new DefaultTileFactory using the specified {@link MapInfo} and
	 * {@link IProjection}.
	 */
	public CachedTileFactoryBase(final IProjection projection,
			final MapInfo info) {
		super(projection);
		mapInfo = info;
		mapImageCache = new MapImageCache() {

			@Override
			public IPath getCachePath(int x, int y, int zoom) {
				return new Path(getName()).append(Integer.toString(zoom))
						.append(Integer.toString(x))
						.append(Integer.toString(y)).addFileExtension(
								mapInfo.getFileExtension());
			}

			@Override
			public Tile createTile(int x, int y, int zoom) {
				return CachedTileFactoryBase.this.createTile(x, y, zoom);
			}
		};
	}

	/** {@inheritDoc} */
	@Override
	public void dispose() {
		loadingTiles.clear();
		mapImageCache.dispose();
	}

	private synchronized ExecutorService getExecutor() {

		if (service != null) {
			return service;
		}

		// create thread pool
		service = Executors.newFixedThreadPool(THREAD_POOL_SIZE,
				new ThreadFactory() {

					private int count = 0;

					public Thread newThread(final Runnable r) {
						final Thread t = new Thread(r, "tile-pool-" + count++);
						t.setPriority(Thread.MIN_PRIORITY);
						t.setDaemon(true);
						return t;
					}
				});
		return service;
	}

	/** {@inheritDoc} */
	@Override
	public Tile getTile(int x, final int y, final int zoom) {

		// wrap the tiles horizontally --> mod the x with the max width
		x = x % getMapSize(zoom).width;
		if (x < 0) {
			x += getMapSize(zoom).width;
		}

		return mapImageCache.obtainTile(x, y, zoom);
	}

	/** Creates and returns a tile object from the given coordinates. */
	private Tile createTile(int x, final int y, final int zoom) {
		final Tile tile = new Tile(x, y, zoom);
		final URL url = mapInfo.getTileUrl(tile);

		// check if tile is already being loaded
		if (loadingTiles.containsKey(url)) {
			return loadingTiles.get(url);
		}

		loadTileImage(tile);
		return tile;
	}

	private void loadTileImage(final Tile tile) {
		if (tile.getStatus() == ETileStatus.LOADING) {
			return;
		}

		try {
			tile.setStatus(ETileStatus.LOADING);
			loadingTiles.put(mapInfo.getTileUrl(tile), tile);
			tileQueue.put(tile);
			getExecutor().submit(new TileRunner());
		} catch (final Exception exception) {
			tile.setError(exception);
			exception.printStackTrace();
		}
	}
}
