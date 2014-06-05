/*
 * @SignBlockBuilder.java 1.0 28/05/2014Sistema Integral de Gestion Hospitalaria
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
