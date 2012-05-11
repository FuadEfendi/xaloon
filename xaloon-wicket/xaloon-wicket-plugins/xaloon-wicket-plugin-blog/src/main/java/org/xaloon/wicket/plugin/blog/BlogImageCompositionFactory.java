package org.xaloon.wicket.plugin.blog;

import org.xaloon.core.api.image.ImageCompositionFactory;
import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntry;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntryImageComposition;

public class BlogImageCompositionFactory implements ImageCompositionFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ImageComposition newImageComposition(Album album, Image image) {
		ImageComposition result = new JpaBlogEntryImageComposition(album);
		result.setImage(image);
		return result;
	}
}
