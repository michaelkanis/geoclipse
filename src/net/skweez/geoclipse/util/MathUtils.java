/* *****************************************************************************
 * de.byteholder.geoclipse.core
 * $Id: MathUtils.java 504 2009-04-11 08:37:08Z damumbl $
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
package net.skweez.geoclipse.util;

/**
 * Mathematical functions that are not in {@link java.lang.Math}.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 504 $
 * @levd.rating YELLOW Rev: 504
 */
public abstract class MathUtils {

	/**
	 * Calculates the hyperbolic area sine. This is the reverse function of
	 * {@link Math#sinh(double)}.
	 */
	public static double asinh(double x) {
		return Math.log(x + Math.sqrt(Math.pow(x, 2) + 1));
	}
}
