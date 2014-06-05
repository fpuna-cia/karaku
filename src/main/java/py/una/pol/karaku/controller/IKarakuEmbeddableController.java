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

/**
 *
 * @author Arturo Volpe
 * @since 2.0, 30/01/2013
 * @version 1.0
 *
 */
public interface IKarakuEmbeddableController {

	/**
	 * Setea el controller al que estara atado, si este controller es nulo, el
	 * controller no deberia actual como un controller anidado
	 *
	 * @param mainController
	 */
	void setMainController(IKarakuMainController mainController);

	/**
	 * Retorna el controlador principal asociado a este controller anidado.
	 *
	 * @return Controlador principal
	 **/
	IKarakuMainController getMainController();

	/**
	 * Retorna la URI donde se encuentra el header a ser expuesto
	 *
	 * @return URI de la cabecera
	 */
	String getHeaderPath();

	/**
	 * Delega la entidad tratada al main controller para que este lo ajuste
	 *
	 * @param child
	 */
	void delegateChild(Object child);

	/**
	 * Setea este controller que apartir de ahora actuara como un controller
	 * hijo
	 */
	void init();

	/**
	 * Cancela todas las actividades y le dice al controller que deje de ser
	 * embebido
	 */
	void cancel();

	/**
	 * Guarda todas las actividades y le dice al controller que deje de ser
	 * embebido
	 */
	void save();

	/**
	 * Metodo que retorna la URI donde se encuentra el formulario de edicion,
	 * esto deberia hacerse con el archivo faces-config.xml pero como no esta en
	 * uso, se hace aca.
	 *
	 * @return URI de la pagina de edicion
	 */
	String goEdit();

	/**
	 * Metodo que retorna la URI donde se encuentra el formulario de creacion,
	 * esto deberia hacerse con el archivo faces-config.xml pero como no esta en
	 * uso, se hace aca.
	 *
	 * @return URI de la pagina de creacion
	 */
	String goNew();

	/**
	 * Metodo que retorna la URI donde se encuentra el formulario de delete,
	 * esto deberia hacerse con el archivo faces-config.xml pero como no esta en
	 * uso, se hace aca.
	 *
	 * @return URI de la pagina de eliminacion
	 */
	String goDelete();

}
