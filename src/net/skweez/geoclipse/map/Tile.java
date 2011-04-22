/* *****************************************************************************
 * $Id: Tile.java 657 2009-12-30 12:27:37Z damumbl $
 * de.byteholder.geoclipse.map
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
package net.skweez.geoclipse.map;

import java.util.Observable;

import net.skweez.geoclipse.Activator;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import edu.tum.cs.commons.assertion.CCSMPre;

/**
 * The Tile class represents a particular square image piece of the world bitmap
 * at a particular zoom level.
 * 
 * @author Joshua Marinacci
 * @author Michael Kanis
 * @author Wolfgang Schramm
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev: 475
 */

public class Tile extends Observable {

	/** The tile's status. Whenever it changes, the observers get notified. */
	private ETileStatus status = ETileStatus.NEW;

	/** If an error occurs while loading a tile, store the exception here. */
	private Throwable error;

	/** The zoom level. */
	private final int zoom;

	/** The x coordinate. */
	private final int x;

	/** The y coordinate. */
	private final int y;

	/** Map image for this tile. */
	private Image image;

	/** Create a new Tile at the specified tile point and zoom level. */
	public Tile(final int x, final int y, final int zoom) {
		this.zoom = zoom;
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the Throwable tied to any error that may have ocurred while
	 * loading the tile. This error may change several times if multiple errors
	 * occur.
	 */
	public Throwable getError() {
		return error;
	}

	/**
	 * Returns the map image for this tile or <code>null</code> if the image is
	 * not available or invalid.
	 */
	public Image getImage() {
		return image;
	}

	/** Returns the tile position for the x-axis. */
	public int getX() {
		return x;
	}

	/** Returns the tile position for the y-axis. */
	public int getY() {
		return y;
	}

	/** Returns the zoom level that this tile belongs to. */
	public int getZoom() {
		return zoom;
	}

	/** Returns status. */
	public ETileStatus getStatus() {
		return status;
	}

	/** Sets the status of this tile. */
	public void setStatus(ETileStatus status) {
		this.status = status;
		setChanged();
		notifyObservers();
	}

	/** Sets an error, e.g. if one occured during loading etc.. */
	public void setError(Throwable error) {
		this.error = error;
		setStatus(ETileStatus.ERROR);
	}

	/** Set the map image for this tile. */
	@SuppressWarnings("null")
	public void setImage(final Image newImage) {
		CCSMPre.isFalse(newImage == null, "Image may not be null.");
		CCSMPre.isFalse(newImage.isDisposed(),
				"Image may not already be disposed.");

		if (image != null && !image.isDisposed()) {
			image.dispose();
		}

		image = newImage;
		setStatus(ETileStatus.READY);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "X: " + x + "  Y: " + y + " Zoom: " + zoom + " Status: "
				+ status;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tile) {
			final Tile other = (Tile) obj;
			return x == other.x && y == other.y && zoom == other.zoom;
		}

		return false;
	}

	public void draw(GC gc) {
		switch (getStatus()) {
		case READY:
			gc.drawImage(getImage(), 0, 0);
			break;
		case ERROR:
			gc.drawImage(
					Activator.getDefault().getImageRegistry()
							.get(Constants.ERROR_IMG_KEY), 0, 0);
			break;
		case LOADING:
			gc.drawImage(
					Activator.getDefault().getImageRegistry()
							.get(Constants.LOADING_IMG_KEY), 0, 0);
			break;
		}
	}
}
