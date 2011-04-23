package net.skweez.geoclipse.map.tilefactories.internal;


/**
 * The OpenStreetMap bicycle map.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating YELLOW Rev: 483
 */
public class OsmCycleFactory extends DefaultTileFactory {

	private static final String NAME = "OSM Cycle map";

	private static final String URL = "http://andy.sandbox.cloudmade.com/tiles/cycle";

	/** Constructor. */
	public OsmCycleFactory() {
		super(new OpenStreetMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}
