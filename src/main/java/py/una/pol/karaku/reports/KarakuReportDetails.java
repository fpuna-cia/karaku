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
public class KarakuReportDetails {

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
	public KarakuReportDetails(List<Column> columns, List<Detail> details) {

		super();
		this.columns = columns;
		this.details = details;
	}

	public KarakuReportDetails(List<Detail> details) {

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
