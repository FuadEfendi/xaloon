package org.xaloon.wicket.plugin.image.impl;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.image.ImageDao;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.wicket.plugin.image.model.JpaImage;

/**
 * 
 * @author vytautas.r
 *
 */
@Named("imageDao")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefaultImageDao implements ImageDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;
	
	@Override
	public Image getImageByPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		QueryBuilder queryBuilder = new QueryBuilder("select jfd from " + JpaImage.class.getSimpleName() + " jfd ");
		queryBuilder.addParameter("jfd.path", "ABSOLUTE_PATH", path);

		return persistenceServices.executeQuerySingle(queryBuilder);
	}

}
