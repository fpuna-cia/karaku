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
package py.una.pol.karaku.dynamic.forms;

import javax.annotation.Nonnull;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.component.html.HtmlSelectOneRadio;
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
import org.richfaces.component.behavior.ToggleControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public final class KarakuComponentFactory {

	private static final String JAVAX_FACES_TEXT = "javax.faces.Text";
	private static final Logger LOG = LoggerFactory
			.getLogger(KarakuComponentFactory.class);

	private KarakuComponentFactory() {

		// No-op
	}

	public static HtmlSelectOneMenu getNewSelectOneMenu() {

		return getComponent(HtmlSelectOneMenu.class,
				HtmlSelectOneMenu.COMPONENT_TYPE, "java.faces.Menu");
	}

	public static UINamingContainer getUiNamingContainer() {

		return getComponent(UINamingContainer.class,
				UINamingContainer.COMPONENT_TYPE, null);
	}

	public static HtmlInputText getHtmlInputText() {

		return getComponent(HtmlInputText.class, HtmlInputText.COMPONENT_TYPE,
				JAVAX_FACES_TEXT);
	}

	public static HtmlOutputText getHtmlOutputText() {

		return getComponent(HtmlOutputText.class,
				HtmlOutputText.COMPONENT_TYPE, JAVAX_FACES_TEXT);
	}

	public static UICalendar getCalendar() {

		return getComponent(UICalendar.class, UICalendar.COMPONENT_TYPE,
				"org.richfaces.CalendarRenderer");
	}

	/**
	 * @return
	 */
	public static UISelectItem getNewSelectItem() {

		return getComponent(UISelectItem.class, UISelectItem.COMPONENT_TYPE,
				null);
	}

	/**
	 * 
	 * @return
	 */
	public static UISelectItems getNewSelectItems() {

		return getComponent(UISelectItems.class, UISelectItems.COMPONENT_TYPE,
				null);
	}

	/**
	 * @return
	 */
	public static HtmlInputText getNewTextField() {

		return getComponent(HtmlInputText.class, HtmlInputText.COMPONENT_TYPE,
				JAVAX_FACES_TEXT);
	}

	public static UICommandButton getAjaxCommandButton() {

		return getComponent(UICommandButton.class,
				UICommandButton.COMPONENT_TYPE,
				"org.richfaces.CommandButtonRenderer");
	}

	public static HtmlSelectOneRadio getHtmlSelectOneRadio() {

		return getComponent(HtmlSelectOneRadio.class,
				HtmlSelectOneRadio.COMPONENT_TYPE, "javax.faces.Menu");
	}

	public static HtmlCommandButton getCommandButton() {

		return getComponent(HtmlCommandButton.class,
				HtmlCommandButton.COMPONENT_TYPE, "javax.faces.Button");
	}

	public static UICommandLink getAjaxCommandLink() {

		return getComponent(UICommandLink.class, UICommandLink.COMPONENT_TYPE,
				"org.richfaces.CommandLinkRenderer");
	}

	public static UIColumn getRichColumn() {

		return getComponent(UIColumn.class, UIColumn.COMPONENT_TYPE, null);
	}

	public static UIExtendedDataTable getDataTable() {

		return getComponent(UIExtendedDataTable.class,
				UIExtendedDataTable.COMPONENT_TYPE, null);
	}

	public static UIPanelMenuGroup getMenuGroup() {

		return getComponent(UIPanelMenuGroup.class,
				UIPanelMenuGroup.COMPONENT_TYPE,
				"org.richfaces.PanelMenuGroupRenderer");
	}

	public static UIPanelMenuItem getMenuItem() {

		return getComponent(UIPanelMenuItem.class,
				UIPanelMenuItem.COMPONENT_TYPE,
				"org.richfaces.PanelMenuItemRenderer");
	}

	public static UIPanelMenu getMenu() {

		return getComponent(UIPanelMenu.class, UIPanelMenu.COMPONENT_TYPE,
				"org.richfaces.PanelMenuRenderer");
	}

	public static HtmlOutcomeTargetLink getLink() {

		return getComponent(HtmlOutcomeTargetLink.class,
				HtmlOutcomeTargetLink.COMPONENT_TYPE, "javax.faces.Link");
	}

	public static AjaxBehavior getAjaxBehavior() {

		return getBehaviour(AjaxBehavior.class, AjaxBehavior.BEHAVIOR_ID);
	}

	/**
	 * Retorna una acción AJAX del tipo {@link ToggleControl}, el cual sirve
	 * para manipular paneles del tipo TogglePanelRenderer
	 * 
	 * @param id
	 *            id del toggle panel
	 * @param action
	 *            '@next', '@prev', que son acciones para manipular el toggle
	 *            panel
	 * @return {@link AjaxBehavior} especializado para tratar con toggle
	 *         controls
	 */
	public static ToggleControl getToogleControl(String id, String action) {

		if (id == null) {
			return getToogleControl(action);
		}
		ToggleControl control = getBehaviour(ToggleControl.class,
				ToggleControl.BEHAVIOR_ID);
		control.setTargetPanel(id);
		control.setTargetItem(action);
		return control;
	}

	/**
	 * Retorna una acción AJAX del tipo {@link ToggleControl}, el cual sirve
	 * para manipular paneles del tipo TogglePanelRenderer
	 * 
	 * @param action
	 *            '@next', '@prev', que son acciones para manipular el toggle
	 *            panel
	 * @return {@link AjaxBehavior} especializado para tratar con toggle
	 *         controls
	 */
	public static ToggleControl getToogleControl(String action) {

		ToggleControl control = getBehaviour(ToggleControl.class,
				ToggleControl.BEHAVIOR_ID);
		control.setTargetItem(action);

		return control;
	}

	public static ComponentControlBehavior getComponentControl(String target,
			String operation) {

		ComponentControlBehavior behavior = getBehaviour(
				ComponentControlBehavior.class,
				ComponentControlBehavior.BEHAVIOR_ID);
		behavior.setOperation(operation);
		behavior.setTarget(target);
		return behavior;
	}

	/**
	 * Crea un componente dado su clase.
	 * 
	 * <p>
	 * Se verifica que {@link #getContext()} no sea nulo, pues si se nulo se
	 * debe crear una clase sin renderer, esto se aplica a los test
	 * </p>
	 * 
	 * @param clazz
	 *            clase a crear
	 * @param type
	 *            tipo del objeto
	 * @param renderer
	 *            el renderer
	 * @return objeto completamente inicializado, nunca <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	private static <T extends UIComponent> T getComponent(Class<T> clazz,
			String type, String renderer) {

		if (getContext() == null) {
			return createSimpleClass(clazz);
		}
		return (T) getApplication().createComponent(getContext(), type,
				renderer);
	}

	/**
	 * @param clazz
	 * @return
	 */
	private static <T> T createSimpleClass(Class<T> clazz) {

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			LOG.warn("Can't create component", e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	private static <T extends ClientBehavior> T getBehaviour(Class<T> clazz,
			String id) {

		if (getContext() == null) {
			return createSimpleClass(clazz);
		}
		return (T) getApplication().createBehavior(id);
	}

	private static Application getApplication() {

		return getContext().getApplication();
	}

	/**
	 * @return
	 */
	private static FacesContext getContext() {

		return FacesContext.getCurrentInstance();
	}
}
