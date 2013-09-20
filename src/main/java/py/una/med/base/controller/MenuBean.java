package py.una.med.base.controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;
import org.richfaces.component.UIPanelMenu;
import org.richfaces.component.UIPanelMenuGroup;
import org.richfaces.component.UIPanelMenuItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.domain.Menu;
import py.una.med.base.dynamic.forms.SIGHComponentFactory;
import py.una.med.base.jsf.utils.CurrentPageHelper;
import py.una.med.base.log.Log;
import py.una.med.base.util.HostResolver;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.MenuHelper;

/**
 * Clase que implementa la creacion del menu de la aplicacion.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 20, 2013
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MenuBean {

	private UIPanelMenu menupanel;

	@Log
	private Logger log;

	@Autowired
	private I18nHelper helper;

	@Autowired
	private PropertiesUtil properties;

	@Autowired
	private HostResolver hostResolver;

	@Autowired
	private CurrentPageHelper currentPageHelper;

	@Autowired
	private MenuHelper menuHelper;

	/**
	 * Configura y retorna un menu
	 * 
	 * @return Menu entero de la aplicación
	 */
	public UIPanelMenu getMenu() {

		menupanel = SIGHComponentFactory.getMenu();
		menupanel.setGroupExpandedLeftIcon("triangleUp");
		menupanel.setGroupCollapsedLeftIcon("triangleDown");
		menupanel.setTopGroupExpandedRightIcon("chevronUp");
		menupanel.setTopGroupCollapsedRightIcon("chevronDown");
		menupanel.setItemLeftIcon("disc");
		menupanel.setStyleClass("menu");
		menupanel.getChildren().clear();
		menupanel.getChildren().addAll(buildMenu());

		if (getCurrentMenuSelected() != null) {
			menupanel.setActiveItem(getCurrentMenuSelected().getId());
		}
		return menupanel;
	}

	/**
	 * Si se llama a esta funcion algo esta mal, se utiliza solamente para que
	 * "menu" sea una atributo de menuBean
	 * 
	 * @param obj
	 */
	public void setMenu(UIPanelMenu menupanel) {

		this.menupanel = menupanel;
	}

	private List<UIComponent> buildMenu() {

		List<UIComponent> menuGroups = new ArrayList<UIComponent>(menuHelper
				.getMenus().getMenus().size());

		for (Menu menu : menuHelper.getMenus().getMenus()) {
			UIComponent component = getComponent(menu);
			if (component != null) {
				menuGroups.add(component);
			}
		}
		return menuGroups;

	}

	private UIComponent getComponent(Menu menu) {

		if (!isVisible(menu)) {
			return null;
		}

		if (menu.getChildrens() == null || menu.getChildrens().size() == 0) {
			return getSingleMenu(menu);
		} else {
			return getMultipleMenu(menu);
		}
	}

	private UIComponent getMultipleMenu(Menu menu) {

		UIPanelMenuGroup menuGroup = SIGHComponentFactory.getMenuGroup();
		menuGroup.setId(menu.getId());
		menuGroup.setName(menu.getId());
		menuGroup.setLabel(helper.getString(menu.getName()));

		for (Menu children : menu.getChildrens()) {
			UIComponent component = getComponent(children);
			if (component != null) {
				menuGroup.getChildren().add(component);
				if (getCurrentMenuSelected() != null
						&& children.equals(getCurrentMenuSelected())) {
					menuGroup.setExpanded(true);
				}
				if (component instanceof UIPanelMenuGroup) {
					UIPanelMenuGroup child = (UIPanelMenuGroup) component;
					if (child.isExpanded() != null) {
						menuGroup.setExpanded(child.isExpanded());
					}
				}
			}
		}

		if (menuGroup.getChildren().isEmpty()) {
			return null;
		}

		return menuGroup;
	}

	private UIComponent getSingleMenu(Menu menu) {

		UIPanelMenuItem item = SIGHComponentFactory.getMenuItem();
		item.setId(menu.getId());
		item.setName(menu.getId());

		item.setLabel(helper.getString(menu.getName()));
		UIComponent link;

		if (menu.getUrl() != null) {
			String menuUrl = menu.getUrl().trim();
			String appPlaceHolder = properties
					.getProperty("application.appUrlPlaceHolder");

			if (menuUrl.startsWith(appPlaceHolder)
					|| menuUrl.startsWith("/view")) {
				// link correspondiente a este sistema
				menuUrl = menuUrl.replace(appPlaceHolder, "");
				link = SIGHComponentFactory.getLink();
				((HtmlOutcomeTargetLink) link).setOutcome(menuUrl);
			} else {
				// link a otro sistema
				String urlPlaceHolder = menuUrl.substring(0,
						menuUrl.indexOf("/"));

				menuUrl = menuUrl.replace(urlPlaceHolder,
						hostResolver.getSystemURL(urlPlaceHolder));
				link = FacesContext
						.getCurrentInstance()
						.getApplication()
						.createComponent(FacesContext.getCurrentInstance(),
								HtmlOutputLink.COMPONENT_TYPE,
								"javax.faces.Link");
				((HtmlOutputLink) link).setValue(menuUrl);
			}
		} else {
			link = SIGHComponentFactory.getLink();
		}

		link.getChildren().add(item);
		return link;
	}

	private boolean isVisible(Menu menu) {

		if (menu == null) {
			return false;
		}

		if (menu.getPermissions() == null) {
			return true;
		}

		for (String permission : menu.getPermissions()) {
			if (AuthorityController.hasRoleStatic(permission)) {
				return true;
			}
		}

		return false;
	}

	private Menu getCurrentMenuSelected() {

		return currentPageHelper.getCurrentMenu();
	}

}
