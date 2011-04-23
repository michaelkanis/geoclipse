/*-----------------------------------------------------------------------+
 | net.skweez.geoclipse
 |                                                                       |
 | $Id$            
 +-----------------------------------------------------------------------*/
package net.skweez.geoclipse.map;

import org.eclipse.swt.graphics.GC;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev$
 * @levd.rating RED Hash:
 */
public abstract class Overlay {

	public void draw(GC gc, MapView mapView) {
		draw(gc, mapView, false);
	}

	public void draw(GC gc, MapView mapView, boolean shadowLayer) {
		// Draw nothing by default
	}

}
