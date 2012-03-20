package org.xaloon.wicket.plugin.google.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.asynchronous.RetryAction;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.plugin.email.EmailFacade;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.core.api.util.UrlUtil;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Kind.AdaptorException;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.ServiceException;

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

/**
 * @author vytautas.r
 */
@Named("picasaFileStorageService")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PicasaFileStorageService implements FileStorageService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PicasaFileStorageService.class);

	private static final String API_PREFIX = "https://picasaweb.google.com/data/feed/api/user/";

	@Inject
	private SecurityFacade securityFacade;
	
	@Inject
	private EmailFacade emailFacade;

	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer) {
		return storeFile(fileDescriptor, inputStreamContainer, new HashMap<String, Object>());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer, Map<String, Object> additionalProperties) {
		try {
			String userEmail = (String)additionalProperties.get(FileStorageService.PARAMETER_USER_EMAIL);
			if (StringUtils.isEmpty(userEmail)) {
				userEmail = securityFacade.getCurrentUserEmail();
			}
			if (StringUtils.isEmpty(userEmail)) {
				throw new IllegalArgumentException("curent user's email is not provided! Email should correspond google account");
			}
			if (StringUtils.isEmpty(fileDescriptor.getLocation())) {
				throw new IllegalArgumentException("location is not provided!");
			}
			final PicasawebService picasawebService = new PicasawebService("xaloon-application");
			Object accessTokenValue = (Object)additionalProperties.get(FileStorageService.PARAMETER_USER_TOKEN);
			if (accessTokenValue == null) {
				accessTokenValue = Configuration.get().getOauthSecurityTokenProvider().getSecurityToken();
			}
			
			setSecurityToken(picasawebService, accessTokenValue);
			
			String albumsUrl = API_PREFIX + userEmail + "?kind=album";

			UserFeed userFeed = getFeed(picasawebService, albumsUrl, UserFeed.class);

			GphotoEntry album = findOrCreateAlbum(picasawebService, userFeed, UrlUtil.encode(fileDescriptor.getLocation()));

			final URL albumPostUrl = new URL(API_PREFIX + userEmail + "/albumid/" + album.getGphotoId());

			final PhotoEntry myPhoto = new PhotoEntry();
			myPhoto.setTitle(new PlainTextConstruct(fileDescriptor.getPath()));

			MediaStreamSource myMedia = new MediaStreamSource(inputStreamContainer.getInputStream(), "image/jpeg");
			myPhoto.setMediaSource(myMedia);

			PhotoEntry entry =new RetryAction<PhotoEntry, Void>(false) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				protected PhotoEntry onPerform(Void parameters) {
					try {
						return picasawebService.insert(albumPostUrl, myPhoto);
					} catch (Exception e) {
						LOGGER.error("Could not upload photo, due to picasa service error!", e);
					}
					return null;
				}
			}.setRandomTimeUsed(true).setMillisecondsToSleep(20000).setRetryCount(5). perform(null);
			 
			if (entry == null) {
				emailFacade.sendMailToSystem(String.format("Upload of image failed: %s", fileDescriptor.getPath()), emailFacade.getSystemEmail(), "Picasa File Storage");
				throw new RuntimeException("Could not finish action, because service is not relialible!");
			}
			String identifier = "/albumid/" + album.getGphotoId() + "/photoid/" + entry.getGphotoId();
			KeyValue<String, String> result = new DefaultKeyValue<String, String>(entry.getMediaContents().get(0).getUrl(), identifier);
			LOGGER.info("New image uploaded.");
			return result;
		} catch (Exception e) {
			LOGGER.error("Got exception", e);
			throw new RuntimeException(e);
		} finally {
			inputStreamContainer.close();
		}
	}

	private void setSecurityToken(PicasawebService picasawebService, Object accessTokenValue) {
		if (accessTokenValue == null) {
			throw new IllegalArgumentException("Google access token was not found in session! Is user logged in using google login service?");
		}
		if (accessTokenValue instanceof Token) {
			picasawebService.setAuthSubToken(((Token)accessTokenValue).getToken());
		} else if (accessTokenValue instanceof String) {
			picasawebService.setUserToken((String)accessTokenValue);
		} else {
			throw new RuntimeException(String.format("Authentication token is not supported: %s!", accessTokenValue.toString()));
		}
	}

	@SuppressWarnings("rawtypes")
	private GphotoEntry findOrCreateAlbum(PicasawebService picasawebService, UserFeed userFeed, String location) throws MalformedURLException,
			IOException, ServiceException {
		GphotoEntry album = findAlbum(userFeed, location);
		if (album == null) {
			album = createAlbum(picasawebService, location);
		}
		return album;
	}

	@SuppressWarnings("rawtypes")
	private GphotoEntry createAlbum(PicasawebService picasawebService, String location) throws MalformedURLException, IOException, ServiceException {
		String feedUrl = API_PREFIX + "default";

		AlbumEntry album = new AlbumEntry();

		album.setTitle(new PlainTextConstruct(location));
		album.setDate(new Date());
		return picasawebService.insert(new URL(feedUrl), album);
	}

	@SuppressWarnings("rawtypes")
	private GphotoEntry findAlbum(UserFeed userFeed, String location) throws AdaptorException {
		GphotoEntry result = null;
		List<GphotoEntry> entries = userFeed.getEntries();
		for (GphotoEntry entry : entries) {
			if (entry.getTitle().getPlainText().equalsIgnoreCase(location)) {
				result = entry;			
				break;
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public <T extends GphotoFeed> T getFeed(PicasawebService picasawebService, String feedHref, Class<T> feedClass) throws IOException,
			ServiceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Get Feed URL: " + feedHref);
		}
		return picasawebService.getFeed(new URL(feedHref), feedClass);
	}

	@Override
	public boolean delete(String uniqueIdentifier) {
		PicasawebService picasawebService = new PicasawebService("xaloon-application");
		picasawebService.setHeader("If-Match", "*");
		try {
			setSecurityToken(picasawebService, Configuration.get().getOauthSecurityTokenProvider().getSecurityToken());
			
			final URL photoPostUrl = new URL("https://picasaweb.google.com/data/entry/api/user/" + securityFacade.getCurrentUserEmail()
					+ uniqueIdentifier);

			picasawebService.delete(photoPostUrl);
			// TODO validate if photo album is not empty
		} catch (Exception e) {
			LOGGER.error(String.format("Could not delete resource: %s", uniqueIdentifier), e);
			return false;
		}
		return true;
	}

	@Override
	public byte[] getByteArrayByIdentifier(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
