/*
 * @ReportBlockBuilder.java 1.0 07/04/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.reports;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import py.una.med.base.util.I18nHelper;
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
	 * tipo {@link SIGHReportBlockField} y {@link SIGHReportBlockGrid}
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
	 * {@link SIGHReportBlockGrid}.
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
	 * Construye un bloque del tipo {@link SIGHReportBlockGrid}
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
	public SIGHReportBlockGrid build(List<?> listElement) {

		return new SIGHReportBlockGrid(getTitle(), getNameDataSource(),
				getColumns(), listElement);
	}

}
