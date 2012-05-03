package org.xaloon.core.api.image;

import java.io.Serializable;

/**
 * @author vytautas r.
 * @param <T>
 */
public interface ImageLocationResolver<T> extends Serializable {
	/**
	 * @param item
	 * @return
	 */
	String resolveImageLocation(T item);

	/**
	 * @param item
	 * @return
	 */
	String resolveThumbnailLocation(T item);
}
