/*
 * @SIGHReportBlock.java 1.0 23/07/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.reports;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Esta clase es utilizada para representar los bloques de un reporte, los
 * cuales estan pueden ser del tipo grilla(con una lista de columnas) o del tipo
 * fields(conformados de una lista de labels). Hay que tener en cuenta que cada
 * bloque definido dentro del reporte debe tener un nameDataSource unico, de
 * manera a poder incorporar el dataSource correspondiente dentro de la lista de
 * parametros.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 23/07/2013
 * 
 */
public class SIGHReportBlock {

	private String title;
	// origen de los datos
	private String nameDataSource;
	private JRDataSource dataSource;

	public SIGHReportBlock(String title, String nameDataSource) {

		super();
		this.title = title;
		this.nameDataSource = nameDataSource;

	}

	public SIGHReportBlock(String title) {

		super();
		this.title = title;

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

}
