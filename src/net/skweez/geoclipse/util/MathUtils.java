/*
 * Copyright (C) 2009-2011 Michael Kanis and others
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
// @ConQAT.Rating YELLOW Hash: 7CC255D772148058C60D3C1740C0404D
package net.skweez.geoclipse.util;

/**
 * Mathematical functions that are not in {@link java.lang.Math}.
 * 
 * @author Michael Kanis
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
