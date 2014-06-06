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
package py.una.pol.karaku.test.base;

import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.test.util.TestControllerHelper;
import py.una.pol.karaku.test.util.TestI18nHelper;
import py.una.pol.karaku.util.I18nHelper;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 11, 2014
 * 
 */
public abstract class BaseControllerTest extends BaseTest {

	@Autowired
	private TestControllerHelper helper;

	@Autowired
	private TestI18nHelper i18nHelper;

	/**
	 * Retorna el {@link TestControllerHelper}.
	 * 
	 * <p>
	 * Es útil para verificar los mensajes, se puede obtener el último mensaje
	 * generado con {@link TestControllerHelper#getLastMessage()}
	 * </p>
	 * 
	 * @return helper nunca <code>null</code>
	 * @see TestControllerHelper
	 */
	public TestControllerHelper getHelper() {

		return helper;
	}

	/**
	 * Retorna el {@link TestI18nHelper}.
	 * 
	 * <p>
	 * Es útil para agregar cadenas a internacionalizar.
	 * </p>
	 * 
	 * @return i18nHelper {@link I18nHelper}, nunca <code>null</code>.
	 * @see TestI18nHelper
	 */
	public TestI18nHelper getI18nHelper() {

		return i18nHelper;
	}
}
