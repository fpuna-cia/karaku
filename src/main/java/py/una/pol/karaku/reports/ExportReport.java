/**
 * @ExportReport 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.reports;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.security.AuthorityController;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.Util;
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
@Component
public class ExportReport {

	private static final String EXPORT_TYPE_EXCEL = "xls";
	private static final String EXPORT_TYPE_PDF = "pdf";
	private static final String FILE_LOCATION = "report/";
	private static final String FILE_LOCATION_TEMPLATE = "report/base/";
	private static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	private static final String MEDIA_TYPE_PDF = "application/pdf";

	@Autowired(required = false)
	private Util util;

	@Autowired
	private DynamicUtils dynamicUtils;

	@Autowired
	private AuthorityController authorityController;

	/**
	 * * Metodo que compila un archivo .jrxml<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>HolaMundo</b>, retorna [Hola,Mundo]
	 * </ol>
	 * 
	 * 1. fileReport.jrxml, retorna fileReport.jasper * * @param fileReport * @return
	 * archivo compilado
	 * 
	 * @throws ReportException
	 */
	private JasperReport compile(String fileReport) throws ReportException {

		try {
			ClassPathResource resource = new ClassPathResource(FILE_LOCATION
					+ fileReport);
			return JasperCompileManager
					.compileReport(resource.getInputStream());
		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
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
	 * @throws ReportException
	 */
	public void exportAvancedReport(DynamicReport report,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(report,
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public void blank(Align align, boolean criteria,
			Map<String, Object> params, String type) throws ReportException {

		exportAvancedReport(dynamicUtils.getInstanceByAlign(align, criteria)
				.build(), new DRDataSource(), params, type);

	}

	public void exportAvancedReport(HttpServletResponse httpServletResponse,
			DynamicReport report, JRDataSource dataSource,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(report,
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
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
	 * @throws ReportException
	 */
	public <T> void exportDetailReport(SIGHReportDetails report, Align align,
			boolean criteria, Class<T> clazz, JRDataSource dataSource,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(dynamicUtils
					.buildReportDetail(report, align, criteria, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
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
	 * @throws ReportException
	 */

	public <T> void exportDetailReport(String path, SIGHReportDetails report,
			Class<T> clazz, JRDataSource dataSource,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportDetail(path, report, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportDetailReport(HttpServletResponse httpServletResponse,
			String path, SIGHReportDetails report, Class<T> clazz,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportDetail(path, report, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
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
	 * @throws ReportException
	 */
	public <T> void exportReportStatic(String fileReport,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = JasperFillManager.fillReport(compile(fileReport),
					getDetailsReport(params), dataSource);

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> JasperPrint generateReport(String fileReport,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = JasperFillManager.fillReport(compile(fileReport),
					getDetailsReport(params), dataSource);
			return jasperPrint;
		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportReportStatic(HttpServletResponse httpServletResponse,
			String fileReport, JRDataSource dataSource,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = JasperFillManager.fillReport(compile(fileReport),
					getDetailsReport(params), dataSource);

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
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
	 * @throws ReportException
	 */
	public <T> void exportSimpleReport(List<Column> columns, Class<T> clazz,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportSimple(columns, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportSimpleReport(HttpServletResponse httpServletResponse,
			List<Column> columns, Class<T> clazz, JRDataSource dataSource,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportSimple(columns, clazz),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	/**
	 * Genera un reporte dinamico formado por bloques del tipo
	 * field(label,value), es decir un reporte que posee una lista de columnas
	 * horizontales.
	 * 
	 * @param blocks
	 *            bloques del reporte que son solo del tipo field
	 * @param params
	 *            especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @return reporte generado
	 * @throws ReportException
	 */
	public <T> void exportReportBlock(Align align, boolean criteria,
			List<SIGHReportBlock> blocks, Map<String, Object> params,
			String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportBlock(align, criteria, blocks),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportReportBlock(boolean criteria,
			List<SIGHReportBlock> blocks, Map<String, Object> params,
			String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(dynamicUtils
					.buildReportBlock(Align.VERTICAL, criteria, blocks),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportReportFields(boolean criteria,
			HttpServletResponse httpServletResponse,
			List<SIGHReportBlock> blocks, Map<String, Object> params,
			String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(dynamicUtils
					.buildReportBlock(Align.VERTICAL, criteria, blocks),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportReportGrid(List<SIGHReportBlockGrid> blocks,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buidReportBlockGrid(blocks),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportReportGrid(HttpServletResponse httpServletResponse,
			List<SIGHReportBlockGrid> blocks, Map<String, Object> params,
			String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buidReportBlockGrid(blocks),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	/**
	 * Genera un reporte dinamico formado por bloques del tipo field y del tipo
	 * firma, es decir un reporte que posee una lista de columnas horizontales y
	 * al final del reporte posee una serie de firmas (se aplica a los reportes
	 * que necesitan ser firmados).
	 * 
	 * @param blocks
	 *            bloques del reporte que son solo del tipo field, es decir que
	 *            posee columnas horizontales(label,value)
	 * @param signs
	 *            bloques del reporte que son solo del tipo firma, es decir que
	 *            posee uaa lista de firmas que deben ser agregadas al final del
	 *            reporte
	 * @param params
	 *            especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @return reporte generado
	 * @throws ReportException
	 */
	public <T> void exportReportFields(boolean criteria,
			List<SIGHReportBlock> blocks, List<SIGHReportBlockSign> signs,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buidReportFields(criteria, blocks, signs),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportReportFields(HttpServletResponse httpServletResponse,
			List<SIGHReportBlock> blocks, List<SIGHReportBlockSign> signs,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buidReportFields(true, blocks, signs),
					new ClassicLayoutManager(), new JREmptyDataSource(),
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
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
	 * @throws ReportException
	 */
	public <T> void exportSimpleReport(List<Column> columns,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportSimple(columns),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(getServletResponse(), jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	public <T> void exportSimpleReport(HttpServletResponse httpServletResponse,
			List<Column> columns, JRDataSource dataSource,
			Map<String, Object> params, String type) throws ReportException {

		JasperPrint jasperPrint;
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(
					dynamicUtils.buildReportSimple(columns),
					new ClassicLayoutManager(), dataSource,
					getDetailsReport(params));

			generate(httpServletResponse, jasperPrint, params, type);

		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	protected HttpServletResponse getServletResponse() {

		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
	}

	/**
	 * Metodo que genera el reporte fisicamente.
	 * 
	 * @param jasperPrint
	 *            Estructura del reporte a generar
	 * @param params
	 *            Parametros del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @throws ReportException
	 */
	public void generate(HttpServletResponse httpServletResponse,
			JasperPrint jasperPrint, Map<String, Object> params, String type)
			throws ReportException {

		generate(httpServletResponse, jasperPrint,
				(String) params.get("titleReport"), type);
	}

	public void generate(HttpServletResponse httpServletResponse,
			JasperPrint jasperPrint, String name, String type)
			throws ReportException {

		try {

			if (type.equalsIgnoreCase(EXPORT_TYPE_EXCEL)) {
				httpServletResponse.setContentType(MEDIA_TYPE_EXCEL);
				httpServletResponse.setHeader("Content-Disposition",
						"attachment; filename=" + name + "."
								+ EXPORT_TYPE_EXCEL);

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
						"attachment; filename=" + name);

				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						httpServletResponse.getOutputStream());

				exporter.exportReport();

			}

			// XXX crear lógica de invocación a través de JSF
			if (FacesContext.getCurrentInstance() != null) {
				FacesContext.getCurrentInstance().responseComplete();
			}
		} catch (JRException e) {
			throw new ReportException(e);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	/**
	 * Obtiene los parametros genericos para los reportes simples<br>
	 * 
	 * @return Lista de parametros generales que seran enviados al template
	 *         basico para los reportes
	 */
	private Map<String, Object> getDetailsReport(Map<String, Object> params)
			throws IOException {

		if (params != null) {
			ClassPathResource imagePath = new ClassPathResource(
					FILE_LOCATION_TEMPLATE + "logo.jpg");

			params.put("logo", imagePath.getInputStream());
			params.put("nombreInstitucion",
					getMessage("BASE_REPORT_NAME_INSTITUTION"));
			params.put("nombreEstablecimiento",
					getMessage("BASE_REPORT_NAME_ESTABLISHMENT"));
			params.put("date", getMessage("BASE_REPORT_DATE"));
			params.put("time", getMessage("BASE_REPORT_TIME"));
			params.put("selectionCriteria",
					getMessage("BASE_REPORT_SELECTION_CRITERIA"));
			params.put("user", getMessage("BASE_REPORT_USER"));
			params.put("userName", getUserName());
			params.put("nameSystem", getNameSystem());
			params.put("page", getMessage("BASE_REPORT_PAGE"));
			params.put("pageThe", getMessage("BASE_REPORT_PAGE_THE"));
		}
		return params;
	}

	protected String getNameSystem() {

		return util.getNameSystem();
	}

	private String getMessage(String key) {

		return I18nHelper.getSingleton().getString(key);
	}

	protected String getUserName() {

		return authorityController.getUsername();
	}
}
