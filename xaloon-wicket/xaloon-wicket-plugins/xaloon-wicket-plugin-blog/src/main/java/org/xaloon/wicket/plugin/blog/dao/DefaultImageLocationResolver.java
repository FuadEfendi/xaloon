package org.xaloon.wicket.plugin.blog.dao;

import javax.inject.Named;

import org.xaloon.core.api.image.ImageLocationResolver;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;

@Named("imageLocationResolver")
public class DefaultImageLocationResolver implements ImageLocationResolver<BlogEntry> {

	/**
	 * Default location where to store blog thumbnails
	 */
	public static final String BLOG_THUMBNAILS = "blog-thumbnails";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String resolveImageLocation(BlogEntry entry) {
		return UrlUtil.encode(entry.getTitle());
	}

	@Override
	public String resolveThumbnailLocation(BlogEntry item) {
		return BLOG_THUMBNAILS;
	}

}
