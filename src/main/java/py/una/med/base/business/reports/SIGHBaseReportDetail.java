/**
 * @SIGHBaseReportDetail 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.reports.Align;
import py.una.med.base.reports.ExportReport;
import py.una.med.base.reports.SIGHReportBlock;
import py.una.med.base.reports.SIGHReportBlockSign;
import py.una.med.base.reports.SIGHReportDetails;
import py.una.med.base.util.ControllerHelper;

/**
 * Clase que implementa el servicio para reportes del tipo cabecera-detalle. Se
 * refiere al reporte especifico de un registro de la grilla.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 07/03/2013
 * 
 */
public abstract class SIGHBaseReportDetail<T> implements
		ISIGHBaseReportDetail<T> {

	@Autowired
	private ExportReport exportReport;

	@Autowired
	private ControllerHelper controllerHelper;

	@Override
	public abstract ISIGHBaseLogic<T, ?> getBaseLogic();

	@Override
	public abstract List<?> getDetails(T bean);

	@Override
	public void generateReport(SIGHReportDetails report, Align align,
			Map<String, Object> params, String type, T bean, Class<?> clazz) {

		try {
			JRDataSource datasource = new JRBeanCollectionDataSource(
					getDetails(bean));
			exportReport.exportDetailReport(report, align, clazz, datasource,
					params, type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_SUCCESS");
		} catch (Exception e) {
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_FAILURE");
		}

	}

	@Override
	public void generateReport(String path, SIGHReportDetails report,
			Map<String, Object> params, String type, T bean, Class<?> clazz) {

		try {
			JRDataSource datasource = new JRBeanCollectionDataSource(
					getDetails(bean));
			exportReport.exportDetailReport(path, report, clazz, datasource,
					params, type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_SUCCESS");
		} catch (Exception e) {
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_FAILURE");
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
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_FAILURE");
		}

	}

	@Override
	public void generateReport(List<SIGHReportBlock> blocks,
			Map<String, Object> params, String type) {

		try {
			exportReport.exportReportFields(blocks,
					setDataSources(blocks, params), type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_FAILURE");
		}

	}

	@Override
	public void generateReport(List<SIGHReportBlock> blocks,
			List<SIGHReportBlockSign> signs, Map<String, Object> params,
			String type) {

		try {
			exportReport.exportReportFields(blocks, signs,
					setDataSources(blocks, params), type);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_INFO, "BASE_REPORT_CREATE_FAILURE");
		}

	}

	private Map<String, Object> setDataSources(List<SIGHReportBlock> blocks,
			Map<String, Object> params) {

		for (SIGHReportBlock block : blocks) {
			System.out.println("AGREGAAA PARAMETROSS: "
					+ block.getNameDataSource() + "--" + block.getDataSource());
			params.put(block.getNameDataSource(), block.getDataSource());
		}
		return params;
	}
}
