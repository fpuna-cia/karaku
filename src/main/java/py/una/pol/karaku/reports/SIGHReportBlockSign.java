/*
 * @SIGHReportBlockSign.java 1.0 24/07/2013 Sistema Integral de Gestion
 * Hospitalaria
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
public class SIGHReportBlockSign extends SIGHReportBlock {

	private List<Sign> signs;

	public SIGHReportBlockSign(List<Sign> signs) {

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
