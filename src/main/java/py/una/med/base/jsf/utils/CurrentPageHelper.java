/*
 * @PageHelper.java 1.0 Aug 23, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.jsf.utils;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.controller.MenuBean;
import py.una.med.base.domain.Menu;
import py.una.med.base.dynamic.forms.SIGHComponentFactory;
import py.una.med.base.util.MenuHelper;

/**
 * Componente que se encarga de proveer funcionalidades básicas para la
 * manipulación de la vista actual.
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 23, 2013
 *
 */

@Component
public class CurrentPageHelper {

	@Autowired
	private MenuHelper menuHelper;

	private Menu menu;

	/**
	 * Método que se encarga de inicializar este componente, debe ser invocado
	 * al inicio de cada página, no es un {@link FacesListener} por qué debe ser
	 * un componente de Spring.
	 */
	public String initialize() {

		FacesContext fc = FacesContext.getCurrentInstance();
		String url = fc.getViewRoot().getViewId();
		setMenu(menuHelper.getMenuByUrl(url));
		return null;
	}

	/**
	 * <b>WorkAround</b> para permitir que esto sea lo primero que se ejecuta a
	 * la hora de mostrar una página.
	 * <p>
	 * Gracias a este método, se ejecuta la acción {@link #initialize()} antes
	 * de que ocurra cualquier otro <code>bind</code> con algún componente, como
	 * ocurre con {@link MenuBean}.
	 * </p>
	 *
	 * @return dummy {@link HtmlOutputText}
	 */
	public HtmlOutputText getBind() {

		// XXX
		HtmlOutputText bind = SIGHComponentFactory.getHtmlOutputText();
		initialize();
		return bind;
	}

	/**
	 * <b>WorkAround</b> para permitir que esto sea lo primero que se ejecuta a
	 * la hora de mostrar una página.
	 * <p>
	 *
	 * @see #getBind()
	 */
	public void setBind(HtmlOutputText input) {

	}

	/**
	 * @param menu
	 *            menu para setear
	 */
	private void setMenu(Menu menu) {

		this.menu = menu;
	}

	/**
	 * Retorna el menú que actualmente esta seleccionado, si el mismo no se
	 * puede detectar, se retorna <code>null</code>
	 *
	 * @return menu actual elemento seleccionado.
	 */
	public Menu getCurrentMenu() {

		if (menu == null) {
			initialize();
		}
		return menu;
	}

}
