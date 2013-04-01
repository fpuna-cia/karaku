/*
 * 
 * @BreadCrumController.java 14/02/13 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.breadcrumb;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.controller.ISIGHBaseController;
import py.una.med.base.domain.Menu;
import py.una.med.base.domain.Menu.Menus;
import py.una.med.base.util.I18nHelper;

/**
 * Controlador que se encarga de manipular la vista del breadcrum
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1
 * 
 */
@Controller
@Scope(value = SIGHConfiguration.SCOPE_CONVERSATION)
@ManagedBean
public class BreadcrumbController {

	/**
	 * Variable que es agregada a la URL de todas las peticiones que se hacen al
	 * sistema que deben resetear la consulta
	 */
	public static final String BREADCRUM_VARIABLE = "breadcrum_reset";

	/**
	 * Path listo para agregar a la URL de tal forma a que el
	 * {@link BreadcrumbController} y {@link BCPhaseListener} reinicien la miga
	 * de pan
	 */
	public static final String BREADCRUM_URL = "?breadcrum_reset=true";

	@Autowired
	Menus menus;

	List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();

	ISIGHBaseController<?, ?> actualController;

	/**
	 * Agrega un registro a la miga de pan.
	 * 
	 * @param menu
	 *            item del arbol de menus a agregar
	 * @param first
	 *            si se desea agregar como primer elemento, en caso de ser
	 *            false, se agrega como ultimo
	 */
	public void addBreadcrum(final Menu menu, final boolean first) {

		if (first) {
			items.add(0, getBreadcrumItem(menu));
		} else {
			items.add(getBreadcrumItem(menu));
		}
	}

	/**
	 * Dado una URI se encarga de buscar el menu en el arbol de menus y luego
	 * agrega su rastro a la lista de items
	 * 
	 * @param uri
	 */
	public void addBreadcrum(final String uri) {

		boolean find = true;
		List<BreadcrumbItem> toRemove = new ArrayList<BreadcrumbItem>();
		BreadcrumbItem nuevo = new BreadcrumbItem(uri, null);
		for (BreadcrumbItem item : items) {
			if (item.equals(nuevo)) {
				find = false;
				nuevo = item;
			} else {
				if (find == false) {
					toRemove.add(item);
				}
			}
		}
		if (find) {
			nuevo = configure(nuevo);
			items.add(nuevo);
		} else {
			items.removeAll(toRemove);
		}
	}

	/**
	 * Limpia y configura el breadcrum, para ello busca en la URL y verifica si
	 * hay que agregar o crear de 0 la miga de pan
	 */
	public void initialize() {

		// System.out.println("BreadcrumbController.initialize()");
		FacesContext fc = FacesContext.getCurrentInstance();
		String url = fc.getViewRoot().getViewId();
		if ("true".equals(fc.getExternalContext().getRequestParameterMap()
				.get(BreadcrumbController.BREADCRUM_VARIABLE))) {
			clearBreadcrum();
			setBreadcrum(url);
		} else {
			addBreadcrum(url);
		}
	}

	/**
	 * Elimina todos los items del breadcrum y vuelve al estado inicial
	 */
	public void clearBreadcrum() {

		items = new ArrayList<BreadcrumbItem>();
	}

	/**
	 * Carga los valores internacionalizados
	 * 
	 * @return
	 */
	private BreadcrumbItem configure(final BreadcrumbItem item) {

		Menu m = menus.getMenuByUrl(item.getUri());
		if (m != null) {
			item.setName(I18nHelper.getMessage(m.getName()));
		} else {
			if (actualController != null) {
				item.setName(getModeMessage());
			} else {
				item.setName(item.getUri());
			}
		}
		return item;
	}

	/**
	 * Obtiene valor i18n del modo del controlador
	 * 
	 * @return String
	 */
	private String getModeMessage() {
		String message = "";
		switch (actualController.getMode()) {
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

		return new BreadcrumbItem(menu.getUrl(), I18nHelper.getMessage(menu
				.getName()));
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
		// System.out.println("BreadcrumbController.getItems()");
		// for (BreadcrumbItem item : items) {
		// System.out.println(item.getName());
		// }
		return items;
	}

	/**
	 * Determina que controller es el que se esta encargando de la pagina a ser
	 * mostrada en el momento, esto es util para mostrar el estado de este
	 * dentro de la miga de pan
	 * 
	 * 
	 * @param controller
	 */
	public void setActualController(final ISIGHBaseController<?, ?> controller) {

		actualController = controller;

	}

	/**
	 * Cambia la configuracion actual del breadcrumb para reflejar el URI
	 * pasado.
	 * 
	 * @param uri
	 *            nueva ubicacion
	 */
	public void setBreadcrum(final String uri) {

		Menu m = menus.getMenuByUrl(uri);
		do {
			addBreadcrum(m, true);
			m = menus.getPadre(m);
		} while (m != null);
	}

	/**
	 * Reemplaza la lista de items.
	 * 
	 * @param items
	 *            nuevo breadcrumb
	 */
	public void setItems(final List<BreadcrumbItem> items) {

		this.items = items;
	}
}
