/**
 * @ExportReport 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.reports;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import py.una.med.base.util.I18nHelper;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;

/**
 * Provee las funcionalidades basicas para la exportacion de reportes.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.4 07/03/2013
 */
public final class ExportReport {

	private ExportReport() {

		// No-op
	}

<<<<<<< HEAD
	private static final String FILE_LOCATION_TEMPLATE = "report/base/";
	private static final String FILE_LOCATION = "report/";
	private static final String EXPORT_TYPE_EXCEL = "xls";
	private static final String EXPORT_TYPE_PDF = "pdf";
	private static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	private static final String MEDIA_TYPE_PDF = "application/pdf";
=======
	private static String FILE_LOCATION_TEMPLATE = "report/base/";
	private static String FILE_LOCATION = "report/";
	private static String EXPORT_TYPE_EXCEL = "xls";
	private static String EXPORT_TYPE_PDF = "pdf";
	private static String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	private static String MEDIA_TYPE_PDF = "application/pdf";
>>>>>>> Sonar - Regla. Hide Utility Class Constructor IIIv2

	/**
	 * Obtiene los parametros genericos para los reportes simples<br>
	 * 
	 * @return Lista de parametros generales que seran enviados al template
	 *         basico para los reportes
	 */
	private static Map<String, Object> getDetailsReport(
			Map<String, Object> params) throws IOException {

		ClassPathResource imagePath = new ClassPathResource(
				FILE_LOCATION_TEMPLATE + "logo.jpg");

		params.put("logo", imagePath.getInputStream());
		params.put("nombreInstitucion",
				I18nHelper.getMessage("BASE_REPORT_NAME_INSTITUTION"));
		params.put("nombreEstablecimiento",
				I18nHelper.getMessage("BASE_REPORT_NAME_ESTABLISHMENT"));
		params.put("date", I18nHelper.getMessage("BASE_REPORT_DATE"));
		params.put("time", I18nHelper.getMessage("BASE_REPORT_TIME"));
		params.put("selectionCriteria",
				I18nHelper.getMessage("BASE_REPORT_SELECTION_CRITERIA"));
		params.put("user", I18nHelper.getMessage("BASE_REPORT_USER"));
		params.put("userName", SecurityContextHolder.getContext()
				.getAuthentication().getName());
		params.put("nameSystem",
				I18nHelper.getMessage("BASE_REPORT_NAME_SYSTEM"));
		params.put("page", I18nHelper.getMessage("BASE_REPORT_PAGE"));
		params.put("pageThe", I18nHelper.getMessage("BASE_REPORT_PAGE_THE"));
		return params;
	}

