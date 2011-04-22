package net.skweez.geoclipse.map.tilefactories.internal;

import net.skweez.geoclipse.projections.MercatorProjection;

/**
 * A tile factory to load tiles from the OpenStreetMap Tiles@home servers. These
 * are rendered using the Osmarender program.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 558 $
 * @levd.rating YELLOW Rev: 483
 */
public class OsmTilesAtHomeFactory extends DefaultTileFactory {

	private static final String NAME = "OSM Osmarender";

	private static final String URL = "http://tah.openstreetmap.org/Tiles/tile";

	/** Constructor. */
	public OsmTilesAtHomeFactory() {
		super(new MercatorProjection(), new OpenStreetMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}
