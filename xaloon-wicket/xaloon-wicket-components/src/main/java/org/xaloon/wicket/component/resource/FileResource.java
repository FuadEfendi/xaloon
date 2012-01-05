package org.xaloon.wicket.component.resource;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.cache.Cache;
import org.xaloon.core.api.storage.FileRepositoryFacade;

/**
 * Dynamic resource from file repository
 * 
 * @author vytautas r.
 * @version 1.1, 10/08/10
 * @since 1.3
 */
public class FileResource extends DynamicImageResource {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FileResource.class);

	@Inject
	private FileRepositoryFacade fileRepository;

	/**
	 * Construct.
	 * 
	 * @param preffix
	 */
	public FileResource() {
		Injector injector = Injector.get();
		if (injector != null) {
			injector.inject(this);
		}
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		org.apache.wicket.util.string.StringValue pathId = attributes.getParameters().get(0);
		if (pathId.isEmpty()) {
			throw new IllegalArgumentException("Attribute cannot be empty!");
		}
		String value = pathId.toString();
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		byte[] theBytes = null;
		try {
			theBytes = IOUtils.toByteArray(fileRepository.getFileByPath(value));
		} catch (IOException e) {
			logger.error("Error while reading from input stream", e);
		}
		return theBytes;
	}

	/**
	 * @param fileRepository
	 */
	public void setFileRepository(FileRepositoryFacade fileRepository) {
		this.fileRepository = fileRepository;
	}

	/**
	 * @param fileCache
	 */
	public void setFileCache(Cache fileCache) {
		// this.fileCache = fileCache;
	}
}
