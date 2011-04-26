package net.skweez.geoclipse.map.tilefactories.internal;


/**
 * The OpenStreetMap Mapnik rendered map.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating YELLOW Rev: 483
 */
public class OsmMapnikFactory extends DefaultTileFactory {

	private static final String NAME = "OSM Mapnik";

	private static final String URL = "http://tile.openstreetmap.org";

	/** Constructor. */
	public OsmMapnikFactory() {
		super(new OpenStreetMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}