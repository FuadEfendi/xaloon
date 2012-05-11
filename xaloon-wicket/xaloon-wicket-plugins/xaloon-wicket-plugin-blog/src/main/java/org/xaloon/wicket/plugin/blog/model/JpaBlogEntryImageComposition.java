package org.xaloon.wicket.plugin.blog.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.xaloon.core.api.image.model.Album;
import org.xaloon.wicket.plugin.image.model.JpaImageComposition;

@Entity
@Table(name="XAL_BLOG_ENTRY_IMAGES")
public class JpaBlogEntryImageComposition extends JpaImageComposition<JpaBlogEntry>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JpaBlogEntryImageComposition() {}
	
	public JpaBlogEntryImageComposition(Album album) {
		super((JpaBlogEntry)album);
	}

	
}
