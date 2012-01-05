#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.page;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.mount.annotation.MountPage;

import ${package}.panel.IndexPanel;

@MountPage(value="/index", order=10)
public class IndexPage extends LayoutWebPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		return new IndexPanel(id);
	}
}