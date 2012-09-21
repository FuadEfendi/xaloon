package org.xaloon.wicket.plugin.jquery.tiptip;

import java.io.Serializable;

import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.odlabs.wiquery.core.options.Options;

/**
 * @author vytautas.r
 */
public class TipTipOptions implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final JavaScriptResourceReference JS_RESOURCE = new JavaScriptResourceReference(TipTipOptions.class, "jquery.tipTip.js");

	/**
	 * 
	 */
	public static final JavaScriptResourceReference JS_RESOURCE_MIN = new JavaScriptResourceReference(TipTipOptions.class,
		"jquery.tipTip.minified.js");

	/**
	 * 
	 */
	public static final CssResourceReference CSS_RESOURCE = new CssResourceReference(TipTipOptions.class, "tipTip.css");

	Options options;

	/**
	 * Construct.
	 */
	public TipTipOptions() {
		options = new Options();
	}

	/**
	 * @param content
	 * @return
	 */
	public TipTipOptions content(String content) {
		options.putLiteral("content", content);
		return this;
	}

	/**
	 * @param width
	 * @return
	 */
	public TipTipOptions maxWidth(String width) {
		options.putLiteral("maxWidth", width);
		return this;
	}

	/**
	 * @param position
	 * @return
	 */
	public TipTipOptions defaultPosition(String position) {
		options.putLiteral("defaultPosition", position);
		return this;
	}

	/**
	 * @return
	 */
	public Options getOptions() {
		return options;
	}
}
