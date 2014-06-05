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
package py.una.pol.karaku.survey.components;

import py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField;

/**
 * Esta clase representa una fila de un dataTable. La cual esta compuesta de un
 * vector de celdas, las cuales representan cada columna del dataTable.
 *
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 17/06/2013
 *
 */
public class DynamicSurveyRow {

	private SurveyField[] cells;
	private String id;
	public static final String TYPE = "py.una.pol.karaku.survey.DynamicSurveyRow";

	public DynamicSurveyRow(String block, int index, int columnsNumber) {

		this.cells = new SurveyField[columnsNumber];
		this.id = block.concat("_row_" + index);

	}

	public DynamicSurveyRow(SurveyField[] cells) {

		super();
		this.cells = cells.clone();
	}

	public SurveyField[] getCells() {

		return cells.clone();
	}

	public void setCells(SurveyField[] cells) {

		this.cells = cells.clone();
	}

	public void addCell(SurveyField element) {

		this.cells[element.getIndex() - 1] = element;
		this.cells[element.getIndex() - 1].setId(getId().concat(
				this.cells[element.getIndex() - 1].getId()));
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	/**
	 * Obtiene una celda en particular de la fila en cuestion.
	 *
	 * @param index
	 * @return
	 */
	public SurveyField getCell(int index) {

		return cells[index];
	}

	public String getType() {

		return TYPE;
	}
}
