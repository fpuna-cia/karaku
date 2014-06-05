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

import java.io.Serializable;

/**
 * Interfaz que define las funcionalidades de un controllador avanzado
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 18, 2013
 * @param <T>
 *            entidad
 * @param <K>
 *            clase del id de la entidad
 * 
 */
public interface IKarakuAdvancedController<T, K extends Serializable> extends
		IKarakuBaseController<T, K> {

	/**
	 * Método que se utiliza para capturar excepciones, manejarlas y mostrar
	 * mensajes de error personalizados
	 * 
	 * <p>
	 * <b>Excepciones que puede recibir</b>
	 * </p>
	 * Puede recibir cualquier excepción, pero las reconocidas son:
	 * <ol>
	 * <li> {@link py.una.pol.karaku.exception.UniqueConstraintException}:
	 * excepciones que se lanzan cuando se un atributo con la anotación
	 * {@link py.una.pol.karaku.model.Unique} duplicado.</li>
	 * </ol>
	 * <p>
	 * </p>
	 * 
	 * @param exception
	 *            es la excepción capturada
	 * @return <code>true</code> si se maneja la excepción y <code>false</code>
	 *         si no se maneja, se retorna <code>false</code> cuando se desea
	 *         que sea capturada y manejada mas arriba en la jerarquía
	 */
	boolean handleException(Exception exception);
}
