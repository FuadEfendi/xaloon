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
package org.xaloon.wicket.plugin.image.model;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrimaryKeyJoinColumn;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.core.jpa.model.AbstractEntity;

@MappedSuperclass
public abstract class JpaImageComposition<T extends Album> extends AbstractEntity implements ImageComposition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JpaImageComposition() {}
	
	public JpaImageComposition(T album) {
		setObject(album);
	}

	@ManyToOne
	@PrimaryKeyJoinColumn(name = "OBJECT_ID", referencedColumnName = "ID")
	private T object;

	@ManyToOne(cascade=CascadeType.REFRESH)
	@PrimaryKeyJoinColumn(name = "IMAGE_ID", referencedColumnName = "ID")
	private JpaImage image;

	/**
	 * @return the object
	 */
	public T getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Album object) {
		this.object = (T)object;
	}

	/**
	 * @return the image
	 */
	public JpaImage getImage() {
		return image;
	}

	@Override
	public void setImage(Image image) {
		this.image = (JpaImage)image;
	}
	
	/**
	 * Sorting current menu item by provided order property
	 * <p>
	 * If order property id is not provided then order by name
	 */
	public int compareTo(JpaImageComposition o) {
		if (o == null) {
			return 1;
		}
		if (getObject() == null) {
			return 1;
		}
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getObject().getId(), o.getObject().getId());

		return compareToBuilder.toComparison();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getObject() == null || !(obj instanceof JpaImageComposition)) {
			return false;
		}
		JpaImageComposition item = (JpaImageComposition)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getObject().getId(), item.getObject().getId());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		if (getObject() == null) {
			return 1;
		}
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getObject().getId());
		return hashCodeBuilder.hashCode();
	}
}
