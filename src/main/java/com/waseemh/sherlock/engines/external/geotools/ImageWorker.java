package com.waseemh.sherlock.engines.external.geotools;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PackedColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.ROI;
import javax.media.jai.TileCache;
import javax.media.jai.operator.ColorConvertDescriptor;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.LookupDescriptor;

/**
 * Helper methods for applying JAI operations on an image. The image is specified at {@linkplain #ImageWorker(RenderedImage) creation time}. Sucessive
 * operations can be applied by invoking the methods defined in this class, and the final image can be obtained by invoking {@link #getRenderedImage}
 * at the end of the process.
 * <p>
 * If an exception is thrown during a method invocation, then this {@code ImageWorker} is left in an undetermined state and should not be used
 * anymore.
 * 
 * @since 2.3
 * 
 * 
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 * @author Bryce Nordgren
 * @author Martin Desruisseaux
 */
public class ImageWorker {

	/**
	 * The image specified by the user at construction time, or last time {@link #invalidateStatistics} were invoked. The {@link #getComputedProperty}
	 * method will not search a property pass this point.
	 */
	private RenderedImage inheritanceStopPoint;

	/**
	 * The image being built.
	 */
	protected RenderedImage image;

	/**
	 * The region of interest, or {@code null} if none.
	 */
	private ROI roi;

	/**
	 * The rendering hints to provides to all image operators. Additional hints may be set (in a separated {@link RenderingHints} object) for
	 * particular images.
	 */
	private RenderingHints commonHints;

	/**
	 * If {@link Boolean#FALSE FALSE}, image operators are not allowed to produce tiled images. The default is {@link Boolean#TRUE TRUE}. The
	 * {@code FALSE} value is sometime useful for exporting images to some formats that doesn't support tiling (e.g. GIF).
	 * 
	 * @see #setRenderingHint
	 */
	public static final Key TILING_ALLOWED = new Key(Boolean.class);

	/**
	 * 0 if tile cache is enabled, any other value otherwise. This counter is incremented everytime {@code tileCacheEnabled(false)} is invoked, and
	 * decremented every time {@code tileCacheEnabled(true)} is invoked.
	 */
	private int tileCacheDisabled = 0;

	/**
	 * Creates a new builder for the specified image. The images to be computed (if any) will save their tiles in the default {@linkplain TileCache
	 * tile cache}.
	 * 
	 * @param image The source image.
	 */
	public ImageWorker(final RenderedImage image) {
		inheritanceStopPoint = this.image = image;
	}

