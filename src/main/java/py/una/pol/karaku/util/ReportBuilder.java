/*
 * @ReportBuilder.java 1.0 28/03/2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import py.una.pol.karaku.reports.Align;
import py.una.pol.karaku.reports.SIGHReportBlock;
import py.una.pol.karaku.reports.SIGHReportBlockField;

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
	private boolean sectionCriteria;
	private Align align;

	public ReportBuilder(String title, String type) {

		this.params = new HashMap<String, Object>();
		// Agrega el título del reporte en la los parámetros.
		this.addParam("titleReport", getMessage(title));
		this.sectionCriteria = false;
		this.setAlign(Align.VERTICAL);
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

	/**
	 * Agrega un bloque al reporte.
	 * 
	 * Cada bloque se considera como un subreporte.
	 * 
	 * @param block
	 * @return
	 */
	public ReportBuilder addBlock(SIGHReportBlock block) {

		return this.addDetail(block);
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

	/**
	 * Retorna los bloques del reporte master-detail.
	 * 
	 * Donde el primer bloque representa la cabecera y los demás cada uno de los
	 * detalles asociados.
	 * 
	 * @return Lista de bloques del reporte del tipo master-detail.
	 */
	public List<SIGHReportBlock> getBlocksMasterDetail() {

		List<SIGHReportBlock> blocks = new ArrayList<SIGHReportBlock>();
		blocks.add(master);
		blocks.addAll(details);
		return blocks;
	}

	/**
	 * Retorna los bloques asociados al reporte.
	 * 
	 * @return Lista de bloques del reporte.
	 */
	public List<SIGHReportBlock> getBlocks() {

		return details;
	}

	private static String getMessage(String key) {

		return I18nHelper.getSingleton().getString(key);
	}

	public boolean isSectionCriteria() {

		return sectionCriteria;
	}

	public void setSectionCriteria(boolean sectionCriteria) {

		this.sectionCriteria = sectionCriteria;
	}

	public Align getAlign() {

		return align;
	}

	public void setAlign(Align align) {

		this.align = align;
	}
}
