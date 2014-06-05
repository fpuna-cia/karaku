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
import py.una.pol.karaku.reports.KarakuReportBlockSign.Sign;

/**
 * Clase utilizada para el diseño de bloques de firmas de un determinado
 * reporte.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/05/2014
 * 
 */

public final class SignBlockBuilder {

	private List<Sign> signs;

	public SignBlockBuilder() {

		this.signs = new ArrayList<Sign>();
	}

	public SignBlockBuilder addSign(String value, int width) {

		this.signs.add(new Sign(value, width));
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public KarakuReportBlockSign build() {

		return new KarakuReportBlockSign(getSigns());

	}

	public List<Sign> getSigns() {

		return signs;
	}

	public void setSigns(List<Sign> signs) {

		this.signs = signs;
	}

}
