/*
 * @SIGHReportBlock.java 1.0 23/07/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.reports;

import java.util.List;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * Esta clase es utilizada para representar los bloques de un reporte, los
 * cuales estan conformados de una lista de labels. Hay que tener en cuenta que
 * cada bloque definido dentro del reporte debe tener un nameDataSource unico,
 * de manera a poder incorporar el dataSource correspondiente dentro de la lista
 * de parametros.
 *
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 23/07/2013
 *
 */
public final class SIGHReportBlock {

	private String title;
	// origen de los datos
	private String nameDataSource;
	private JRDataSource dataSource;
	private int widthLabel;
	private int widthValue;

	public SIGHReportBlock(String title, String nameDataSource,
			List<Field> fields, int widthLabel, int widthValue) {

		super();
		this.title = title;
		this.nameDataSource = nameDataSource;
		this.dataSource = buildDataSource(fields);
		this.widthLabel = widthLabel;
		this.widthValue = widthValue;

	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public String getNameDataSource() {

		return nameDataSource;
	}

	public void setNameDataSource(String nameDataSource) {

		this.nameDataSource = nameDataSource;
	}

	public JRDataSource getDataSource() {

		return dataSource;
	}

	public void setDataSource(JRDataSource dataSource) {

		this.dataSource = dataSource;
	}

	public JRDataSource buildDataSource(List<Field> fields) {

		DRDataSource ds = new DRDataSource("label", "value");

		for (Field field : fields) {
			ds.add(field.getLabel(), field.getValue());
		}
		return ds;
	}

	public int getWidthValue() {

		return widthValue;
	}

	public void setWidthValue(int widthValue) {

		this.widthValue = widthValue;
	}

	public int getWidthLabel() {

		return widthLabel;
	}

	public void setWidthLabel(int widthLabel) {

		this.widthLabel = widthLabel;
	}

	public static class Field {

		private String label;
		private String value;

		public Field(String label, String value) {

			super();
			this.label = label;
			this.value = value;
		}

		public String getValue() {

			return value;
		}

		public void setValue(String value) {

			this.value = value;
		}

		public String getLabel() {

			return label;
		}

		public void setLabel(String label) {

			this.label = label;
		}

	}

}
