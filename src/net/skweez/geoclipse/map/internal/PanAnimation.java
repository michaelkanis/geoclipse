package net.skweez.geoclipse.map.internal;

import java.util.TimerTask;

import net.skweez.geoclipse.map.GeoPoint;
import net.skweez.geoclipse.map.MapController;

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