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
package py.una.pol.karaku.util;

import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

public class KarakuCustomJasperDataSource extends JRAbstractBeanDataSource {

	private List<?> objects;
	/**
	 * Representa a la fila actual
	 */
	private int row = -1;
	/**
	 * Representa a la columna actual;
	 */
	private int column;

	public KarakuCustomJasperDataSource(List<?> objects) {

		super(true);
		this.objects = objects;
	}

	@Override
	public void moveFirst() throws JRException {

		row = -1;
		column = 0;

	}

	@Override
	public boolean next() throws JRException {

		row++;
		column = 0;
		if (this.objects.size() <= row) {
			return false;
		}
		return true;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {

		if (objects.get(row) instanceof String) {
			if (column == 0) {
				column++;
				return objects.get(row);
			} else {
				return null;
			}
		}
		Object[] columnas = (Object[]) objects.get(row);
		if (columnas.length <= column) {
			column++;
			return null;
		}
		Object object = ((Object[]) objects.get(row))[column];
		column++;
		return object;
	}

}
