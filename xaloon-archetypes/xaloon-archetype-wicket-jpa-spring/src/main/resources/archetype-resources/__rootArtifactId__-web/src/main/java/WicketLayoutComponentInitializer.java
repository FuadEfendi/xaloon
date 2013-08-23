#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.xaloon.core.api.tree.TreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.application.page.LayoutComponentInitializer;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.plugin.addthis.panel.AddThisPanel;
import org.xaloon.wicket.plugin.google.GoogleWebMasterMarkupContainer;
import org.xaloon.wicket.plugin.google.panel.GoogleAnalyticsPanel;
import org.xaloon.wicket.plugin.menu.DynamicMenuFacade;
import org.xaloon.wicket.plugin.menu.MenuContainer;
import org.xaloon.wicket.plugin.menu.panel.DynamicMenuItemPanel;
import org.xaloon.wicket.plugin.user.panel.HeaderPanel;
import org.apache.wicket.model.Model;

@Named
public class WicketLayoutComponentInitializer implements LayoutComponentInitializer {
	private static final long serialVersionUID = 1L;

	private static final String WICKET_ID_SIDEBAR = "sidebar";

	@Inject
	private DynamicMenuFacade dynamicMenuFacade;
	
	@Override
	public void onBeforeRender(LayoutWebPage layoutWebPage) {
		if (layoutWebPage.get(WICKET_ID_SIDEBAR) != null) {
			layoutWebPage.remove(WICKET_ID_SIDEBAR);
		}

		// Take parent of this menu item as we want to display all menu of current level
		TreeNode<MenuItem> parentMenuTreeItem = dynamicMenuFacade.getParent(layoutWebPage.getClass());
		if (parentMenuTreeItem == null) {
			layoutWebPage.add(new Label(WICKET_ID_SIDEBAR, new Model<String>("")).setVisible(false));
			return;
		}

		// Do not duplicate root menu
		if (parentMenuTreeItem.getParent() != null && parentMenuTreeItem.hasMoreThanOneChildren()) {
			layoutWebPage.add(new DynamicMenuItemPanel(WICKET_ID_SIDEBAR, parentMenuTreeItem.getChildren()).setUseMenuDelimiter(false));
		} else {
			layoutWebPage.add(new Label(WICKET_ID_SIDEBAR, new Model<String>("")).setVisible(false));
		}
	}

	@Override
	public void onInitialize(LayoutWebPage layoutWebPage, Panel content) {
		layoutWebPage.add(new HeaderPanel("header-panel"));
				
		layoutWebPage.add (new GoogleWebMasterMarkupContainer("webmaster-keys"));
		
		//add dynamic menu
		layoutWebPage.add(new MenuContainer("dynamic-menu-container"));
				
		//addthis panel
		layoutWebPage.add(new AddThisPanel("add-this-panel"));
		
		//add google analytics panel
		layoutWebPage.add(new GoogleAnalyticsPanel("google-analytics-panel"));
		
		if (hasSidebarMenu(layoutWebPage)) {
			content.add(new AttributeModifier("class", "content_sidebar"));
		}
	}
	
	protected boolean hasSidebarMenu(LayoutWebPage layoutWebPage) {
		TreeNode<MenuItem> parentMenuItem = dynamicMenuFacade.getParent(layoutWebPage.getClass());
		return (parentMenuItem != null && parentMenuItem.getParent() != null && parentMenuItem.hasMoreThanOneChildren());
	}	
}
