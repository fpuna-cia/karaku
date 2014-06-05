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
package py.una.pol.karaku.jsf.utils;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import py.una.pol.karaku.menu.client.MenuHelper;
import py.una.pol.karaku.menu.schemas.Menu;

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
