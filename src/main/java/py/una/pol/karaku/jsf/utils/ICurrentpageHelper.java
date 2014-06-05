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
import javax.faces.event.FacesListener;
import py.una.pol.karaku.controller.MenuBean;
import py.una.pol.karaku.menu.schemas.Menu;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 7, 2013
 *
 */
public interface ICurrentpageHelper {

	/**
	 * Método que se encarga de inicializar este componente, debe ser invocado
	 * al inicio de cada página, no es un {@link FacesListener} por qué debe ser
	 * un componente de Spring.
	 */
	String initialize();

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
	HtmlOutputText getBind();

	/**
	 * <b>WorkAround</b> para permitir que esto sea lo primero que se ejecuta a
	 * la hora de mostrar una página.
	 * <p>
	 *
	 * @see #getBind()
	 */
	void setBind(HtmlOutputText input);

	/**
	 * Retorna el menú que actualmente esta seleccionado, si el mismo no se
	 * puede detectar, se retorna <code>null</code>
	 *
	 * @return menu actual elemento seleccionado.
	 */
	Menu getCurrentMenu();

}
