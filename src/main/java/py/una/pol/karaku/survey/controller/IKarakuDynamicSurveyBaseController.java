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
package py.una.pol.karaku.survey.controller;

import java.util.List;
import py.una.pol.karaku.survey.components.DynamicSurveyBlock;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 03/06/2013
 * 
 */
public interface IKarakuDynamicSurveyBaseController {

	/**
	 * Obtiene las lista de bloques de la encuesta.
	 * 
	 * <p>
	 * Los bloques definen la estructura dinámica de la encuesta, donde cada
	 * bloque puede representar una lista de fields(label,valor)o una grilla de
	 * elementos.
	 * 
	 * @return Lista de bloques de la encuesta.
	 */
	List<DynamicSurveyBlock> getBlocks();

	/**
	 * Setea la encuesta a ser tratada por el controlador.
	 * 
	 * @param bean
	 *            Encuesta en cuestion.
	 */
	void setBean(Encuesta bean);

	/**
	 * Obtiene la encuesta manipulada por el controlador.
	 * 
	 * @return Encuesta en cuestion.
	 */
	Encuesta getBean();

	/**
	 * Método invocado al presionar el botón cancelar.
	 * 
	 * @return Página desde donde fue invocada la encuesta.
	 */
	String doCancel();

	/**
	 * Retorna true si el controlador esta en modo NEW.
	 * 
	 * @return
	 */
	boolean isNew();

	/**
	 * Retorna true si el controlador esta en modo EDIT.
	 * 
	 * @return
	 */
	boolean isEdit();

	/**
	 * Retorna true si el controlador esta en modo VIEW.
	 * 
	 * @return
	 */
	boolean isView();

	/**
	 * Retorna true si el controlador esta en modo DELETE.
	 * 
	 * @return
	 */
	boolean isDelete();

	/**
	 * Verifica si los campos del formulario deben estar habilitados o no.
	 * 
	 * 
	 * <p>
	 * Esto es, retorna true si el controlador esta en modo NEW o EDIT.
	 * 
	 * @return
	 */
	boolean isEditable();

	/**
	 * Construye cada uno de los bloques del formulario en base al template
	 * asociado a la encuesta.
	 * 
	 * <p>
	 * Tambien setea las respuestas de cada pregunta si las mismas ya existen en
	 * la base de datos.
	 */
	void buildBlocksFromSurvey();

	/**
	 * Agrega un bloque al formulario dinámico.
	 * 
	 * @param bloque
	 *            Bloque a agregar al formulario.
	 * @return Lista de bloques de la encuesta.
	 */
	List<DynamicSurveyBlock> addBlock(DynamicSurveyBlock bloque);

	/**
	 * Crea un nuevo bean con la plantilla pasada como parámetro y la fecha
	 * actual.
	 * 
	 * @param template
	 *            Plantilla que define la estructura del formulario dinámico.
	 */
	void preCreate(EncuestaPlantilla template);

	/**
	 * Realiza las configuraciones necesarias para crear una encuesta y
	 * construir el formulario dinámico.
	 * 
	 * @param template
	 *            Plantilla que define la estructura del formulario dinámico.
	 * @return Página del formulario dinámico.
	 */
	String goCreate(EncuestaPlantilla template);

	/**
	 * Crea la encuesta en base al formulario dinámico.
	 * 
	 * @return Paǵina desde donde se le invoco al formulario dinámico.
	 */
	String doCreate();

	/**
	 * Persiste la encuesta en la base de datos.
	 */
	void create();

	/**
	 * Setea en el controlador la encuesta que se desea editar.
	 * 
	 * @param encuesta
	 * 
	 *            Encuesta que se desea editar.
	 */
	void preEdit(Encuesta encuesta);

	/**
	 * Realiza las configuraciones necesarias para construir la encuesta pasada
	 * como parámetro en el formulario dinámico.
	 * 
	 * @param encuesta
	 *            Encuesta que se dese editar
	 * @return Página del formulario dinámico.
	 */
	String goEdit(Encuesta encuesta);

	/**
	 * Edita la encuesta en base a los datos ingresados en el formulario.
	 * 
	 * @return Paǵina desde donde se le invoco al formulario dinámico.
	 */
	String doEdit();

	/**
	 * Persiste las modificaciones realizadas en una determinada encuesta.
	 */
	void edit();

	/**
	 * Setea en el controlador la encuesta que se desea visualizar.
	 * 
	 * @param encuesta
	 *            Encuesta que se desea visualizar en el formulario dinámico.
	 */
	void preView(Encuesta encuesta);

	/**
	 * Realiza las configuraciones necesarias para la visualización de una
	 * determinada encuesta
	 * 
	 * @param encuesta
	 *            Encuesta que se desea visualizar en el formulario dinámico.
	 * @return Página del formulario dinámico.
	 */
	String goView(Encuesta encuesta);

	/**
	 * Setea en el controlador la encuesta que se desea eliminar.
	 * 
	 * @param encuesta
	 *            Encuesta que se desea visualizar en el formulario dinámico
	 *            para luego eliminarse.
	 */
	void preDelete(Encuesta encuesta);

	/**
	 * Realiza las configuraciones necesarias para contruir la encuesta dinámica
	 * para su posterior eliminación.
	 * 
	 * @param encuesta
	 *            Encuesta que se desea visualizar en el formulario dinámico
	 *            para ser eliminada.
	 * @return Página del formulario dinámico.
	 */
	String goDelete(Encuesta encuesta);

	/**
	 * Elimina una determinada encuesta.
	 * 
	 * @return Paǵina desde donde se le invoco al formulario dinámico.
	 */
	String doDelete();

}
