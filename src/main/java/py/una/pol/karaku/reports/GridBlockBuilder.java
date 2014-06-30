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

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import py.una.pol.karaku.util.I18nHelper;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;

/**
 * Clase utilizada para el diseño de bloques del tipo grilla para un determinado
 * reporte.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 07/04/2014
 * 
 */

public final class GridBlockBuilder {

	private String reportTitle;
	private String nameDataSource;
	private List<Column> columns;

	/**
	 * Construye una instancia de un builder para diseñar bloques de reporte del
	 * tipo {@link KarakuReportBlockField} y {@link KarakuReportBlockGrid}
	 * 
	 * @param title
	 *            título del bloque a construir.
	 *            <p>
	 *            <li>Debe ser una cadena de internacionalización.
	 */
	public GridBlockBuilder(String title) {

		this.reportTitle = getMessage(title);
		this.columns = new ArrayList<Column>();
	}

	/**
	 * 
	 * @param title
	 * @param i18n
	 *            true si se desea internacionalizar el título del bloque
	 */
	public GridBlockBuilder(String title, boolean i18n) {

		if (i18n) {
			reportTitle = getMessage(title);
		} else {
			reportTitle = title;
		}

		this.columns = new ArrayList<Column>();
	}

	public String getTitle() {

		return reportTitle;
	}

	public void setTitle(String title) {

		this.reportTitle = title;
	}

	public String getNameDataSource() {

		return nameDataSource;
	}

	/**
	 * Configura el identificador del dataSource utilizado en el bloque.
	 * 
	 * <li>El identificador debe ser único.
	 * 
	 * @param nameDataSource
	 *            identificador del {@link JRDataSource}
	 * @return
	 */
	protected GridBlockBuilder setNameDataSource(String nameDataSource) {

		this.nameDataSource = nameDataSource;
		return this;
	}

	/**
	 * Columnas utilizadas para representar un bloque del tipo
	 * {@link KarakuReportBlockGrid}.
	 * 
	 * @return
	 */
	public List<Column> getColumns() {

		return columns;
	}

	public void setColumns(List<Column> columns) {

		this.columns = columns;
	}

	public GridBlockBuilder addColumn(Column column) {

		column.setTitle(getMessage(column.getTitle()));
		this.columns.add(column);
		return this;
	}

	public GridBlockBuilder addColumn(String title, String field) {

		this.columns.add(new Column(getMessage(title), field));
		return this;
	}

	/**
	 * Agrega una columna con estilos específicos al reporte.
	 * 
	 * @param title
	 *            Título de la columna
	 * @param field
	 *            Atributo con el cual se debe asociar la columna
	 * @param align
	 *            Alineación de la columna
	 * @param pattern
	 *            Pattern que se desea aplicar a la columna
	 * @return
	 */
	public GridBlockBuilder addColumn(String title, String field,
			HorizontalAlign align, String pattern) {

		this.columns.add(new Column(getMessage(title), field, align, pattern));
		return this;
	}

	/**
	 * Agrega una columna con estilos específicos al reporte.
	 * 
	 * @param title
	 *            Título de la columna
	 * @param field
	 *            Atributo con el cual se debe asociar la columna
	 * @param width
	 *            Ancho de la columna
	 * @param align
	 *            Alineación de la columna
	 * @param pattern
	 *            Pattern que se desea aplicar a la columna
	 * @return
	 */
	public GridBlockBuilder addColumn(String title, String field, int width,
			HorizontalAlign align, String pattern) {

		this.columns.add(new Column(getMessage(title), field, width, align,
				pattern));
		return this;
	}

	public GridBlockBuilder addColumn(String title, boolean i18n, String field) {

		if (i18n) {
			this.columns.add(new Column(getMessage(title), field));
		} else {
			this.columns.add(new Column(title, field));
		}
		return this;
	}

	public GridBlockBuilder addColumn(String title, String field, int width,
			HorizontalAlign align) {

		this.columns.add(new Column(getMessage(title), field, width, align));
		return this;
	}

	public GridBlockBuilder addColumn(String title, String field, int width) {

		this.columns.add(new Column(getMessage(title), field, width));
		return this;
	}

	/**
	 * Internacionaliza la cadena recibida como parámetro.
	 * 
	 * @param key
	 *            cadena de internacionalización.
	 * @return
	 */
	private static String getMessage(String key) {

		return I18nHelper.getSingleton().getString(key);
	}

	/**
	 * Construye un bloque del tipo {@link KarakuReportBlockGrid}
	 * 
	 * 
	 * <p>
	 * Ejemplo del bloque:
	 * </p>
	 * <table>
	 * <tr>
	 * <th>Nombre</th>
	 * <th>Apellido</th>
	 * <th>Sexo</th>
	 * </tr>
	 * <tr>
	 * <td>Jill</td>
	 * <td>Smith</td>
	 * <td>Masculino</td>
	 * </tr>
	 * <tr>
	 * <td>Michael</td>
	 * <td>Jackson</td>
	 * <td>Masculino</td>
	 * </tr>
	 * </table>
	 * 
	 * @param listElement
	 *            lista de elementos a visualizar en la grilla.
	 * @return bloque configurado correctamente.
	 */
	public KarakuReportBlockGrid build(List<?> listElement) {

		return new KarakuReportBlockGrid(getTitle(), getNameDataSource(),
				getColumns(), listElement);
	}

}
