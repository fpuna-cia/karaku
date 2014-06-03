/**
 * @SIGHSubReport 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.reports;

import java.util.List;

/**
 * Clase utilizada para representar los reportes del tipo cabecera-detalle.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 20/02/2013
 * 
 */
public class SIGHReportDetails {

	private List<Column> columns;
	private List<Detail> details;

	/**
	 * Construye una estructura que representa un reporte del tipo
	 * cabecera-detalle
	 * 
	 * @param columns
	 *            Columnas del reporte principal(cabecera)
	 * @param details
	 *            Detalles del reporte principal
	 */
	public SIGHReportDetails(List<Column> columns, List<Detail> details) {

		super();
		this.columns = columns;
		this.details = details;
	}

	public SIGHReportDetails(List<Detail> details) {

		super();
		this.details = details;
	}

	public void addDetail(Detail detail) {

		details.add(detail);
	}

	public List<Column> getColumns() {

		return columns;
	}

	public void setColumns(List<Column> columns) {

		this.columns = columns;
	}

	public List<Detail> getDetails() {

		return details;
	}

	public void setDetails(List<Detail> details) {

		this.details = details;
	}

	public static class Detail {

		private String title;
		private String field;
		private List<Column> columns;

		public Detail(String title, String field, List<Column> columns) {

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

		public List<Column> getColumns() {

			return columns;
		}

		public void setColumns(List<Column> columns) {

			this.columns = columns;
		}

	}

}
