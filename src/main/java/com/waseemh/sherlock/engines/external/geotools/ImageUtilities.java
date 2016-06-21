package com.waseemh.sherlock.engines.external.geotools;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;


/**
 * A set of static methods working on images. Some of those methods are useful, but not
 * really rigorous. This is why they do not appear in any "official" package, but instead
 * in this private one.
 *
 *                      <strong>Do not rely on this API!</strong>
 *
 * It may change in incompatible way in any future version.
 *
 * @since 2.0
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions
 */
public class ImageUtilities {

	/**
	 * The default tile size. This default tile size can be
	 * overridden with a call to {@link JAI#setDefaultTileSize}.
	 */
	private static final Dimension GEOTOOLS_DEFAULT_TILE_SIZE = new Dimension(512,512);

	/**
	 * The minimum tile size.
	 */
	private static final int GEOTOOLS_MIN_TILE_SIZE = 256;

	/**
	 * Maximum tile width or height before to consider a tile as a stripe. It tile width or height
	 * are smaller or equals than this size, then the image will be retiled. That is done because
	 * there are many formats that use stripes as an alternative to tiles, an example is tiff. A
	 * stripe can be a performance black hole, users can have stripes as large as 20000 columns x 8
	 * rows. If we just want to see a chunk of 512x512, this is a lot of uneeded data to load.
	 */
	private static final int STRIPE_SIZE = 64;

	/**
	 * Suggests an {@link ImageLayout} for the specified image. All parameters are initially set
	 * equal to those of the given {@link RenderedImage}, and then the {@linkplain #toTileSize
	 * tile size is updated according the image size}. This method never returns {@code null}.
	 */
	public static ImageLayout getImageLayout(final RenderedImage image) {
		return getImageLayout(image, true);
	}

	/**
	 * Returns an {@link ImageLayout} for the specified image. If {@code initToImage} is
	 * {@code true}, then all parameters are initially set equal to those of the given
	 * {@link RenderedImage} and the returned layout is never {@code null} (except if
	 * the image is null).
	 */
	private static ImageLayout getImageLayout(final RenderedImage image, final boolean initToImage) {
		if (image == null) {
			return null;
		}
		ImageLayout layout = initToImage ? new ImageLayout(image) : null;
		if ((image.getNumXTiles()==1 || image.getTileWidth () <= STRIPE_SIZE) &&
				(image.getNumYTiles()==1 || image.getTileHeight() <= STRIPE_SIZE))
		{
			// If the image was already tiled, reuse the same tile size.
			// Otherwise, compute default tile size.  If a default tile
			// size can't be computed, it will be left unset.
			if (layout != null) {
				layout = layout.unsetTileLayout();
			}
			Dimension defaultSize = JAI.getDefaultTileSize();
			if (defaultSize == null) {
				defaultSize = GEOTOOLS_DEFAULT_TILE_SIZE;
			}
			int s;
			if ((s=toTileSize(image.getWidth(), defaultSize.width)) != 0) {
				if (layout == null) {
					layout = new ImageLayout();
				}
				layout = layout.setTileWidth(s);
				layout.setTileGridXOffset(image.getMinX());
			}
			if ((s=toTileSize(image.getHeight(), defaultSize.height)) != 0) {
				if (layout == null) {
					layout = new ImageLayout();
				}
				layout = layout.setTileHeight(s);
				layout.setTileGridYOffset(image.getMinY());
			}
		}
		return layout;
	}

