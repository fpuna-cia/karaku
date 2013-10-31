/*
 *
 * @BreadCrumController.java 14/02/13 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.breadcrumb;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.controller.ISIGHBaseController;
import py.una.med.base.jsf.utils.ICurrentpageHelper;
import py.una.med.base.menu.client.MenuHelper;
import py.una.med.base.menu.schemas.Menu;
import py.una.med.base.util.I18nHelper;

/**
 * Controlador que se encarga de manipular la vista del breadcrum
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1
 *
 */
@Component
@Scope(value = SIGHConfiguration.SCOPE_CONVERSATION)
public class BreadcrumbController {

	@Autowired
	private ICurrentpageHelper currentPageHelper;

	@Autowired
	private MenuHelper menuHelper;

	private List<BreadcrumbItem> items;

	private ISIGHBaseController<?, ?> actualController;

	/**
	 * Limpia y configura el breadcrum, para ello busca en la URL y verifica si
	 * hay que agregar o crear de 0 la miga de pan
	 */
	private void initialize() {

		Menu current = currentPageHelper.getCurrentMenu();
		items = new ArrayList<BreadcrumbItem>();
		while (current != null) {

			items.add(0, getBreadcrumItem(current));
			current = menuHelper.getFather(current);
		}

		if (actualController != null) {

			FacesContext fc = FacesContext.getCurrentInstance();
			String url = fc.getViewRoot().getViewId();
			if (!url.contains("/list.xhtml")) {
				BreadcrumbItem last = getLastBCI();
				items.add(last);
			}
		}
	}

	/**
	 *
	 */
	private BreadcrumbItem getLastBCI() {

		String last = getModeMessage();
		return new BreadcrumbItem(null, last);
	}

	/**
	 * Obtiene valor i18n del modo del controlador
	 * <p>
	 * Los valores soportados son:
	 * <ul>
	 * <li>LIST:</li>
	 * <li>EDIT: BREADCRUMB_EDIT</li>
	 * <li>VIEW: BREADCRUM_VIEW</li>
	 * <li>DELETE: BREADCRUM_DELETE</li>
	 * <li>NEW: BREADCRUM_CREATE</li>
	 * <li>DEFAULT: BREADCRUM_UNKNOWN</li>
	 * </ul>
	 * </p>
	 *
	 * @return String
	 */
	private String getModeMessage() {

		String message = "";
		switch (actualController.getMode()) {
			case LIST: {
				return "";
			}
			case EDIT: {
				message = "BREADCRUM_EDIT";
				break;
			}
			case VIEW: {
				message = "BREADCRUM_VIEW";
				break;
			}
			case DELETE: {
				message = "BREADCRUM_DELETE";
				break;
			}
			case NEW: {
				message = "BREADCRUM_CREATE";
				break;
			}
			default: {
				message = "BREADCRUM_UNKNOWN";
			}
		}
		return I18nHelper.getMessage(message);
	}

	/**
	 * Dado un item del menu, genera su miga de pan para ser agregada a la lista
	 * de items
	 *
	 * @param menu
	 * @return {@link BreadcrumbItem} configurado con la url y el mensaje ya
	 *         desInternacionailzado
	 */
	public BreadcrumbItem getBreadcrumItem(final Menu menu) {

		return new BreadcrumbItem(menu.getUrl(), menu.getName());
	}

	/**
	 * Configura el breadcrumb controller y retorna la lista de items, este es
	 * el metodo de entrada a este controlador, ningun otro metodo debe ser
	 * llamado
	 *
	 * @return lista de items que forman la miga de pan actual
	 */
	public List<BreadcrumbItem> getItems() {

		initialize();
		return items;
	}

	/**
	 * Determina que controller es el que se esta encargando de la página a ser
	 * mostrada en el momento, esto es util para mostrar el estado de este
	 * dentro de la miga de pan
	 *
	 *
	 * @param controller
	 */
	public void setActualController(final ISIGHBaseController<?, ?> controller) {

		actualController = controller;

	}

}
