package org.xaloon.wicket.plugin.google.storage;

import java.io.IOException;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.image.AbstractImageRepository;
import org.xaloon.core.api.image.ImageOptions;
import org.xaloon.core.api.image.ImageRepository;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.wicket.plugin.google.storage.util.GoogleImagePathUtil;

@Named("picasaImageRepository")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PicasaImageRepository extends AbstractImageRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	@Named("picasaFileStorageService")
	private FileStorageService fileStorageService;
	
	@Inject
	@Named("jpaImageRepository")
	private ImageRepository alternativeImageRepository;

	/**
	 * @return the alternativeImageRepository
	 */
	public ImageRepository getAlternativeImageRepository() {
		return alternativeImageRepository;
	}

	@Override
	protected FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	@Override
	protected KeyValue<String, String> storeFile(Image image, ImageOptions options) throws IOException {
		if (GoogleImagePathUtil.isPicasaImage(image.getPath())) {
			return createPicasaDescriptor(image, options);
		}
		InputStreamContainer resizedInputStreamContainer = resize(options);
		return getFileStorageService().storeFile(image, resizedInputStreamContainer);
	}

	private KeyValue<String, String> createPicasaDescriptor(Image image, ImageOptions options) {
		KeyValue<String, String> uniqueIdentifier = new DefaultKeyValue<String, String>();
		uniqueIdentifier.setKey(GoogleImagePathUtil.getGoogleResizedPath(image.getPath(), options.getImageSize().getWidth()));
		uniqueIdentifier.setValue(uniqueIdentifier.getKey());
		return uniqueIdentifier;
	}
}