	/**
	 * Genera un reporte utilizando el template basico de configuracion que se
	 * encuentra en la aplicacion, y el contenido es generado de forma dinamica.
	 * Es utilizado para los reportes de la grilla<br>
	 * 
	 * @param columns
	 *            Columnas que seran visualizadas en el reporte
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @param dataSource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param params
	 *            Parametros especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @return reporte generado
	 */
	public static <T> void exportSimpleReport(LinkedList<Column> columns,
			Class<T> clazz, JRDataSource dataSource,
			Map<String, Object> params, String type) {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					DynamicUtils.buildReportSimple(columns, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(jasperPrint, params, dataSource, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genera un reporte utilizando el template basico de configuracion que se
	 * encuentra en la aplicacion, y el contenido es generado de forma dinamica.
	 * Es utilizado para los reportes de la grilla cuyas columnas son atributos
	 * calculados<br>
	 * 
	 * @param columns
	 *            Columnas que seran visualizadas en el reporte
	 * @param dataSource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param params
	 *            Parametros especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @return reporte generado
	 */
	public static <T> void exportSimpleReport(LinkedList<Column> columns,
			JRDataSource dataSource, Map<String, Object> params, String type) {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					DynamicUtils.buildReportSimple(columns),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(jasperPrint, params, dataSource, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genera un reporte del tipo cabecera-detalle de forma dinamica utilizando
	 * un template base, tanto los atributos de la cabecera como los detalles
	 * son generados de forma dinamica.
	 * 
	 * @param report
	 *            Estructura que contiene la definicion estructural del reporte
	 *            y datos necesarios para generar el mismo.
	 * @param align
	 *            Alineacion con la cual se desea visualizar el reporte
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @param dataSource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param params
	 *            Parametros especificos del reporte.
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 */
	public static <T> void exportDetailReport(SIGHReportDetails report,
			Align align, Class<T> clazz, JRDataSource dataSource,
			Map<String, Object> params, String type) {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					DynamicUtils.buildReportDetail(report, align, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(jasperPrint, params, dataSource, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genera un reporte del tipo cabecera-detalle de forma dinamica utilizando
	 * un template especifico, los atributos de la cabecera son definidos dentro
	 * del mismo, no son generados de forma dinamica a diferencia de los
	 * detalles los cuales si son generados de forma dinamica.
	 * 
	 * @param path
	 *            Ubicacion del template que se desea adicionar al reporte. Debe
	 *            ser el nombre de la dependencia, luego el directorio, seguido
	 *            del nombre del jrxml.
	 * @param report
	 *            Estructura que contiene la definicion y datos necesarios para
	 *            generar el reporte.
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @param dataSource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param params
	 *            Parametros especificos del reporte.
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 */

	public static <T> void exportDetailReport(String path,
			SIGHReportDetails report, Class<T> clazz, JRDataSource dataSource,
			Map<String, Object> params, String type) {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					DynamicUtils.buildReportDetail(path, report, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(jasperPrint, params, dataSource, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Genera un reporte complejo de forma dinamica.
	 * 
	 * @param report
	 *            Reporte complejo especifico
	 * @param dataSource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param params
	 *            Parametros especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 */
	public static void exportAvancedReport(DynamicReport report,
			JRDataSource dataSource, Map<String, Object> params, String type) {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(report,
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(jasperPrint, params, dataSource, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que genera un reporte utilizando un archivo con extension jrxml,
	 * el mismo es previamente compilado y luego generado.
	 * 
	 * @param fileReport
	 *            Ubicacion del archivo que define la estructura del reporte.
	 *            Debe ser el nombre de la dependencia, luego del directorio,
	 *            seguido del nombre del jrxml. <br>
	 *            <b>Por ejemplo</b><br>
	 *            <ol>
	 *            <li><b>identificacion/persona/report.jrxml</b>
	 *            </ol>
	 * @param dataSource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param params
	 *            Parametros especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 */
	public static <T> void exportReportStatic(String fileReport,
			JRDataSource dataSource, Map<String, Object> params, String type) {

		JasperPrint jasperPrint;
		try {
			jasperPrint = JasperFillManager.fillReport(compile(fileReport),
					getDetailsReport(params), dataSource);

			generate(jasperPrint, params, dataSource, type);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que genera el reporte fisicamente.
	 * 
	 * @param jasperPrint
	 *            Estructura del reporte a generar
	 * @param params
	 *            Parametros del reporte
	 * @param datasource
	 *            Datasource que contiene la lista filtrada de acuerdo a los
	 *            criterios de seleccion.
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 */
	private static void generate(JasperPrint jasperPrint,
			Map<String, Object> params, JRDataSource datasource, String type) {

		try {

			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();

			if (type.equalsIgnoreCase(EXPORT_TYPE_EXCEL)) {
				httpServletResponse.setContentType(MEDIA_TYPE_EXCEL);
				httpServletResponse.setHeader("Content-Disposition",
						"attachment; filename=" + params.get("titleReport")
								+ "." + EXPORT_TYPE_EXCEL);

				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						httpServletResponse.getOutputStream());

				exporter.exportReport();
			}

			if (type.equalsIgnoreCase(EXPORT_TYPE_PDF)) {
				httpServletResponse.setContentType(MEDIA_TYPE_PDF);
				httpServletResponse.setHeader("Content-Disposition",
						"attachment; filename=" + params.get("titleReport"));

				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						httpServletResponse.getOutputStream());

				exporter.exportReport();

			}

			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	/**
	 * * Metodo que compila un archivo .jrxml<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>HolaMundo</b>, retorna [Hola,Mundo]
	 * </ol>
	 * 
	 * 1. fileReport.jrxml, retorna fileReport.jasper * * @param fileReport * @return
	 * archivo compilado
	 */
	private static JasperReport compile(String fileReport) {

		try {
			ClassPathResource resource = new ClassPathResource(FILE_LOCATION
					+ fileReport);
			return JasperCompileManager
					.compileReport(resource.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
