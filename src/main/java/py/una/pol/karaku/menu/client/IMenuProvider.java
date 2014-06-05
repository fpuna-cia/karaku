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
package py.una.pol.karaku.menu.client;

import java.util.List;
import py.una.pol.karaku.menu.schemas.Menu;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 22, 2013
 *
 */
public interface IMenuProvider {

	/**
	 * Retorna los menús actuales, en orden.
	 *
	 *
	 * @return Menú actual del sistema
	 */
	List<Menu> getMenu();

	/**
	 * Agrega un notificador de eventso.
	 */
	void addMenuChangeListener(MenuChangeListener mcl);

	/**
	 * Evento que se lanza cuando se modifica el menú.
	 *
	 * <p>
	 * Es útil para componentes que cachean ciertos elementos o realizan un
	 * trabajo adicional sobre el mismo.
	 * </p>
	 *
	 * TODO ver factibilidad de crear métodos como #onAdd o #onRemove para que
	 * no se reconstruya todo siempre.
	 *
	 * @author Arturo Volpe
	 * @since 2.2.8
	 * @version 1.0 Oct 22, 2013
	 *
	 */
	interface MenuChangeListener {

		/**
		 * Notifica que <code>newMenu</code> es el nuevo menú del sistema.
		 *
		 * @param newMenu
		 */
		void onChange(List<Menu> newMenu);
	}

	/**
	 * Retorna el menú del sistema actual.
	 *
	 * @return Menú del sistema actual.
	 */
	List<Menu> getLocalMenu();
}
