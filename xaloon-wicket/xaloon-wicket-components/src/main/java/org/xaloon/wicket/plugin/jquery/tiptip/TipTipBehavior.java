package org.xaloon.wicket.plugin.jquery.tiptip;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractAjaxBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;

/**
 * @author vytautas.r
 */
public class TipTipBehavior extends WiQueryAbstractAjaxBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param opt
	 */
	public TipTipBehavior(TipTipOptions opt) {
		options = opt.getOptions();
	}

	@Override
	public void renderHead(Component arg0, IHeaderResponse response) {
		super.renderHead(arg0, response);
		response.render(JavaScriptHeaderItem.forReference(TipTipOptions.JS_RESOURCE_MIN));
		response.render(CssHeaderItem.forReference(TipTipOptions.CSS_RESOURCE));
		response.render(OnDomReadyHeaderItem.forScript(new JsQuery(getComponent()).$().chain("tipTip", options.getJavaScriptOptions()).render()));
	}
}
