/**
 * @SIGHSubReport 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.reports;

import java.util.LinkedList;

/**
 * Clase utilizada para representar los reportes del tipo cabecera-detalle.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 20/02/2013
 * 
 */
public class SIGHReportDetails {

	private LinkedList<Column> columns;
	private LinkedList<Detail> details;

	/**
	 * Construye una estructura que representa un reporte del tipo
	 * cabecera-detalle
	 * 
	 * @param columns
	 *            Columnas del reporte principal(cabecera)
	 * @param details
	 *            Detalles del reporte principal
	 */
	public SIGHReportDetails(LinkedList<Column> columns,
			LinkedList<Detail> details) {

		super();
		this.columns = columns;
		this.details = details;
	}

	public SIGHReportDetails(LinkedList<Detail> details) {

		super();
		this.details = details;
	}

	public void addDetail(Detail detail) {

		details.add(detail);
	}

	public LinkedList<Column> getColumns() {

		return columns;
	}

	public void setColumns(LinkedList<Column> columns) {

		this.columns = columns;
	}

	public LinkedList<Detail> getDetails() {

		return details;
	}

	public void setDetails(LinkedList<Detail> details) {

		this.details = details;
	}

	public static class Detail {

		private String title;
		private String field;
		private LinkedList<Column> columns;

		public Detail(String title, String field, LinkedList<Column> columns) {

			super();
			this.title = title;
			this.field = field;
			this.columns = columns;
		}

		public void addColumn(Column column) {

			columns.add(column);
		}

		public String getTitle() {

			return title;
		}

		public void setTitle(String title) {

			this.title = title;
		}

		public String getField() {

			return field;
		}

		public void setField(String field) {

			this.field = field;
		}

		public LinkedList<Column> getColumns() {

			return columns;
		}

		public void setColumns(LinkedList<Column> columns) {

			this.columns = columns;
		}

	}

}
