#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.panel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class IndexPanel extends Panel {
	public IndexPanel(String id) {
		super(id);
		add(new FeedbackPanel("feedback"));
	}
}
