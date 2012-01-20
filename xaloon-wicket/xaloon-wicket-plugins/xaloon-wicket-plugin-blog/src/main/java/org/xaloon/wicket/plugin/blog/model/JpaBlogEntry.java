/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xaloon.wicket.plugin.blog.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.classifier.model.JpaClassifierItem;
import org.xaloon.wicket.plugin.blog.path.BlogEntryPathTypeEnum;
import org.xaloon.wicket.plugin.image.model.AbstractAlbum;
import org.xaloon.wicket.plugin.image.model.JpaImage;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_BLOG_ENTRY", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID", "PATH" }))
public class JpaBlogEntry extends AbstractAlbum implements BlogEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CUSTOM_PATH")
	private String customPath;

	@ManyToOne
	@JoinColumn(name = "BLOG_CATEGORY_ID", referencedColumnName = "ID")
	private JpaClassifierItem category;

	@Column(name = "VIEW_COUNT")
	private Long viewCount;

	@Column(name = "COMMENT_COUNT")
	private Long commentCount;

	@Column(name = "RATING")
	private Long rating;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "XAL_BLOG_ENTRY_TAGS", joinColumns = { @JoinColumn(name = "BLOG_ID") }, inverseJoinColumns = { @JoinColumn(name = "TAG_ID") })
	private List<JpaBlogEntryTag> tags = new ArrayList<JpaBlogEntryTag>();

	@Column(name = "IS_STICKY")
	private boolean sticky;

	@Enumerated(EnumType.STRING)
	@Column(name = "BLOG_ENTRY_PATH_TYPE")
	private BlogEntryPathTypeEnum blogEntryPathType;

	@Column(name = "CONTENT", nullable = false)
	@Lob
	private String content;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "XAL_BLOG_ENTRY_IMAGES", joinColumns = { @JoinColumn(name = "BLOG_ID") }, inverseJoinColumns = { @JoinColumn(name = "IMAGE_ID") })
	private List<JpaImage> images = new ArrayList<JpaImage>();

	public JpaClassifierItem getCategory() {
		return category;
	}

	/**
	 * @param category
	 */
	public void setCategory(JpaClassifierItem category) {
		this.category = category;
	}

	public Long getViewCount() {
		return viewCount;
	}

	/**
	 * @param viewCount
	 */
	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	/**
	 * @param commentCount
	 */
	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getRating() {
		return rating;
	}

	/**
	 * @param rating
	 */
	public void setRating(Long rating) {
		this.rating = rating;
	}

	public List<JpaBlogEntryTag> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 */
	public void setTags(List<JpaBlogEntryTag> tags) {
		this.tags = tags;
	}

	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getCustomPath() {
		return customPath;
	}

	/**
	 * @param customPath
	 */
	public void setCustomPath(String customPath) {
		this.customPath = customPath;
	}


	public boolean isSticky() {
		return sticky;
	}

	/**
	 * @param sticky
	 */
	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	/**
	 * Gets blogEntryPathType.
	 * 
	 * @return blogEntryPathType
	 */
	public BlogEntryPathTypeEnum getBlogEntryPathType() {
		return blogEntryPathType;
	}

	/**
	 * Sets blogEntryPathType.
	 * 
	 * @param blogEntryPathType
	 *            blogEntryPathType
	 */
	public void setBlogEntryPathType(BlogEntryPathTypeEnum blogEntryPathType) {
		this.blogEntryPathType = blogEntryPathType;
	}

	@Override
	public Long getComponentId() {
		return 1000L;// TODO FIX return CommentComponentContainer.COMPONENT_BLOG_ENTRY;
	}

	@Override
	protected void beforeCreate() {
		super.beforeCreate();
		if (!StringUtils.isEmpty(getCustomPath())) {
			setPath(getCustomPath());
		} else {
			setPath(UrlUtil.encode(getTitle()));
		}
	}

	@Override
	protected void beforeUpdate() {
		super.beforeUpdate();
		if (BlogEntryPathTypeEnum.CUSTOM.equals(getBlogEntryPathType()) && !StringUtils.isEmpty(getCustomPath()) &&
			!getCustomPath().equals(getPath())) {
			setPath(getCustomPath());
		}
	}

	@Override
	public String getOwnerUsername() {
		if (getOwner() != null) {
			return getOwner().getUsername();
		}
		return null;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Album#getImages()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Image> getImages() {
		return images;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Album#setImages(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setImages(List<? extends Image> images) {
		/**
		 * assume that if at first element failed then all elements should fail. Of cource it is possible to switch values, but why somebody should do
		 * this?
		 */
		if (images != null && !images.isEmpty() && !(images.get(0) instanceof JpaImage)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.images = (List<JpaImage>)images;
	}

	@Override
	public String toString() {
		return String.format("[%s] title='%s'", this.getClass().getSimpleName(), getTitle());
	}
}
