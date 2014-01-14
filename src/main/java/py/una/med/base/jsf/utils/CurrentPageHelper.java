/*
 * @PageHelper.java 1.0 Aug 23, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.jsf.utils;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.menu.client.MenuHelper;
import py.una.med.base.menu.schemas.Menu;

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
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
public class CurrentPageHelper implements ICurrentpageHelper {

	@Autowired
	private MenuHelper menuHelper;

	private Menu menu;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String initialize() {

		FacesContext fc = FacesContext.getCurrentInstance();
		String url = fc.getViewRoot().getViewId();
		setMenu(menuHelper.getMenuByUrl(url));
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HtmlOutputText getBind() {

		// XXX

		initialize();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBind(HtmlOutputText input) {

		// Es vacío por que lo utiliza para inicializar al objeto
	}

	/**
	 * @param menu
	 *            menu para setear
	 */
	private void setMenu(Menu menu) {

		this.menu = menu;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Menu getCurrentMenu() {

		if (menu == null) {
			initialize();
		}
		return menu;
	}

}
