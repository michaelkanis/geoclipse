/*-----------------------------------------------------------------------+
 | net.skweez.geoclipse
 |                                                                       |
 | $Id$            
 +-----------------------------------------------------------------------*/
package net.skweez.geoclipse.map.internal;

import java.util.TimerTask;

import net.skweez.geoclipse.map.GeoPoint;
import net.skweez.geoclipse.map.MapController;

public class PanAnimation extends TimerTask {

	private static final int DURATION = 250;

	private long startTime = 0;

	private final GeoPoint start;

	private final GeoPoint end;

	private final MapController controller;

	public PanAnimation(GeoPoint start, GeoPoint end, MapController controller) {

		this.start = start;
		this.end = end;
		this.controller = controller;
		startTime = System.nanoTime() / 1000000;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {

		long currentTime = System.nanoTime() / 1000000;
		long totalTime = currentTime - startTime;

		if (totalTime > DURATION) {
			cancel();
		}

		float fraction = Math.min(1.0f, (float) totalTime / DURATION);

		// x = x0 + f * (x1 - x0)
		double lat0 = start.getLatitude();
		double lat1 = end.getLatitude();
		double latitude = lat0 + fraction * (lat1 - lat0);

		double lon0 = start.getLongitude();
		double lon1 = end.getLongitude();
		double longitude = lon0 + fraction * (lon1 - lon0);

		controller.setCenter(new GeoPoint(latitude, longitude));
	}
}