package py.una.med.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import org.richfaces.component.UIPanelMenu;
import org.richfaces.component.UIPanelMenuGroup;
import org.richfaces.component.UIPanelMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.breadcrumb.BreadcrumbController;
import py.una.med.base.domain.Menu;
import py.una.med.base.domain.Menu.Menus;
import py.una.med.base.dynamic.forms.SIGHComponentFactory;
import py.una.med.base.util.I18nHelper;

/**
 * Clase que implementa la creacion del menu de la aplicacion.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 20, 2013
 * 
 */
@SessionScoped
@Controller
@ManagedBean
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class MenuBean {

	private UIPanelMenu menupanel;
	// private final Logger logger = LoggerFactory.getLogger(MenuBean.class);

	@Autowired
	private Menus menus;

	@Autowired
	private I18nHelper helper;

	private HashMap<String, Boolean> expanded;

	/**
	 * Configura y retorna un menu
	 * 
	 * @return Menu entero de la aplicacion
	 */
	public UIPanelMenu getMenu() {

		// XXX Ver como generar el menupanel UNA sola vez.

		menupanel = SIGHComponentFactory.getMenu();
		menupanel.setGroupExpandedLeftIcon("triangleUp");
		menupanel.setGroupCollapsedLeftIcon("triangleDown");
		menupanel.setTopGroupExpandedRightIcon("chevronUp");
		menupanel.setTopGroupCollapsedRightIcon("chevronDown");
		menupanel.setItemLeftIcon("disc");
		menupanel.getChildren().clear();
		menupanel.getChildren().addAll(buildMenu());
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

		List<UIComponent> menuGroups = new ArrayList<UIComponent>(
				menus.menus.size());

		for (Menu menu : menus.menus) {
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
		menuGroup.setId(menu.getIdFather() + menu.getId());
		menuGroup.setLabel(helper.getString(menu.getName()));
		menuGroup.setExpanded(false);
		for (Menu children : menu.getChildrens()) {
			UIComponent component = getComponent(children);
			if (component != null) {
				menuGroup.getChildren().add(component);
			}
		}

		if (menuGroup.getChildren().isEmpty()) {
			return null;
		}

		return menuGroup;
	}

	private UIComponent getSingleMenu(Menu menu) {

		UIPanelMenuItem item = SIGHComponentFactory.getMenuItem();
		// XXX ver mejor forma de validar esto
		if (menu.getUrl() != null && !menu.getUrl().startsWith("/v")) {
			menu.setUrl("/views/" + menu.getUrl());
		}
		// /
		item.setId(menu.getIdFather() + menu.getId());
		// item.setLeftIcon(mod.getIconURI());
		item.setLabel(helper.getString(menu.getName()));
		HtmlOutcomeTargetLink link = SIGHComponentFactory.getLink();
		if (menu.getUrl() != null) {
			link.setOutcome(menu.getUrl() + BreadcrumbController.BREADCRUM_URL);
		}
		// link.setValue(children.getName());
		link.getChildren().add(item);
		// menuGroup.getChildren().add(link);
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

	/**
	 * Retorna si un menu dado debe estar expandido
	 * 
	 * @return lista de menus, con su corresopndiente debe estar o no expandido
	 */
	public HashMap<String, Boolean> getExpanded() {

		return expanded;
	}

	/**
	 * 
	 * @param expanded
	 */
	public void setExpanded(HashMap<String, Boolean> expanded) {

		this.expanded = expanded;
	}
}
