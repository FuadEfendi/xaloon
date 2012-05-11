package org.xaloon.wicket.plugin.image.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="XAL_ALBUM_IMAGES")
public class JpaAlbumImageComposition extends JpaImageComposition<JpaAlbum>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JpaAlbumImageComposition() {}
	
	public JpaAlbumImageComposition(JpaAlbum album) {
		super(album);
	}
}
