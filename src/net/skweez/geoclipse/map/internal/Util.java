/* *****************************************************************************
 * de.byteholder.geoclipse.map
 * $Id: Util.java 657 2009-12-30 12:27:37Z damumbl $
 *
 * Copyright (C) 2009 mks and others
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
package net.skweez.geoclipse.map.internal;

import java.awt.Dimension;
import java.awt.Point;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

/**
 * 
 * @author mks
 * @author $Author: damumbl $
 * @version $Rev: 657 $
 * @levd.rating RED Rev:
 */
public abstract class Util {

	/**
	 * Checks if an image can be reused, this is true if the image exists and
	 * has the same size.
	 */
	public static boolean canReuseImage(final Image image, final Rectangle rect) {

		if (image == null || image.isDisposed()) {
			return false;
		}

		// image exist, check for the bounds
		final Rectangle oldBounds = image.getBounds();

		if (!(oldBounds.width == rect.width && oldBounds.height == rect.height)) {
			return false;
		}

		return true;
	}

	/**
	 * creates a new image
	 * 
	 * @param display
	 * @param image
	 *            image which will be disposed if the image is not null
	 * @param rect
	 * @return returns a new created image
	 */
	public static Image createImage(final Display display, final Image image,
			final Rectangle rect) {

		if (image != null && !image.isDisposed()) {
			image.dispose();
		}

		final int width = Math.max(1, rect.width);
		final int height = Math.max(1, rect.height);

		return new Image(display, width, height);
	}

	/** Get rid of a SWT resource, if it is not null. */
	public static void disposeResource(Resource resource) {
		if (resource != null && !resource.isDisposed()) {
			resource.dispose();
		}
	}

	/**
	 * Returns <code>true</code> if a tile with the given coordinates would be
	 * on the map or <code>false</code> otherwise.
	 */
	public static boolean isTileOnMap(Point position, Dimension mapSize) {
		return position.y >= 0 && position.y < mapSize.getHeight();
	}

}
