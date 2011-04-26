package net.skweez.geoclipse.map;

import org.eclipse.swt.graphics.GC;

/**
 * Base class for overlays that may be drawn on top of the map. To add an
 * overlay, use the extension point <code>net.skweez.geoclipse.overlays</code>
 * and subclass this class.
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev$
 * @levd.rating RED Hash:
 */
public abstract class Overlay {

	/** The extension point ID for map overlays. */
	public static final String EXTENSION_POINT = "net.skweez.geoclipse.overlays";

	/** Draw the overlay over the map. */
	public void draw(GC gc, MapView mapView) {
		draw(gc, mapView, false);
	}

	/**
	 * Draw the overlay over the map. This will be called on all active overlays
	 * with shadow=true, to lay down the shadow layer, and then again on all
	 * overlays with shadow=false. By default, draws nothing.
	 * 
	 * This is private for now, because shadow layers are not yet supported.
	 */
	@SuppressWarnings("unused")
	private void draw(GC gc, MapView mapView, boolean shadowLayer) {
		// Draw nothing by default
	}

}
