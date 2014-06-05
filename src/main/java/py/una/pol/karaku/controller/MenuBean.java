/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;
import org.richfaces.component.UIPanelMenu;
import org.richfaces.component.UIPanelMenuGroup;
import org.richfaces.component.UIPanelMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.jsf.utils.ICurrentpageHelper;
import py.una.pol.karaku.menu.client.AbstractMenuProvider;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.security.AuthorityController;

/**
 * Clase que implementa la creación del menu de la aplicación.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 20, 2013
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MenuBean {

	private UIPanelMenu menupanel;

	@Autowired
	private PropertiesUtil properties;

	@Autowired
	private ICurrentpageHelper currentPageHelper;

	@Autowired
	private AbstractMenuProvider menuProvider;

	/**
	 * Configura y retorna un menu
	 * 
	 * @return Menu entero de la aplicación
	 */
	public UIPanelMenu getMenu() {

		menupanel = KarakuComponentFactory.getMenu();
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

		List<UIComponent> menuGroups = new ArrayList<UIComponent>();

		for (Menu menu : menuProvider.getMenu()) {
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

		if ((menu.getItems() == null) || (menu.getItems().isEmpty())) {
			return getSingleMenu(menu);
		}
		return getMultipleMenu(menu);
	}

	private UIComponent getMultipleMenu(Menu menu) {

		UIPanelMenuGroup menuGroup = KarakuComponentFactory.getMenuGroup();
		menuGroup.setLabel(menu.getName());

		for (Menu children : menu.getItems()) {
			UIComponent component = getComponent(children);
			if (component != null) {
				menuGroup.getChildren().add(component);
				if ((getCurrentMenuSelected() != null)
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

		UIPanelMenuItem item = KarakuComponentFactory.getMenuItem();
		// Con este código se consigue que solo el menu actualmente seleccionado
		// tenga un ID legible, así se puede marcar como seleccionado.
		if ((getCurrentMenuSelected() != null)
				&& (menu.getId().equals(getCurrentMenuSelected().getId()))) {
			item.setId(menu.getId());
			item.setName(menu.getId());
		}

		item.setLabel(menu.getName());
		UIComponent link;

		if (menu.getUrl() != null) {
			String menuUrl = menu.getUrl().trim();
			String appPlaceHolder = properties
					.get("application.appUrlPlaceHolder");

			if (menuUrl.startsWith(appPlaceHolder)
					|| menuUrl.startsWith("/view")) {
				// link correspondiente a este sistema
				menuUrl = menuUrl.replace(appPlaceHolder, "");
				link = KarakuComponentFactory.getLink();
				((HtmlOutcomeTargetLink) link).setOutcome(menuUrl);
			} else {
				// link a otro sistema

				link = FacesContext
						.getCurrentInstance()
						.getApplication()
						.createComponent(FacesContext.getCurrentInstance(),
								HtmlOutputLink.COMPONENT_TYPE,
								"javax.faces.Link");
				((HtmlOutputLink) link).setValue(menuUrl);
			}
		} else {
			link = KarakuComponentFactory.getLink();
		}

		link.getChildren().add(item);
		return link;
	}

	private boolean isVisible(Menu menu) {

		if (menu == null) {
			return false;
		}

		if (menu.getPermission() == null) {
			return true;
		}

		if (AuthorityController.hasRoleStatic(menu.getPermission())) {
			return true;
		}

		return false;
	}

	private Menu getCurrentMenuSelected() {

		return currentPageHelper.getCurrentMenu();
	}

}
