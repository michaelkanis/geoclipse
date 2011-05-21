/*
 *  Copyright (C) 2008-2011 Michael Kanis and others
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
 */
package net.skweez.geoclipse.map.internal;

import java.util.TimerTask;

import net.skweez.geoclipse.map.GeoPoint;
import net.skweez.geoclipse.map.MapController;

/**
 * @author "Michael Kanis"
 */
public class PanAnimation extends TimerTask {

	private final int duration;

	private final long startTime;

	private final GeoPoint start;

	private final GeoPoint end;

	private final MapController controller;

	public PanAnimation(GeoPoint start, GeoPoint end, MapController controller,
			int duration) {

		this.start = start;
		this.end = end;
		this.controller = controller;
		this.duration = duration;
		startTime = System.nanoTime() / 1000000;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {

		long currentTime = System.nanoTime() / 1000000;
		long totalTime = currentTime - startTime;

		if (totalTime > duration) {
			cancel();
		}

		double fraction = Math.min(1.0f, (float) totalTime / duration);

		// Use non-linear accelartion
		fraction = Math.sqrt(fraction);

		double latitude = linearInterpolation(start.getLatitude(),
				end.getLatitude(), fraction);

		double longitude = linearInterpolation(start.getLongitude(),
				end.getLongitude(), fraction);

		controller.setCenter(new GeoPoint(latitude, longitude));
	}

	// x = x0 + f * (x1 - x0)
	private double linearInterpolation(double x0, double x1, double f) {
		return x0 + f * (x1 - x0);
	}
}