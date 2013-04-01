/*
 * @SIGHComponentFactory.java 1.0 Feb 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import javax.faces.application.Application;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UIColumn;
import org.richfaces.component.UICommandButton;
import org.richfaces.component.UICommandLink;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.component.UIPanelMenu;
import org.richfaces.component.UIPanelMenuGroup;
import org.richfaces.component.UIPanelMenuItem;
import org.richfaces.component.behavior.ComponentControlBehavior;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */

public class SIGHComponentFactory {

	public static HtmlSelectOneMenu getNewSelectOneMenu() {

		return (HtmlSelectOneMenu) getApplication().createComponent(
				getContext(), HtmlSelectOneMenu.COMPONENT_TYPE,
				"javax.faces.Menu");
	}

	/**
	 * @return
	 */
	private static FacesContext getContext() {

		return FacesContext.getCurrentInstance();
	}

	public static UINamingContainer getUiNamingContainer() {

		return (UINamingContainer) getApplication().createComponent(
				getContext(), UINamingContainer.COMPONENT_TYPE, null);
	}

	public static HtmlInputText getHtmlInputText() {

		return (HtmlInputText) getApplication().createComponent(getContext(),
				HtmlInputText.COMPONENT_TYPE, "javax.faces.Text");
	}

	public static HtmlOutputText getHtmlOutputText() {

		return (HtmlOutputText) getApplication().createComponent(getContext(),
				HtmlOutputText.COMPONENT_TYPE, "javax.faces.Text");
	}

	public static UICalendar getCalendar() {

		return (UICalendar) getApplication().createComponent(getContext(),
				UICalendar.COMPONENT_TYPE, "org.richfaces.CalendarRenderer");
	}

	/**
	 * @return
	 */
	private static Application getApplication() {

		return getContext().getApplication();
	}

	/**
	 * @return
	 */
	public static UISelectItem getNewSelectItem() {

		return (UISelectItem) getApplication().createComponent(getContext(),
				UISelectItem.COMPONENT_TYPE, null);
	}

	/**
	 * 
	 * @return
	 */
	public static UISelectItems getNewSelectItems() {

		return (UISelectItems) getApplication().createComponent(getContext(),
				UISelectItems.COMPONENT_TYPE, null);
	}

	/**
	 * @return
	 */
	public static HtmlInputText getNewTextField() {

		return (HtmlInputText) getApplication().createComponent(getContext(),
				HtmlInputText.COMPONENT_TYPE, "javax.faces.Text");
	}

	public static ComponentControlBehavior getComponentControl() {

		return new ComponentControlBehavior();
	}

	public static UICommandButton getAjaxCommandButton() {

		return (UICommandButton) getApplication().createComponent(getContext(),
				UICommandButton.COMPONENT_TYPE,
				"org.richfaces.CommandButtonRenderer");
	}

	public static HtmlCommandButton getCommandButton() {

		return (HtmlCommandButton) getApplication().createComponent(
				getContext(), HtmlCommandButton.COMPONENT_TYPE,
				"javax.faces.Button");
	}

	public static UICommandLink getAjaxCommandLink() {

		return (UICommandLink) getApplication().createComponent(getContext(),
				UICommandLink.COMPONENT_TYPE,
				"org.richfaces.CommandLinkRenderer");
	}

	public static UIColumn getRichColumn() {

		return (UIColumn) getApplication().createComponent(getContext(),
				UIColumn.COMPONENT_TYPE, null);
	}

	public static UIExtendedDataTable getDataTable() {

		return (UIExtendedDataTable) getApplication().createComponent(
				getContext(), UIExtendedDataTable.COMPONENT_TYPE,
				"org.richfaces.ExtendedDataTableRenderer");
	}

	public static UIPanelMenuGroup getMenuGroup() {

		return (UIPanelMenuGroup) getApplication().createComponent(
				getContext(), UIPanelMenuGroup.COMPONENT_TYPE,
				"org.richfaces.PanelMenuGroupRenderer");
	}

	public static UIPanelMenuItem getMenuItem() {

		return (UIPanelMenuItem) getApplication().createComponent(getContext(),
				UIPanelMenuItem.COMPONENT_TYPE,
				"org.richfaces.PanelMenuItemRenderer");
	}

	public static UIPanelMenu getMenu() {

		return (UIPanelMenu) getApplication().createComponent(getContext(),
				UIPanelMenu.COMPONENT_TYPE, "org.richfaces.PanelMenuRenderer");
	}

	public static HtmlOutcomeTargetLink getLink() {

		return (HtmlOutcomeTargetLink) getApplication().createComponent(
				getContext(), HtmlOutcomeTargetLink.COMPONENT_TYPE,
				"javax.faces.Link");
	}
}
