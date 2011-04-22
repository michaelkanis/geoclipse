package net.skweez.geoclipse.map.tilefactories.internal;

import net.skweez.geoclipse.projections.MercatorProjection;

/**
 * A tile factory to load tiles from the OpenStreetBrowser server. These are
 * limited to Europe atm.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating YELLOW Rev: 483
 */
public class OpenStreetBrowserFactory extends DefaultTileFactory {

	private static final String NAME = "OpenStreetBrowser (Europe only)";

	private static final String URL = "http://www.openstreetbrowser.org/tiles/base";

	/** Constructor. */
	public OpenStreetBrowserFactory() {
		super(new MercatorProjection(), new OpenStreetMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}