	/**
	 * Prepare this builder for the specified image. The images to be computed (if any) will save their tiles in the default {@linkplain TileCache
	 * tile cache}.
	 * 
	 * @param image The source image.
	 */
	public final ImageWorker setImage(final RenderedImage image) {
		inheritanceStopPoint = this.image = image;
		return this;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// ////// ////////
	// ////// IMAGE, PROPERTIES AND RENDERING HINTS ACCESSORS ////////
	// ////// ////////
	// /////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the current image.
	 * 
	 * @return The rendered image.
	 * 
	 * @see #getBufferedImage
	 * @see #getPlanarImage
	 * @see #getRenderedOperation
	 * @see #getImageAsROI
	 */
	public final RenderedImage getRenderedImage() {
		return image;
	}

	/**
	 * Returns the <cite>region of interest</cite> currently set, or {@code null} if none. The default value is {@code null}.
	 * 
	 * @return The current region of interest.
	 * 
	 * @see #getMinimums
	 * @see #getMaximums
	 */
	public final ROI getROI() {
		return roi;
	}

	/**
	 * Set the <cite>region of interest</cite> (ROI). A {@code null} set the ROI to the whole {@linkplain #image}. The ROI is used by statistical
	 * methods like {@link #getMinimums} and {@link #getMaximums}.
	 * 
	 * @param roi The new region of interest.
	 * @return This ImageWorker
	 * 
	 * @see #getMinimums
	 * @see #getMaximums
	 */
	public final ImageWorker setROI(final ROI roi) {
		this.roi = roi;
		invalidateStatistics();
		return this;
	}

	/**
	 * Returns the rendering hint for the specified key, or {@code null} if none.
	 */
	public final Object getRenderingHint(final RenderingHints.Key key) {
		return (commonHints != null) ? commonHints.get(key) : null;
	}

	/**
	 * Set a map of rendering hints to use for all images to be computed by this class. This method applies only to the next images to be computed;
	 * images already computed before this method call (if any) will not be affected.
	 * 
	 * <p>
	 * If <code>hints</code> is null we won't modify this list.
	 * 
	 * @return This ImageWorker
	 * @see #setRenderingHint(RenderingHints)
	 */
	public final ImageWorker setRenderingHints(final RenderingHints hints) {
		if (commonHints == null) {
			commonHints = new RenderingHints(null);
		}
		if (hints != null)
			commonHints.add(hints);
		return this;
	}


	/**
	 * Returns the rendering hints for an image to be computed by this class. The default implementation returns the following hints:
	 * <p>
	 * <ul>
	 * <li>An {@linkplain ImageLayout image layout} with tiles size computed automatically from the current {@linkplain #image} size.</li>
	 * <li>Any additional hints specified through the {@link #setRenderingHint} method. If the user provided explicitly a {@link JAI#KEY_IMAGE_LAYOUT}
	 * , then the user layout has precedence over the automatic layout computed in previous step.</li>
	 * </ul>
	 * 
	 * @return The rendering hints to use for image computation (never {@code null}).
	 */
	public final RenderingHints getRenderingHints() {
		RenderingHints hints = ImageUtilities.getRenderingHints(image);
		if (hints == null) {
			hints = new RenderingHints(null);
			if (commonHints != null) {
				hints.add(commonHints);
			}
		} else if (commonHints != null) {
			hints.putAll(commonHints);
		}
		if (Boolean.FALSE.equals(hints.get(TILING_ALLOWED))) {
			final ImageLayout layout = getImageLayout(hints);
			if (commonHints == null || layout != commonHints.get(JAI.KEY_IMAGE_LAYOUT)) {
				// Set the layout only if it is not a user-supplied object.
				layout.setTileWidth(image.getWidth());
				layout.setTileHeight(image.getHeight());
				layout.setTileGridXOffset(image.getMinX());
				layout.setTileGridYOffset(image.getMinY());
				hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
			}
		}
		if (tileCacheDisabled != 0
				&& (commonHints != null && !commonHints.containsKey(JAI.KEY_TILE_CACHE))) {
			hints.add(new RenderingHints(JAI.KEY_TILE_CACHE, null));
		}
		return hints;
	}


	/**
	 * Returns the {@linkplain #getRenderingHints rendering hints}, but with a {@linkplain ComponentColorModel component color model} of the specified
	 * data type. The data type is changed only if no color model was explicitly specified by the user through {@link #getRenderingHints()}.
	 * 
	 * @param type The data type (typically {@link DataBuffer#TYPE_BYTE}).
	 */
	private final RenderingHints getRenderingHints(final int type) {
		/*
		 * Gets the default hints, which usually contains only informations about tiling. If the user overridden the rendering hints with an explict
		 * color model, keep the user's choice.
		 */
		final RenderingHints hints = getRenderingHints();
		final ImageLayout layout = getImageLayout(hints);
		if (layout.isValid(ImageLayout.COLOR_MODEL_MASK)) {
			return hints;
		}
		/*
		 * Creates the new color model.
		 */
		final ColorModel oldCm = image.getColorModel();
		if (oldCm != null) {
			final ColorModel newCm = new ComponentColorModel(oldCm.getColorSpace(),
					oldCm.hasAlpha(), // If true, supports transparency.
					oldCm.isAlphaPremultiplied(), // If true, alpha is premultiplied.
					oldCm.getTransparency(), // What alpha values can be represented.
					type); // Type of primitive array used to represent pixel.
			/*
			 * Creating the final image layout which should allow us to change color model.
			 */
			layout.setColorModel(newCm);
			layout.setSampleModel(newCm.createCompatibleSampleModel(image.getWidth(),
					image.getHeight()));
		} else {
			final int numBands = image.getSampleModel().getNumBands();
			final ColorModel newCm = new ComponentColorModel(new BogusColorSpace(numBands), false, // If true, supports transparency.
					false, // If true, alpha is premultiplied.
					Transparency.OPAQUE, // What alpha values can be represented.
					type); // Type of primitive array used to represent pixel.
			/*
			 * Creating the final image layout which should allow us to change color model.
			 */
			layout.setColorModel(newCm);
			layout.setSampleModel(newCm.createCompatibleSampleModel(image.getWidth(),
					image.getHeight()));
		}
		hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
		return hints;
	}

	/**
	 * Gets the image layout from the specified rendering hints, creating a new one if needed. This method do not modify the specified hints. If the
	 * caller modifies the image layout, it should invoke {@code hints.put(JAI.KEY_IMAGE_LAYOUT, layout)} explicitly.
	 */
	private static ImageLayout getImageLayout(final RenderingHints hints) {
		final Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
		if (candidate instanceof ImageLayout) {
			return (ImageLayout) candidate;
		}
		return new ImageLayout();
	}

	/**
	 * Returns {@code true} if the {@linkplain #image} uses a RGB {@linkplain ColorSpace color space}. Note that a RGB color space doesn't mean that
	 * pixel values are directly stored as RGB components. The image may be {@linkplain #isIndexed indexed} as well.
	 * 
	 * @see #forceColorSpaceRGB
	 */
	public final boolean isColorSpaceRGB() {
		final ColorModel cm = image.getColorModel();
		if (cm == null) {
			return false;
		}
		return cm.getColorSpace().getType() == ColorSpace.TYPE_RGB;
	}


	/**
	 * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model} preserving transparency. This is
	 * used especially in order to go from {@link PackedColorModel} to {@link ComponentColorModel}, which seems to be well accepted from
	 * {@code PNGEncoder} and {@code TIFFEncoder}.
	 * <p>
	 * This code is adapted from jai-interests mailing list archive.
	 * 
	 * @return this {@link ImageWorker}.
	 * 
	 * @see FormatDescriptor
	 */
	public final ImageWorker forceComponentColorModel() {
		return forceComponentColorModel(false);
	}

	/**
	 * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model} preserving transparency. This is
	 * used especially in order to go from {@link PackedColorModel} to {@link ComponentColorModel}, which seems to be well accepted from
	 * {@code PNGEncoder} and {@code TIFFEncoder}.
	 * <p>
	 * This code is adapted from jai-interests mailing list archive.
	 * 
	 * @param checkTransparent
	 * @param optimizeGray
	 * 
	 * @return this {@link ImageWorker}.
	 * 
	 * @see FormatDescriptor
	 */
	public final ImageWorker forceComponentColorModel(boolean checkTransparent, boolean optimizeGray) {
		final ColorModel cm = image.getColorModel();
		if (cm instanceof ComponentColorModel) {
			// Already an component color model - nothing to do.
			return this;
		}
		// shortcut for index color model
		if (cm instanceof IndexColorModel) {
			final IndexColorModel icm = (IndexColorModel) cm;
			final SampleModel sm = this.image.getSampleModel();
			final int datatype = sm.getDataType();
			final boolean gray = isGrayPalette(icm, checkTransparent) & optimizeGray;
			final boolean alpha = icm.hasAlpha();
			/*
			 * If the image is grayscale, retain only the needed bands.
			 */
			final int numDestinationBands = gray ? (alpha ? 2 : 1) : (alpha ? 4 : 3);
			LookupTableJAI lut = null;

			switch (datatype) {
			case DataBuffer.TYPE_BYTE: {
				final byte data[][] = new byte[numDestinationBands][icm.getMapSize()];
				icm.getReds(data[0]);
				if (numDestinationBands >= 2)
					// remember to optimize for grayscale images
					if (!gray)
						icm.getGreens(data[1]);
					else
						icm.getAlphas(data[1]);
				if (numDestinationBands >= 3)
					icm.getBlues(data[2]);
				if (numDestinationBands == 4) {
					icm.getAlphas(data[3]);
				}
				lut = new LookupTableJAI(data);

			}
			break;

			case DataBuffer.TYPE_USHORT: {
				final int mapSize = icm.getMapSize();
				final short data[][] = new short[numDestinationBands][mapSize];
				for (int i = 0; i < mapSize; i++) {
					data[0][i] = (short) icm.getRed(i);
					if (numDestinationBands >= 2)
						// remember to optimize for grayscale images
						if (!gray)
							data[1][i] = (short) icm.getGreen(i);
						else
							data[1][i] = (short) icm.getAlpha(i);
					if (numDestinationBands >= 3)
						data[2][i] = (short) icm.getBlue(i);
					if (numDestinationBands == 4) {
						data[3][i] = (short) icm.getAlpha(i);
					}
				}
				lut = new LookupTableJAI(data, datatype == DataBuffer.TYPE_USHORT);

			}
			break;

			default:
				throw new IllegalArgumentException("ERROR");
			}

			// did we initialized the LUT?
			if (lut == null)
				throw new IllegalStateException("lut");
			/*
			 * Get the default hints, which usually contains only informations about tiling. If the user override the rendering hints with an explicit
			 * color model, keep the user's choice.
			 */
			final RenderingHints hints = (RenderingHints) getRenderingHints();
			final ImageLayout layout;
			final Object candidate = hints.get(JAI.KEY_IMAGE_LAYOUT);
			if (candidate instanceof ImageLayout) {
				layout = (ImageLayout) candidate;
			} else {
				layout = new ImageLayout(image);
				hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
			}

			int[] bits = new int[numDestinationBands];
			// bits per component
			for (int i = 0; i < numDestinationBands; i++)
				bits[i] = sm.getSampleSize(i);
			final ComponentColorModel destinationColorModel = new ComponentColorModel(
					numDestinationBands >= 3 ? ColorSpace.getInstance(ColorSpace.CS_sRGB)
							: ColorSpace.getInstance(ColorSpace.CS_GRAY), bits, alpha,
							cm.isAlphaPremultiplied(), alpha ? Transparency.TRANSLUCENT
									: Transparency.OPAQUE, datatype);
			final SampleModel destinationSampleModel = destinationColorModel
					.createCompatibleSampleModel(image.getWidth(), image.getHeight());
			layout.setColorModel(destinationColorModel);
			layout.setSampleModel(destinationSampleModel);
			image = LookupDescriptor.create(image, lut, hints);

		} else {
			// Most of the code adapted from jai-interests is in 'getRenderingHints(int)'.
			final int type = (cm instanceof DirectColorModel) ? DataBuffer.TYPE_BYTE : image
					.getSampleModel().getTransferType();
			final RenderingHints hints = getRenderingHints(type);
			// image=ColorConvertDescriptor.create(image, RIFUtil.getImageLayoutHint(hints).getColorModel(null), hints);
			image = FormatDescriptor.create(image, type, hints);
			;
		}
		invalidateStatistics();

		// All post conditions for this method contract.
		assert image.getColorModel() instanceof ComponentColorModel;
		return this;
	}

	/**
	 * Reformats the {@linkplain ColorModel color model} to a {@linkplain ComponentColorModel component color model} preserving transparency. This is
	 * used especially in order to go from {@link PackedColorModel} to {@link ComponentColorModel}, which seems to be well accepted from
	 * {@code PNGEncoder} and {@code TIFFEncoder}.
	 * <p>
	 * This code is adapted from jai-interests mailing list archive.
	 * 
	 * @param checkTransparent tells this method to not consider fully transparent pixels when optimizing grayscale palettes.
	 * 
	 * @return this {@link ImageWorker}.
	 * 
	 * @see FormatDescriptor
	 */
	public final ImageWorker forceComponentColorModel(boolean checkTransparent) {
		return forceComponentColorModel(checkTransparent, true);
	}

	/**
	 * Forces the {@linkplain #image} color model to the {@linkplain ColorSpace#CS_sRGB RGB color space}. If the current color space is already of
	 * {@linkplain ColorSpace#TYPE_RGB RGB type}, then this method does nothing. This operation may loose the alpha channel.
	 * 
	 * @return this {@link ImageWorker}.
	 * 
	 * @see #isColorSpaceRGB
	 * @see ColorConvertDescriptor
	 */
	public final ImageWorker forceColorSpaceRGB() {
		if (!isColorSpaceRGB()) {
			final ColorModel cm = new ComponentColorModel(
					ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, Transparency.OPAQUE,
					image.getSampleModel().getDataType());

			// force computation of the new colormodel
			forceColorModel(cm);
		}
		// All post conditions for this method contract.
		assert isColorSpaceRGB();
		return this;
	}


	/** Forces the provided {@link ColorModel} via the JAI ColorConvert operation. */
	private void forceColorModel(final ColorModel cm) {

		final ImageLayout2 il = new ImageLayout2(image);
		il.setColorModel(cm);
		il.setSampleModel(cm.createCompatibleSampleModel(image.getWidth(), image.getHeight()));
		final RenderingHints oldRi = this.getRenderingHints();
		final RenderingHints newRi = (RenderingHints) oldRi.clone();
		newRi.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il));
		setRenderingHints(newRi);
		image = ColorConvertDescriptor.create(image, cm, getRenderingHints());

		// restore RI
		this.setRenderingHints(oldRi);

		// invalidate stats
		invalidateStatistics();
	}

	/**
	 * Tells this builder that all statistics on pixel values (e.g. the "extrema" property in the {@linkplain #image}) should not be inherited from
	 * the source images (if any). This method should be invoked every time an operation changed the pixel values.
	 * 
	 * @return This ImageWorker
	 */
	private ImageWorker invalidateStatistics() {
		inheritanceStopPoint = image;
		return this;
	}

	public static boolean isGrayPalette(final IndexColorModel icm, boolean ignoreTransparents) {
		if (!icm.hasAlpha()) {
			// We will not check transparent pixels if there is none in the color model.
			ignoreTransparents = false;
		}
		final int mapSize = icm.getMapSize();
		for (int i=0; i<mapSize; i++) {
			if (ignoreTransparents) {
				// If this entry is transparent and we were asked
				// to check transparents pixels, let's leave.
				if (icm.getAlpha(i) == 0) {
					continue;
				}
			}
			// Get the color for this pixel only if it is requested.
			// If gray, all components are the same.
			final int green = icm.getGreen(i);
			if (green != icm.getRed(i) || green != icm.getBlue(i)) {
				return false;
			}
		}
		return true;
	}

}
