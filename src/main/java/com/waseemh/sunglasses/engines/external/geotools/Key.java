package com.waseemh.sunglasses.engines.external.geotools;

import java.awt.RenderingHints;

/**
 * The type for keys used to control various aspects of the factory
 * creation. Factory creation impacts rendering (which is why extending
 * {@linkplain java.awt.RenderingHints.Key rendering key} is not a complete
 * non-sense), but may impact other aspects of an application as well.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class Key extends RenderingHints.Key {
	/**
	 * The number of key created up to date.
	 */
	private static int count;

	/**
	 * The class name for {@link #valueClass}.
	 */
	private final String className;

	/**
	 * Base class of all values for this key. Will be created from {@link #className} only when
	 * first required, in order to avoid too early class loading. This is significant for the
	 * {@link #JAI_INSTANCE} key for example, in order to avoid JAI dependencies in applications
	 * that do not need it.
	 */
	private transient Class<?> valueClass;

	/**
	 * Constructs a new key for values of the given class.
	 *
	 * @param classe The base class for all valid values.
	 */
	public Key(final Class<?> classe) {
		this(classe.getName());
		valueClass = classe;
	}

	/**
	 * Constructs a new key for values of the given class. The class is
	 * specified by name instead of a {@link Class} object. This allows to
	 * defer class loading until needed.
	 *
	 * @param className Name of base class for all valid values.
	 */
	Key(final String className) {
		super(count());
		this.className = className;
	}

	/**
	 * Workaround for RFE #4093999 ("Relax constraint on placement of this()/super()
	 * call in constructors"): {@code count++} need to be executed in a synchronized
	 * block since it is not an atomic operation.
	 */
	private static synchronized int count() {
		return count++;
	}

	/**
	 * Returns the expected class for values stored under this key.
	 *
	 * @return The class of values stored under this key.
	 */
	public Class<?> getValueClass() {
		if (valueClass == null) {
			try {
				valueClass = Class.forName(className);
			} catch (ClassNotFoundException exception) {
				valueClass = Object.class;
			}
		}
		return valueClass;
	}

	/**
	 * Returns {@code true} if the specified object is a valid value for
	 * this key. The default implementation checks if the specified value
	 * {@linkplain Class#isInstance is an instance} of the {@linkplain
	 * #getValueClass value class}.
	 * <p>
	 * Note that many hint keys defined in the {@link Hints} class relax this rule and accept
	 * {@link Class} object assignable to the expected {@linkplain #getValueClass value class}
	 * as well.
	 *
	 * @param value
	 *            The object to test for validity.
	 * @return {@code true} if the value is valid; {@code false} otherwise.
	 *
	 * @see Hints.ClassKey#isCompatibleValue
	 * @see Hints.FileKey#isCompatibleValue
	 * @see Hints.IntegerKey#isCompatibleValue
	 * @see Hints.OptionKey#isCompatibleValue
	 */
	public boolean isCompatibleValue(final Object value) {
		return getValueClass().isInstance(value);
	}

}