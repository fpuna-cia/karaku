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
public class KarakuReportBlock {

	private String title;
	// origen de los datos
	private String nameDataSource;
	private JRDataSource dataSource;

	public KarakuReportBlock(String title, String nameDataSource) {

		super();
		this.title = title;
		this.nameDataSource = nameDataSource;

	}

	public KarakuReportBlock(String title) {

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
