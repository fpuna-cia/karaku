/*
 * @ReportBuilder.java 1.0 28/03/2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import py.una.med.base.reports.SIGHReportBlock;
import py.una.med.base.reports.SIGHReportBlockField;

/**
 * Clase utilizada para el diseño de reportes.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/03/2014
 * 
 */

public final class ReportBuilder {

	private SIGHReportBlockField master;

	private List<SIGHReportBlock> details;

	private Map<String, Object> params;
	private String type;

	public ReportBuilder(String title, String type) {

		this.params = new HashMap<String, Object>();
		// Agrega el título del reporte en la los parámetros.
		this.addParam("titleReport", getMessage(title));
		this.type = type;
	}

	/**
	 * @param block
	 *            Representa la cabecera de un reporte cabecera-detalle
	 */
	public ReportBuilder setMaster(SIGHReportBlockField block) {

		this.master = block;
		return this;
	}

	/**
	 * @param block
	 *            Representa un determinado detalle de un reporte
	 *            cabecera-detalle
	 */
	public ReportBuilder addDetail(SIGHReportBlock block) {

		if (details == null) {
			details = new ArrayList<SIGHReportBlock>();
		}
		this.details.add(block);
		return this;
	}

	public List<SIGHReportBlock> getDetails() {

		return details;
	}

	public SIGHReportBlockField getMaster() {

		return master;
	}

	public Map<String, Object> getParams() {

		return params;
	}

	public void setParams(Map<String, Object> params) {

		this.params = params;
	}

	public ReportBuilder addParam(String key, Object value) {

		this.params.put(key, value);
		return this;

	}

	public String getType() {

		return type;
	}

	public ReportBuilder setType(String type) {

		this.type = type;
		return this;
	}

	public List<SIGHReportBlock> getBlocks() {

		List<SIGHReportBlock> blocks = new ArrayList<SIGHReportBlock>();
		blocks.add(master);
		blocks.addAll(details);
		return blocks;
	}

	private static String getMessage(String key) {

		return I18nHelper.getSingleton().getString(key);
	}
}
