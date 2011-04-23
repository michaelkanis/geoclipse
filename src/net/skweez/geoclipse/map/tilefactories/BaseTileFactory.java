package net.skweez.geoclipse.map.tilefactories;

import java.awt.Dimension;

import net.skweez.geoclipse.map.Projection;

/**
 * A base class for tile factories. You can extend this class instead of
 * implementing {@link ITileFactory} directly.
 * 
 * @author Joshua Marinacci
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 561 $
 * @levd.rating YELLOW Rev: 561
 */
public abstract class BaseTileFactory implements ITileFactory {

	/** The projection that is used by this tile factory. */
	private final Projection projection;

	/** Constructor. */
	public BaseTileFactory(Projection projection) {
		this.projection = projection;
		projection.setTileFactory(this);
	}

	/**
	 * Dispose of all of the images and other SWT
	 * {@link org.eclipse.swt.graphics.Resource}s.
	 * 
	 * This should be called when a tile factory is no longer used.
	 */
	public void dispose() {
		// do nothing by default
	}

	/** {@inheritDoc} */
	public Projection getProjection() {
		return projection;
	}

	/** {@inheritDoc} */
	public String getName() {
		return getClass().getSimpleName();
	}

	/** {@inheritDoc} */
	@Override
	public Dimension getMapSizeInPixels(int zoom) {
		return new Dimension(getMapSize(zoom).width * getTileSize(),
				getMapSize(zoom).height * getTileSize());
	}
}
