package org.xaloon.wicket.plugin.google.storage;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.wicket.Session;
import org.scribe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.wicket.component.security.AuthenticatedWebSession;

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

	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer) {
		return storeFile(fileDescriptor, inputStreamContainer, new HashMap<String, Object>());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer, Map<String, Object> additionalProperties) {
		try {
			String userEmail = (String)additionalProperties.get("useremail");
			if (StringUtils.isEmpty(userEmail)) {
				userEmail = securityFacade.getCurrentUserEmail();
			}
			if (StringUtils.isEmpty(userEmail)) {
				throw new IllegalArgumentException("curent user's email is not provided! Email should correspond google account");
			}
			if (StringUtils.isEmpty(fileDescriptor.getLocation())) {
				throw new IllegalArgumentException("location is not provided!");
			}
			PicasawebService picasawebService = new PicasawebService("xaloon-application");
			Token accessToken = (Token)additionalProperties.get("authToken");
			if (accessToken == null) {
				accessToken = (Token) Session.get().getMetaData(AuthenticatedWebSession.METADATAKEY_AUTH_TOKEN);
			}

			if (accessToken == null) {
				throw new IllegalArgumentException("Google access token was not found in session! Is user logged in using google login service?");
			}
			picasawebService.setAuthSubToken(accessToken.getToken());
			String albumsUrl = API_PREFIX + userEmail + "?kind=album";

			UserFeed userFeed = getFeed(picasawebService, albumsUrl, UserFeed.class);

			GphotoEntry album = findOrCreateAlbum(picasawebService, userFeed, UrlUtil.encode(fileDescriptor.getLocation()));

			final URL albumPostUrl = new URL(API_PREFIX + userEmail + "/albumid/" + album.getGphotoId());

			final PhotoEntry myPhoto = new PhotoEntry();
			myPhoto.setTitle(new PlainTextConstruct(fileDescriptor.getPath()));

			MediaStreamSource myMedia = new MediaStreamSource(inputStreamContainer.getInputStream(), "image/jpeg");
			myPhoto.setMediaSource(myMedia);

			PhotoEntry entry = picasawebService.insert(albumPostUrl, myPhoto);

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
	public InputStream getInputStreamByIdentifier(String identifier) {
		return null;
	}

	@Override
	public boolean delete(String uniqueIdentifier) {
		PicasawebService picasawebService = new PicasawebService("xaloon-application");
		picasawebService.setHeader("If-Match", "*");
		try {

			Token accessToken = (Token) Session.get().getMetaData(AuthenticatedWebSession.METADATAKEY_AUTH_TOKEN);
			picasawebService.setAuthSubToken(accessToken.getToken());

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

}
