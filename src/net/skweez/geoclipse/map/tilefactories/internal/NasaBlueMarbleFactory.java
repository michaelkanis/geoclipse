package net.skweez.geoclipse.map.tilefactories.internal;

import net.skweez.geoclipse.projections.EquirectangularProjection;

/**
 * This tile factory loads Blue Marble NG tiles from the NASA JPL servers.
 * 
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 561 $
 * @levd.rating YELLOW Rev: 561
 */
public class NasaBlueMarbleFactory extends DefaultTileFactory {

	/**
	 * The base URL where the tiles are loaded from.
	 * <p>
	 * <b> Do not change this URL pattern! The tiles it requests are
	 * pre-rendered. Requesting non pre-rendered tiles in client apps from WMS
	 * servers is very expensive in the means of server resources and can lead
	 * to banning from the server.</b>
	 */
	private static final String URL = "http://wms.jpl.nasa.gov/wms.cgi?request="
			+ "GetMap&layers=BMNG&srs=EPSG:4326&format=image/jpeg&styles=May"
			+ "&width=480&height=480&bbox=";

	/** The human readable name of the tile factory. */
	private static final String NAME = "NASA Blue Marble Next Generation";

	/** Constructor. */
	public NasaBlueMarbleFactory() {
		super(new EquirectangularProjection(), new WmsMapInfo(URL));
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return NAME;
	}
}
