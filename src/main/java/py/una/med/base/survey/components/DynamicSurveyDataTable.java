/*
 * @DynamicSurveyCell.java 1.0 04/06/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.components;

import java.util.List;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import py.una.med.base.survey.components.DynamicSurveyField.SurveyField;
import py.una.med.base.survey.domain.EncuestaPlantillaPregunta;
import py.una.med.base.util.I18nHelper;

/**
 * Esta clase representa un dataTable, el cual esta compuesto de una lista de
 * filas.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 04/06/2013
 * 
 */
public class DynamicSurveyDataTable extends DynamicSurveyBlock {

	private List<DynamicSurveyRow> rows;
	private int rowsNumber = 0;
	private int columnsNumber = 0;
	public static final int ROWS_FOR_TABLE = 8;
	public static final String TYPE = "py.una.med.base.survey.components.DynamicSurveyDataTable";
	private String globalMessage = "";

	/**
	 * Construye un bloque del tipo GRILLA.
	 * 
	 * @param questions
	 *            Lista de preguntas del bloque, que en este caso se representan
	 *            como las columnas del bloque.
	 * @param index
	 *            Ubicacion del bloque dentro de la encuesta
	 */
	public DynamicSurveyDataTable(List<EncuestaPlantillaPregunta> questions,
			int index) {

		super(questions, index);
		buildColumns(questions);

	}

	public List<DynamicSurveyRow> getRows() {

		return rows;
	}

	public void setRows(List<DynamicSurveyRow> rows) {

		this.rows = rows;
	}

	public DynamicSurveyDataTable buildRows(List<DynamicSurveyRow> rows) {

		initRows(rows.size());
		this.rows = validateResponse(rows);
		return this;

	}

	private DynamicSurveyDataTable buildColumns(
			List<EncuestaPlantillaPregunta> questions) {

		initColumns(questions.size());
		return this;

	}

	private void initRows(int rowsNumber) {

		this.setRowsNumber(rowsNumber);
	}

	private void initColumns(int columnsNumber) {

		this.setColumnsNumber(columnsNumber);
	}

	public DynamicSurveyDataTable addRow() {

		if (validateAddRow()) {
			clearGlobalMessage();
			addEmptyRow();

		} else {
			setGlobalMessagePerkeyValue("NOT_ADD_ROW");
		}
		return this;
	}

	public DynamicSurveyDataTable deleteRow(int index) {

		if (validateDeleteRow()) {
			clearGlobalMessage();
			deleteEmptyRow(index);
		} else {
			setGlobalMessagePerkeyValue("NOT_DELETE_ROW");
		}
		return this;
	}

	private void addEmptyRow() {

		this.rows.add(buildEmptyRow());
		this.rowsNumber++;
	}

	private void deleteEmptyRow(int index) {

		this.rows.remove(index);
		this.rowsNumber--;
	}

	private DynamicSurveyRow buildEmptyRow() {

		DynamicSurveyRow row = new DynamicSurveyRow(getId(), rowsNumber + 1,
				columnsNumber);
		for (EncuestaPlantillaPregunta column : getQuestions()) {
			row.addCell(DynamicSurveyField.fieldFactory(column));
		}
		return row;
	}

	private boolean validateAddRow() {

		if (rowsNumber < ROWS_FOR_TABLE) {
			return true;

		}
		return false;
	}

	/**
	 * Crea una celda para aquellas respuestas no requeridas.
	 * 
	 * @param rows
	 *            lista de respuestas registradas o almacenadas
	 * 
	 * @return filas del bloque construidas
	 */
	private List<DynamicSurveyRow> validateResponse(List<DynamicSurveyRow> rows) {

		for (DynamicSurveyRow row : rows) {
			int index = 0;
			for (SurveyField cell : row.getCells()) {

				if (cell == null) {
					row.addCell(DynamicSurveyField.fieldFactory(getQuestions()
							.get(index)));
				}
				index++;
			}
		}
		return rows;
	}

	private boolean validateDeleteRow() {

		if (rowsNumber > 0) {
			return true;
		}
		return false;
	}

	public int getRowsNumber() {

		return rowsNumber;
	}

	public void setRowsNumber(int rowsNumber) {

		this.rowsNumber = rowsNumber;
	}

	public int getColumnsNumber() {

		return columnsNumber;
	}

	public void setColumnsNumber(int columnsNumber) {

		this.columnsNumber = columnsNumber;
	}

	/**
	 * Obtiene una fila en particular del dataTable en cuestion.
	 * 
	 * @param index
	 * @return
	 */
	public DynamicSurveyRow getRow(int index) {

		return rows.get(index);
	}

	@Override
	public String getType() {

		return TYPE;
	}

	public String getGlobalMessage() {

		return globalMessage;
	}

	public void setGlobalMessage(String globalMessage) {

		this.globalMessage = globalMessage;
	}

	/*
	 * Obtiene el mensaje asociado a una determinada clave de
	 * internacionalizacion, para ser visualizado en la seccion de mensajes
	 * globales.
	 * 
	 * @param key
	 */
	private void setGlobalMessagePerkeyValue(String key) {

		setGlobalMessage(I18nHelper.getMessage(key));
	}

	private void clearGlobalMessage() {

		setGlobalMessage("");
	}

	/**
	 * Setea el valor ingresado en una celda de una fila en particular en el
	 * momento en el cual el valor es ingresado de manera a preservar el valor.
	 * 
	 * @param event
	 */
	public void completeChangeListener(AjaxBehaviorEvent event) {

		int row = Integer.valueOf(getRequestParameter("rowNumber"));
		int cell = Integer.valueOf(getRequestParameter("cellNumber"));
		String value = (String) ((UIInput) event.getComponent()).getValue();
		rows.get(row).getCell(cell).setValue(value);
	}
}
