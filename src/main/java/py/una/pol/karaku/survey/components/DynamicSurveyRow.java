/*
 * @Row.java 1.0 17/06/2013 Sistema Integral de Gestion Hospitalaria
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
