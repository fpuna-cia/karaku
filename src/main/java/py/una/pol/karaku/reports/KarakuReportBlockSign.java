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
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 24/07/2013
 * 
 */
public class KarakuReportBlockSign extends KarakuReportBlock {

	private List<Sign> signs;

	public KarakuReportBlockSign(List<Sign> signs) {

		super("");
		this.signs = signs;
	}

	public static class Sign {

		private String value;
		private int width;

		public Sign(String value, int width) {

			super();
			this.value = value;
			this.width = width;
		}

		public int getWidth() {

			return width;
		}

		public void setWidth(int width) {

			this.width = width;
		}

		public String getValue() {

			return value;
		}

		public void setValue(String value) {

			this.value = value;
		}
	}

	public List<Sign> getSigns() {

		return signs;
	}

	public void setSigns(List<Sign> signs) {

		this.signs = signs;
	}

}
