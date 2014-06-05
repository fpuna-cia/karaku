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

package py.una.pol.karaku.business.reports;

import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.reports.Align;
import py.una.pol.karaku.reports.ExportReport;
import py.una.pol.karaku.reports.KarakuReportBlock;
import py.una.pol.karaku.reports.KarakuReportBlockSign;
import py.una.pol.karaku.reports.KarakuReportDetails;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.I18nHelper;

/**
 * Clase que implementa el servicio para reportes del tipo cabecera-detalle. Se
 * refiere al reporte especifico de un registro de la grilla.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 07/03/2013
 * 
 */
public abstract class KarakuBaseReportDetail<T> implements
		IKarakuBaseReportDetail<T> {

	/**
	 * 
	 */
	private static final String ERROR_MESSAGE = "Can't generate report";
	private static final String BASE_REPORT_CREATE_FAILURE = "BASE_REPORT_CREATE_FAILURE";
	private static final String BASE_REPORT_CREATE_SUCCESS = "BASE_REPORT_CREATE_SUCCESS";
	@Log
	private transient Logger log;
	@Autowired
	private ExportReport exportReport;

	@Autowired
	private ControllerHelper controllerHelper;

	@Autowired
	private I18nHelper i18nHelper;

	@Override
	public abstract IKarakuBaseLogic<T, ?> getBaseLogic();

	@Override
	public abstract List<?> getDetails(T bean);

	@Override
	public boolean withCriteriaVisible() {

		return true;
	}

	@Override
	public void generateReport(KarakuReportDetails report, Align align,
			Map<String, Object> params, String type, T bean, Class<?> clazz) {

		try {
			JRDataSource datasource = new JRBeanCollectionDataSource(
					getDetails(bean));
			exportReport.exportDetailReport(report, align,
					withCriteriaVisible(), clazz, datasource, params, type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_SUCCESS);
		} catch (Exception e) {
			log.warn("Can't create report", e);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_FAILURE);
		}

	}

	@Override
	public void generateReport(String path, KarakuReportDetails report,
			Map<String, Object> params, String type, T bean, Class<?> clazz) {

		try {
			JRDataSource datasource = new JRBeanCollectionDataSource(
					getDetails(bean));
			exportReport.exportDetailReport(path, report, clazz, datasource,
					params, type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_SUCCESS);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_FAILURE);
		}

	}

	@Override
	public void generateReport(String path, Map<String, Object> params,
			String type, T bean, Class<?> clazz) {

		try {
			JRDataSource datasource = new JRBeanCollectionDataSource(
					getDetails(bean));
			exportReport.exportReportStatic(path, datasource, params, type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_SUCCESS);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_FAILURE);
		}

	}

	@Override
	public void generateReport(List<KarakuReportBlock> blocks,
			Map<String, Object> params, String type) {

		try {
			exportReport.exportReportBlock(Align.VERTICAL, false, blocks,
					setDataSources(blocks, params), type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_SUCCESS);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_FAILURE);
		}

	}

	@Override
	public void generateReport(List<KarakuReportBlock> blocks,
			List<KarakuReportBlockSign> signs, Map<String, Object> params,
			String type) {

		try {
			exportReport.exportReportFields(withCriteriaVisible(), blocks,
					signs, setDataSources(blocks, params), type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_SUCCESS);
		} catch (Exception e) {
			log.warn(ERROR_MESSAGE, e);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, BASE_REPORT_CREATE_FAILURE);
		}

	}

	private Map<String, Object> setDataSources(List<KarakuReportBlock> blocks,
			Map<String, Object> params) {

		for (KarakuReportBlock block : blocks) {
			params.put(block.getNameDataSource(), block.getDataSource());
		}
		return params;
	}

	/**
	 * Retorna una cadena internacionalizada dada la llave.
	 * 
	 * @param code
	 *            clave del archivo de internacionalización
	 * @return cadena internacionalizada
	 */
	public String getMessage(String code) {

		return i18nHelper.getString(code);
	}
}
