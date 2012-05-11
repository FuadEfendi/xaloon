package org.xaloon.wicket.plugin.blog.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.exception.CreateClassInstanceException;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.wicket.component.classifier.ClassifierDropDownChoice;
import org.xaloon.wicket.component.tag.StringTokensPanel;
import org.xaloon.wicket.plugin.blog.BlogEntryParameters;
import org.xaloon.wicket.plugin.blog.BlogImageCompositionFactory;
import org.xaloon.wicket.plugin.blog.BlogPlugin;
import org.xaloon.wicket.plugin.blog.BlogPluginBean;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.path.BlogEntryPathTypeEnum;
import org.xaloon.wicket.plugin.image.panel.AlbumAdministrationPanel;
import org.xaloon.wicket.plugin.image.plugin.GallerySecurityAuthorities;
import org.xaloon.wicket.util.UrlUtils;

import com.google.code.jqwicket.ui.ckeditor.CKEditorOptions;
import com.google.code.jqwicket.ui.ckeditor.CKEditorTextArea;

/**
 * @author vytautas r.
 */
public class CreateBlogEntryPanel extends AbstractBlogPluginPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param params
	 */
	public CreateBlogEntryPanel(String id, PageParameters params) {
		super(id, params);
	}

	@Override
	protected void onInitialize(BlogPlugin plugin, BlogPluginBean pluginBean) {
		BlogEntry jpaBlogEntry = null;
		if (!getPageRequestParameters().isEmpty()) {
			BlogEntryParameters parameters = parseBlogEntryParameters();
			if (parameters != null) {
				jpaBlogEntry = getBlogFacade().findEntryByPath(parameters.getUsername(), parameters.getPath());
			}
		}
		if (jpaBlogEntry == null) {
			jpaBlogEntry = blogFacade.newBlogEntry();
		}
		add(new CreateBlogEntryForm("blog-entry-form", jpaBlogEntry));
	}

	class CreateBlogEntryForm extends Form<BlogEntry> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private AlbumAdministrationPanel albumAdministrationPanel;

		private AlbumAdministrationPanel thumbnailManagementPanel;

		public CreateBlogEntryForm(String id, BlogEntry blogEntry) {
			super(id);
			setMultiPart(true);
			setModel(new CompoundPropertyModel<BlogEntry>(blogEntry));
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onInitialize() {
			super.onInitialize();

			// Add feedback panel
			add(new FeedbackPanel("feedback-panel"));

			// Add blog entry title and error message for it
			RequiredTextField<String> title = new RequiredTextField<String>("title");
			title.add(new MaximumLengthValidator(200));
			add(title);

			// Add custom path
			final WebMarkupContainer customPathContainer = new WebMarkupContainer("customPathContainer");
			customPathContainer.setOutputMarkupId(true);
			add(customPathContainer);

			final RequiredTextField<String> customPath = new RequiredTextField<String>("customPath");
			customPath.setVisible(getModelObject().getBlogEntryPathType().equals(BlogEntryPathTypeEnum.CUSTOM)); // hidden by default
			customPathContainer.add(customPath);

			BlogEntryPathDropDownChoice blogEntryPathDropDownChoice = new BlogEntryPathDropDownChoice("blogEntryPathType");
			add(blogEntryPathDropDownChoice);
			blogEntryPathDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (getModelObject().getBlogEntryPathType().equals(BlogEntryPathTypeEnum.CUSTOM)) {
						customPath.setVisible(true);
					} else {
						customPath.setVisible(false);
					}
					target.add(customPathContainer);
				}
			});

			// Add checkbox for sticky blog entry
			add(new CheckBox("sticky"));

			// Add image administration panel
			addBlogEntryImagePanel();

			// Add content
			add(new CKEditorTextArea<String>("content", new CKEditorOptions()).setRequired(true));

			// Add tags
			List<KeyValue<String, String>> tags = (List<KeyValue<String, String>>)getModelObject().getTags();
			add(new StringTokensPanel<KeyValue<String, String>>("tags-panel", KEY_VALUE_BLOG_TAG, tags) {
				private static final long serialVersionUID = 1L;

				@Override
				protected KeyValue<String, String> findInStorage(String key, String value) {
					return keyValueDao.findInStorage(key, value);
				}

				@Override
				protected KeyValue<String, String> newKeyValue(String key, String value, boolean persist) {
					try {
						return keyValueDao.newKeyValue(key, value);
					} catch (CreateClassInstanceException e) {
						LOGGER.error("Could not create JpaBlogEntryTag instance!", e);
						throw new RuntimeException(e);
					}
				}

			});

			// Add category drop down choice
			List<ClassifierItem> classifierItems = blogFacade.getBlogCategories();
			add(new ClassifierDropDownChoice("category", new PropertyModel<ClassifierItem>(getModel(), "category"), classifierItems));

			// Add image album management panel
			albumAdministrationPanel = addImageAlbumManagementPanel(getModelObject());
		}

		@Override
		protected void onSubmit() {
			super.onSubmit();
			BlogEntry entry = getModelObject();
			try {
				boolean deleteThumbnail = !thumbnailManagementPanel.getImagesToDelete().isEmpty();
				Image thumbnailToAdd = (!thumbnailManagementPanel.getImagesToAdd().isEmpty()) ? thumbnailManagementPanel.getImagesToAdd().get(0)
					: null;
				blogFacade.storeBlogEntry(entry, thumbnailToAdd, deleteThumbnail, getPluginBean(), albumAdministrationPanel.getImagesToDelete(),
					albumAdministrationPanel.getImagesToAdd());
			} catch (Exception e) {
				e.printStackTrace();
				error("Could not save data.please, try again: " + e.getMessage());
			}
			String url = UrlUtils.generateFullvalue(getBlogEntryListPageClass());
			throw new RedirectToUrlException(url);
		}

		private void addBlogEntryImagePanel() {
			List<Image> thumbnailImages = new ArrayList<Image>();
			FileDescriptor thumbnail = getModelObject().getThumbnail();
			if (thumbnail != null) {
				Image image = blogFacade.newImage();
				image.setId(thumbnail.getId());
				image.setPath(thumbnail.getPath());
				thumbnailImages.add(image);
			}
			thumbnailManagementPanel = new AlbumAdministrationPanel("image-administration-panel", thumbnailImages);
			thumbnailManagementPanel.setMaxImagesAllowed(1);
			thumbnailManagementPanel.setImageThumbnailWidth(getPluginBean().getBlogImageWidth());
			thumbnailManagementPanel.setImageThumbnailHeight(getPluginBean().getBlogImageHeight());
			thumbnailManagementPanel.setVisible(securityFacade.hasAny(GallerySecurityAuthorities.IMAGE_EDIT));
			add(thumbnailManagementPanel);
		}

		private AlbumAdministrationPanel addImageAlbumManagementPanel(BlogEntry album) {
			List<Image> albumImages = albumFacade.getImagesByAlbum(album);

			AlbumAdministrationPanel albumAdministrationPanel = new AlbumAdministrationPanel("images-administration", albumImages);
			albumAdministrationPanel.setImageThumbnailWidth(getPluginBean().getBlogImageWidth());
			albumAdministrationPanel.setImageThumbnailHeight(getPluginBean().getBlogImageHeight());
			albumAdministrationPanel.setVisible(securityFacade.hasAny(GallerySecurityAuthorities.IMAGE_EDIT));
			add(albumAdministrationPanel);
			return albumAdministrationPanel;
		}
	}
}
