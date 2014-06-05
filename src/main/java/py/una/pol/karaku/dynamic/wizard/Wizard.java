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
package py.una.pol.karaku.dynamic.wizard;

import java.util.List;

/**
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public interface Wizard {

	/**
	 * Longitud máxima del titulo del wizard
	 */
	int TITLE_MAX_LENGTH = 100;

	/**
	 * Lista de pasos que componen este wizard
	 * 
	 * @return {@link List} de pasos
	 */
	List<Step> getSteps();

	/**
	 * Reinicia todos los pasos dados por {@link #getSteps()}
	 */
	void initialize();

	/**
	 * Se mueve al siguiente paso, si {@link #validateEachStep()} retorna
	 * <code>true</code>, este método valida el contenido del <b>paso
	 * actual</b>, si falla se mantiene en el paso actual, en caso contrario
	 * avanza.
	 * 
	 * @return El {@link Step} actual si la validación falla, o el siguiente
	 *         {@link Step} si no hay problemas de validación.
	 */
	Step next();

	/**
	 * Vuelve al paso anterior, sin modificar el contenido del paso actual, esto
	 * es, si el paso actual sufrio alguna modificación, estas persisten.
	 * 
	 * @return {@link Step} anterior
	 */
	Step previous();

	/**
	 * Bandera que representa si cada paso debe ser validado.
	 * 
	 * @return <code>true</code> si cada paso (AJAX) debe ser validado,
	 *         <code>false</code> si no se requiere la validación
	 */
	boolean validateEachStep();

	/**
	 * Retorna el numero del paso actual, con este resultado se puede invocar a
	 * {@link List#get(int)} sobre el resultado de {@link #getSteps()} se
	 * obtiene el paso actual.
	 * 
	 * @return entero positivo entre 0 y size({@link #getSteps()})
	 */
	int getCurrentStepNumber();

	/**
	 * Retorna el titulo del wizard. Esta cadena debe resumir la función del
	 * wizard, y no debe ser muy extensa. Su longitud va de 0 a
	 * {@value #TITLE_MAX_LENGTH}
	 * 
	 * @return Cadena que explica el funcionamiento del wizard.
	 */
	String getTitle();

	/**
	 * Retorna el ID del panel, este id es importante pues se utiliza en la
	 * definición de los botones, y además es el identificador único de todo
	 * aquello que forma parte del wizard.
	 * 
	 * @return
	 */
	String getTogglePanelId();
}
