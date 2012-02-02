package org.xaloon.wicket.plugin.image.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.xaloon.core.api.image.model.Image;

public abstract class ImageDescriptionPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageDescriptionPanel(String id, IModel<Image> model) {
		super(id, model);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		removeAll();

		Form<Image> imageForm = new Form<Image>("image-form", new CompoundPropertyModel<Image>((Image) getDefaultModelObject()));
		imageForm.setMultiPart(true);
		add(imageForm);

		// Add feedback panel
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		imageForm.add(feedbackPanel);

		// Add image title
		imageForm.add(new TextField<String>("title"));
				
		// Add image description
		imageForm.add(new TextArea<String>("description"));
		
		// Add submit button
		imageForm.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Image entity = (Image) form.getModelObject();
				onImageUpdate(target, entity);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		});
	}

	protected abstract void onImageUpdate(AjaxRequestTarget target, Image entity);
}