	/**
	 * Suggests a set of {@link RenderingHints} for the specified image.
	 * The rendering hints may include the following parameters:
	 *
	 * <ul>
	 *   <li>{@link JAI#KEY_IMAGE_LAYOUT} with a proposed tile size.</li>
	 * </ul>
	 *
	 * This method may returns {@code null} if no rendering hints is proposed.
	 */
	public static RenderingHints getRenderingHints(final RenderedImage image) {
		final ImageLayout layout = getImageLayout(image, false);
		return (layout != null) ? new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout) : null;
	}
	/**
	 * Suggests a tile size for the specified image size. On input, {@code size} is the image's
	 * size. On output, it is the tile size. This method write the result directly in the supplied
	 * object and returns {@code size} for convenience.
	 * <p>
	 * This method it aimed to computing a tile size such that the tile grid would have overlapped
	 * the image bound in order to avoid having tiles crossing the image bounds and being therefore
	 * partially empty. This method will never returns a tile size smaller than
	 * {@value ImageUtilities#GEOTOOLS_MIN_TILE_SIZE}. If this method can't suggest a size,
	 * then it left the corresponding {@code size} field ({@link Dimension#width width} or
	 * {@link Dimension#height height}) unchanged.
	 * <p>
	 * The {@link Dimension#width width} and {@link Dimension#height height} fields are processed
	 * independently in the same way. The following discussion use the {@code width} field as an
	 * example.
	 * <p>
	 * This method inspects different tile sizes close to the {@linkplain JAI#getDefaultTileSize()
	 * default tile size}. Lets {@code width} be the default tile width. Values are tried in the
	 * following order: {@code width}, {@code width+1}, {@code width-1}, {@code width+2},
	 * {@code width-2}, {@code width+3}, {@code width-3}, <cite>etc.</cite> until one of the
	 * following happen:
	 * <p>
	 * <ul>
	 *   <li>A suitable tile size is found. More specifically, a size is found which is a dividor
	 *       of the specified image size, and is the closest one of the default tile size. The
	 *       {@link Dimension} field ({@code width} or {@code height}) is set to this value.</li>
	 *
	 *   <li>An arbitrary limit (both a minimum and a maximum tile size) is reached. In this case,
	 *       this method <strong>may</strong> set the {@link Dimension} field to a value that
	 *       maximize the remainder of <var>image size</var> / <var>tile size</var> (in other
	 *       words, the size that left as few empty pixels as possible).</li>
	 * </ul>
	 */
	public static Dimension toTileSize(final Dimension size) {
		Dimension defaultSize = JAI.getDefaultTileSize();
		if (defaultSize == null) {
			defaultSize = GEOTOOLS_DEFAULT_TILE_SIZE;
		}
		int s;
		if ((s=toTileSize(size.width,  defaultSize.width )) != 0) size.width  = s;
		if ((s=toTileSize(size.height, defaultSize.height)) != 0) size.height = s;
		return size;
	}

	/**
	 * Suggests a tile size close to {@code tileSize} for the specified {@code imageSize}.
	 * This method it aimed to computing a tile size such that the tile grid would have
	 * overlapped the image bound in order to avoid having tiles crossing the image bounds
	 * and being therefore partially empty. This method will never returns a tile size smaller
	 * than {@value #GEOTOOLS_MIN_TILE_SIZE}. If this method can't suggest a size, then it returns 0.
	 *
	 * @param imageSize The image size.
	 * @param tileSize  The preferred tile size, which is often {@value #GEOTOOLS_DEFAULT_TILE_SIZE}.
	 */
	private static int toTileSize(final int imageSize, final int tileSize) {
		final int MAX_TILE_SIZE = Math.min(tileSize*2, imageSize);
		final int stop = Math.max(tileSize-GEOTOOLS_MIN_TILE_SIZE, MAX_TILE_SIZE-tileSize);
		int sopt = 0;  // An "optimal" tile size, to be used if no exact dividor is found.
		int rmax = 0;  // The remainder of 'imageSize / sopt'. We will try to maximize this value.
		/*
		 * Inspects all tile sizes in the range [GEOTOOLS_MIN_TILE_SIZE .. MAX_TIME_SIZE]. We will begin
		 * with a tile size equals to the specified 'tileSize'. Next we will try tile sizes of
		 * 'tileSize+1', 'tileSize-1', 'tileSize+2', 'tileSize-2', 'tileSize+3', 'tileSize-3',
		 * etc. until a tile size if found suitable.
		 *
		 * More generally, the loop below tests the 'tileSize+i' and 'tileSize-i' values. The
		 * 'stop' constant was computed assuming that MIN_TIME_SIZE < tileSize < MAX_TILE_SIZE.
		 * If a tile size is found which is a dividor of the image size, than that tile size (the
		 * closest one to 'tileSize') is returned. Otherwise, the loop continue until all values
		 * in the range [GEOTOOLS_MIN_TILE_SIZE .. MAX_TIME_SIZE] were tested. In this process, we remind
		 * the tile size that gave the greatest reminder (rmax). In other words, this is the tile
		 * size with the smallest amount of empty pixels.
		 */
		for (int i=0; i<=stop; i++) {
			int s;
			if ((s = tileSize+i) <= MAX_TILE_SIZE) {
				final int r = imageSize % s;
				if (r == 0) {
					// Found a size >= to 'tileSize' which is a dividor of image size.
					return s;
				}
				if (r > rmax) {
					rmax = r;
					sopt = s;
				}
			}
			if ((s = tileSize-i) >= GEOTOOLS_MIN_TILE_SIZE) {
				final int r = imageSize % s;
				if (r == 0) {
					// Found a size <= to 'tileSize' which is a dividor of image size.
					return s;
				}
				if (r > rmax) {
					rmax = r;
					sopt = s;
				}
			}
		}
		/*
		 * No dividor were found in the range [GEOTOOLS_MIN_TILE_SIZE .. MAX_TIME_SIZE]. At this point
		 * 'sopt' is an "optimal" tile size (the one that left as few empty pixel as possible),
		 * and 'rmax' is the amount of non-empty pixels using this tile size. We will use this
		 * "optimal" tile size only if it fill at least 75% of the tile. Otherwise, we arbitrarily
		 * consider that it doesn't worth to use a "non-standard" tile size. The purpose of this
		 * arbitrary test is again to avoid too many small tiles (assuming that
		 */
		return (rmax >= tileSize - tileSize/4) ? sopt : 0;
	}
}