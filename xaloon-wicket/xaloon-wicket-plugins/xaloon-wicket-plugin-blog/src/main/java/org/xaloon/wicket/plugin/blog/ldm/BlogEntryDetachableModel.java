package org.xaloon.wicket.plugin.blog.ldm;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.xaloon.wicket.plugin.blog.dao.BlogDao;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntry;

/**
 * Blog entry model. It should be loaded by it's path and path should be unique
 * 
 * @author vytautas r.
 * 
 */
public class BlogEntryDetachableModel extends LoadableDetachableModel<BlogEntry> {
	private static final long serialVersionUID = 1L;

	private String blog_path;

	private String username;

	@Inject
	private BlogDao blogDao;

	/**
	 * Construct.
	 */
	public BlogEntryDetachableModel() {
	}

	/**
	 * Construct.
	 * 
	 * @param username
	 * @param blog_path
	 */
	public BlogEntryDetachableModel(String username, String blog_path) {
		this.blog_path = blog_path;
		this.username = username;
		Injector.get().inject(this);
	}

	@Override
	protected BlogEntry load() {
		if (!StringUtils.isEmpty(blog_path)) {
			return blogDao.findEntryByPath(username, blog_path);
		}
		return new JpaBlogEntry();
	}
}
