/**
 * /**
 * 
 * @DynamicUtils 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.reports;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.springframework.stereotype.Component;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.SIGHReportBlockSign.Sign;
import py.una.med.base.reports.SIGHReportDetails.Detail;
import py.una.med.base.util.I18nHelper;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCrosstab;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.builders.SubReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.Subreport;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

/**
 * Clase que provee las funcionalidades basicas para generar reportes dinamicos.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.5 19/03/2013
 */
@Component
public final class DynamicUtils {

	/**
	 * Ubicacion del template base dentro de la aplicacion.
	 */
	private static final String FILE_PORTRAIT_LOCATION = "report/base/PageBaseReportPortrait.jrxml";
	private static final String FILE_LOCATION = "report/";
	private static final String FILE_LANDSCAPE_LOCATION = "report/base/PageBaseReportLandscape.jrxml";

	private static final int BIG = 12;

	/**
	 * Metodo que crea un reporte dinamico(en memoria), utilizando el template
	 * base para la configuracion general. Se utiliza para los reportes simples.
	 * 
	 * @param columns
	 *            Lista de columnas -> [header, field], que deben ser generadas
	 *            de forma dinamica.
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return Reporte dinamico estructurado listo para ser generado
	 * @throws ReportException
	 */
	public <T> DynamicReport buildReportSimple(List<Column> columns,
			Class<T> clazz) throws ReportException {

		FastReportBuilder structReport = new FastReportBuilder();

		setTemplatePortrait(structReport);
		setWhenNotData(structReport);

		buildColumnHeader(structReport, columns, clazz);

		structReport.setUseFullPageWidth(true);

		return structReport.build();
	}

	/**
	 * Metodo que crea un reporte dinamico(en memoria), utilizando el template
	 * base para la configuracion general. Se utiliza para los reportes simples,
	 * cuya grilla no define los atributos fisicos de la entidad, sino son
	 * atributos transformados.
	 * 
	 * @param columns
	 *            Lista de columnas -> [header, field] que deben ser generadas
	 *            de forma dinamica.
	 * @return Reporte dinamico estructurado listo para ser generado
	 * @throws ReportException
	 */
	public <T> DynamicReport buildReportSimple(List<Column> columns)
			throws ReportException {

		FastReportBuilder structReport = new FastReportBuilder();

		setTemplatePortrait(structReport);
		setWhenNotData(structReport);

		addColumn(structReport, columns);

		structReport.setDefaultStyles(null, null, getStyleColumnHeader(), null);

		structReport.setUseFullPageWidth(true);

		return structReport.build();
	}

	/**
	 * Construye un reporte dinamico(maestro), el cual posee una lista de
	 * bloques, donde cada bloque es una lista de fields.
	 * 
	 * @param blocks
	 *            bloques del reporte que son solo del tipo field, es decir que
	 *            posee columnas horizontales(label,value)
	 * @return reporte dinamico con bloques del tipo fields
	 * @throws ReportException
	 */
	public <T> DynamicReport buidReportFields(List<SIGHReportBlock> blocks)
			throws ReportException {

		FastReportBuilder structReport = new FastReportBuilder();
		setTemplatePortrait(structReport);
		setWhenNotData(structReport);

		addBlocks(structReport, blocks);

		structReport.setUseFullPageWidth(true);

		return structReport.build();
	}

	public <T> DynamicReport buidReportBlockGrid(
			List<SIGHReportBlockGrid> blocks) throws ReportException {

		FastReportBuilder structReport = new FastReportBuilder();
		setTemplatePortrait(structReport);
		setWhenNotData(structReport);

		addBlocksGrid(structReport, blocks);

		structReport.setUseFullPageWidth(true);

		return structReport.build();
	}

	/**
	 * 
	 * Construye un reporte dinamico (maestro) formado por bloques del tipo
	 * field y del tipo firma, es decir un reporte que posee una lista de
	 * columnas horizontales y al final del reporte posee una serie de firmas
	 * (se aplica a los reportes que necesitan ser firmados).
	 * 
	 * @param blocks
	 *            bloques del reporte que son solo del tipo field, es decir que
	 *            posee columnas horizontales(label,value)
	 * @param signs
	 *            bloques del reporte que son solo del tipo firma, es decir que
	 *            posee uaa lista de firmas que deben ser agregadas al final del
	 *            reporte
	 * @return
	 * @throws ReportException
	 */
	public <T> DynamicReport buidReportFields(List<SIGHReportBlock> blocks,
			List<SIGHReportBlockSign> signs) throws ReportException {

		FastReportBuilder structReport = new FastReportBuilder();
		setTemplatePortrait(structReport);
		setWhenNotData(structReport);

		addBlocks(structReport, blocks);
		addBlocksSign(structReport, signs);

		structReport.setUseFullPageWidth(true);

		return structReport.build();
	}

	/**
	 * Metodo que crea un reporte dinamico, utilizando un template base para la
	 * configuracion general, los atributos de la cabecera tambien son generados
	 * dinamicamente. Se utiliza para los reportes del tipo cabecera-detalle.
	 * 
	 * @param report
	 *            Estructura que contiene la definicion y datos necesarios para
	 *            generar el reporte.
	 * @param align
	 *            Alineacion con la cual se desea visualizar el reporte
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return Reporte dinamico estructurado listo para ser generado
	 * @throws ReportException
	 */
	public <T> DynamicReport buildReportDetail(SIGHReportDetails report,
			Align align, Class<T> clazz) throws ReportException {

		FastReportBuilder structReportHead = new FastReportBuilder();

		setAlignReport(structReportHead, align);

		setWhenNotData(structReportHead);

		buildColumnHeader(structReportHead, report.getColumns(), clazz);

		if (!(report.getDetails() == null)) {
			addDetails(structReportHead, report, clazz);
		}

		structReportHead.setUseFullPageWidth(true);

		return structReportHead.build();

	}

	/**
	 * Metodo que crea un reporte dinamico, utilizando un template especifico
	 * para la configuracion referente a los atributos de la cabecera. Se
	 * utiliza para los reportes del tipo cabecera-detalle.
	 * 
	 * @param path
	 *            Ubicacion del template que se desea adicionar al reporte. Debe
	 *            ser el nombre de la dependencia,luego el nombre del
	 *            directorio, seguido del nombre del jrxml.<br>
	 *            <b>Por ejemplo</b><br>
	 *            <ol>
	 *            <li><b>identificacion/persona/detallesPersona.jrxml</b>
	 *            </ol>
	 * @param report
	 *            Estructura que contiene la definicion y datos necesarios para
	 *            generar el reporte.
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return
	 * @throws ReportException
	 */
	public <T> DynamicReport buildReportDetail(String path,
			SIGHReportDetails report, Class<T> clazz) throws ReportException {

		FastReportBuilder structReportHead = new FastReportBuilder();
		setTemplate(path, structReportHead);
		setWhenNotData(structReportHead);

		addDetails(structReportHead, report, clazz);

		structReportHead.setUseFullPageWidth(true);

		return structReportHead.build();

	}

	/**
	 * Agrega una lista de detalles al reporte cabecera.
	 * 
	 * @param structReportHead
	 *            Structura del reporte principal
	 * @param report
	 *            Estructura que contiene la definicion y datos necesarios para
	 *            generar el reporte.
	 * @param clazz
	 *            Clase de la entidad principal sobre la cual se desea realizar
	 *            el reporte
	 * @return
	 * @throws ReportException
	 */
	public <T> FastReportBuilder addDetails(FastReportBuilder structReportHead,
			SIGHReportDetails report, Class<T> clazz) throws ReportException {

		Style titleStyle = new Style();
		titleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		for (Detail detail : report.getDetails()) {
			structReportHead.addField(detail.getField(), List.class.getName());

			FastReportBuilder structSubreport = new FastReportBuilder();
			structSubreport.setTitle(detail.getTitle());
			structSubreport.setTitleStyle(titleStyle);
			structSubreport.setUseFullPageWidth(true);

			setWhenNotData(structSubreport);

			Class<?> clazzDetail = getClazzDetail(clazz, detail);

			buildColumnDetail(structSubreport, detail.getColumns(), clazzDetail);

			DynamicReport subreport = structSubreport.build();

			structReportHead.addConcatenatedReport(subreport,
					new ClassicLayoutManager(), detail.getField(),
					DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD,
					DJConstants.DATA_SOURCE_TYPE_COLLECTION);
		}
		return structReportHead;
	}

	/**
	 * Genera un reporte utilizando crosstab.
	 * 
	 * @param crosstab
	 *            Crosstab estructurado a ser agregado al reporte.
	 * @return
	 */
	public DynamicReport builReportCrossTab(DJCrosstab crosstab)
			throws ClassNotFoundException {

		FastReportBuilder structReport = new FastReportBuilder();
		setTemplateLandScape(structReport);

		structReport.addHeaderCrosstab(crosstab).setUseFullPageWidth(true);

		setWhenNotData(structReport);

		return structReport.build();

	}

	/**
	 * Metodo que genera las columnas principales de un reporte de forma
	 * dinamica,ademas configura los estilos correspondientes.
	 * 
	 * @param structReport
	 *            Estructura del reporte
	 * @param columns
	 *            Lista de columnas que deben ser generadas
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return Reporte con las correspondientes columnas generadas y estilos
	 *         aplicados.
	 * @throws ReportException
	 */
	private <T> FastReportBuilder buildColumnHeader(
			FastReportBuilder structReport, List<Column> columns, Class<T> clazz)
			throws ReportException {

		addColumn(structReport, columns, clazz);

		structReport.setDefaultStyles(null, null, getStyleColumnHeader(), null);
		return structReport;
	}

	/**
	 * Metodo que genera las columnas secundarias correspondientes a los
	 * subreportes de forma dinamica, ademas configura los estilos
	 * correspondientes.
	 * 
	 * @param structReport
	 *            Estructura del reporte
	 * @param columns
	 *            Lista de columnas que deben ser generadas
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return Reporte con las correspondientes columnas generadas y estilos
	 *         aplicados
	 * @throws ReportException
	 */
	private FastReportBuilder buildColumnDetail(FastReportBuilder structReport,
			List<Column> columns, Class<?> clazz) throws ReportException {

		if (clazz.equals(Object.class)) {
			addColumn(structReport, columns);
		} else {
			addColumn(structReport, columns, clazz);
		}

		structReport.setDefaultStyles(getStyleTitle(), null,
				getStyleColumnHeaderDetails(), null);
		return structReport;
	}

	/**
	 * Metodo que obtiene el tipo de dato de cada columna que debe ser generada
	 * de forma dinamica y finalmente agrega las columnas correctamente
	 * definidas al reporte.
	 * 
	 * @param structReport
	 *            Estructura del reporte
	 * @param columns
	 *            Lista de columnas que deben ser generadas
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return Reporte con las correspondientes columnas generadas
	 * @throws ReportException
	 */
	private <T> FastReportBuilder addColumn(FastReportBuilder structReport,
			List<Column> columns, Class<T> clazz) throws ReportException {

		for (Column column : columns) {
			try {

				structReport.addColumn(column.getTitle(), column.getField(),
						Object.class.getName(), column.getWidth());

			} catch (ClassNotFoundException e) {
				throw new ReportException(e);
			}
		}
		return structReport;
	}

	/**
	 * Agrega la lista de bloques del tipo field(label,value)al reporte maestro
	 * 
	 * @param structReport
	 *            estructura del reporte dinamico
	 * @param blocks
	 *            bloques que son solo del tipo field, es decir que posee
	 *            columnas horizontales(label,value)
	 * @return reporte dinamico formado con los bloques recibidos como parametro
	 * @throws ReportException
	 */
	public <T> FastReportBuilder addBlocks(FastReportBuilder structReport,
			List<SIGHReportBlock> blocks) throws ReportException {

		for (SIGHReportBlock block : blocks) {
			buildBlock(structReport, block);
		}

		return structReport;
	}

	public <T> FastReportBuilder addBlocksGrid(FastReportBuilder structReport,
			List<SIGHReportBlockGrid> blocks) throws ReportException {

		for (SIGHReportBlockGrid block : blocks) {
			buildBlockGrid(structReport, block);
		}

		return structReport;
	}

	/**
	 * Agrega la lista de bloques del tipo firma al reporte maestro
	 * 
	 * @param structReport
	 *            estructura del reporte dinamico
	 * @param blocks
	 *            bloques que son solo del tipo firma, es decir que posee uaa
	 *            lista de firmas que deben ser agregadas al final del reporte
	 * @return reporte dinamico formado con los bloques recibidos como parametro
	 * @throws ReportException
	 */
	public <T> FastReportBuilder addBlocksSign(FastReportBuilder structReport,
			List<SIGHReportBlockSign> blocks) throws ReportException {

		for (SIGHReportBlockSign block : blocks) {
			buildBlockSign(structReport, block);
		}

		return structReport;
	}

	/**
	 * Metodo que agrega las columnas al reporte, el tipo de las columnas
	 * generadas es del tipo Object, se utiliza para el caso que el objeto
	 * utilizado dentro del reporte no sea un objeto fisico existente, sino uno
	 * creado en memoria.
	 * 
	 * @param structReport
	 *            Estructura del reporte
	 * @param columns
	 *            Lista de columnas que deben ser generadas
	 * @return Reporte con las correspondiente columnas generadas
	 * @throws ReportException
	 */
	private FastReportBuilder addColumn(FastReportBuilder structReport,
			List<Column> columns) throws ReportException {

		for (Column column : columns) {
			try {
				structReport.addColumn(column.getTitle(), column.getField(),
						Object.class.getName(), column.getWidth());

			} catch (ClassNotFoundException e) {
				throw new ReportException(e);
			}

		}
		return structReport;
	}

	/**
	 * Construye un bloque del tipo field dinamico y lo agrega al reporte.
	 * 
	 * @param structReportHead
	 *            estructura del reporte maestro(principal)
	 * @param block
	 *            bloque del tipo field, es decir que posee columnas
	 *            horizontales(label,value)
	 * @return reporte con el bloque construido
	 * @throws ReportException
	 */
	public FastReportBuilder buildBlock(FastReportBuilder structReportHead,
			SIGHReportBlock block) throws ReportException {

		FastReportBuilder structBlockReport = new FastReportBuilder();
		structBlockReport.setDefaultStyles(getStyleTitleTransparentUnderline(),
				getStyleTitle(), getStyleColumnHeaderBlank(), null);

		structBlockReport.setUseFullPageWidth(true);
		setWhenNotData(structBlockReport);

		try {

			structBlockReport.setTitle(block.getTitle());
			structBlockReport.setHeaderHeight(0);
			structBlockReport.addColumn("", "label", String.class,
					block.getWidthLabel());
			structBlockReport.addColumn("", "value", String.class,
					block.getWidthValue());

			Subreport subReport = new SubReportBuilder()
					.setDataSource(DJConstants.DATA_SOURCE_ORIGIN_PARAMETER,
							DJConstants.DATA_SOURCE_TYPE_JRDATASOURCE,
							block.getNameDataSource())
					.setDynamicReport(structBlockReport.build(),
							new ClassicLayoutManager()).build();

			structReportHead.addConcatenatedReport(subReport);

			return structReportHead;

		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		}

	}

	public FastReportBuilder buildBlockGrid(FastReportBuilder structReportHead,
			SIGHReportBlockGrid block) throws ReportException {

		FastReportBuilder structBlockReport = new FastReportBuilder();
		structBlockReport.setDefaultStyles(getStyleTitle(), null,
				getStyleColumnHeader(), null);

		structBlockReport.setUseFullPageWidth(true);
		setWhenNotData(structBlockReport);

		structBlockReport.setTitle(block.getTitle());
		addColumn(structBlockReport, block.getColumns());

		Subreport subReport = new SubReportBuilder()
				.setDataSource(DJConstants.DATA_SOURCE_ORIGIN_PARAMETER,
						DJConstants.DATA_SOURCE_TYPE_JRDATASOURCE,
						block.getNameDataSource())
				.setDynamicReport(structBlockReport.build(),
						new ClassicLayoutManager()).build();

		structReportHead.addConcatenatedReport(subReport);

		return structReportHead;

	}

	/**
	 * Construye un bloque del tipo firma dinamico y lo agrega al reporte.
	 * 
	 * @param structReportHead
	 *            estructura del reporte maestro(principal)
	 * @param block
	 *            bloque del tipo firma, es decir que posee una lista de firmas
	 *            que deben ser agregadas al final del reporte
	 * @return reporte con el bloque construido
	 * @throws ReportException
	 */
	public FastReportBuilder buildBlockSign(FastReportBuilder structReportHead,
			SIGHReportBlockSign block) throws ReportException {

		FastReportBuilder structBlockReport = new FastReportBuilder();
		structBlockReport.setDefaultStyles(getStyleTitle(), getStyleTitle(),
				getStyleColumnHeaderTransparentUnderlineTop(), null);

		structBlockReport.setUseFullPageWidth(true);
		setWhenNotDataEmpty(structBlockReport);

		try {

			for (Sign sign : block.getSigns()) {

				structBlockReport.addColumn(sign.getValue(), "label",
						String.class, sign.getWidth());
				if (!sign.equals(block.getSigns().get(
						block.getSigns().size() - 1))) {
					structBlockReport.addColumn(buildColumnSeparator());
				}

			}

			structReportHead.addConcatenatedReport(buildReportSeparator());

			Subreport subReport = new SubReportBuilder()
					.setDataSource("")
					.setDynamicReport(structBlockReport.build(),
							new ClassicLayoutManager()).build();
			structReportHead.addConcatenatedReport(subReport);

			return structReportHead;

		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		}

	}

	private AbstractColumn buildColumnSeparator() {

		return ColumnBuilder.getNew().setStyle(getStyleColumnHeader())
				.setColumnProperty("label", String.class).setWidth(10).build();
	}

	private Subreport buildReportSeparator() {

		FastReportBuilder structReport = new FastReportBuilder();
		structReport.setDefaultStyles(getStyleTitle(), getStyleTitle(),
				getStyleColumnHeaderTransparentUnderlineTop(), null);
		structReport.addColumn(buildColumnSeparator());
		structReport.setUseFullPageWidth(true);
		setWhenNotDataEmpty(structReport);

		Subreport subReport = new SubReportBuilder()
				.setDataSource("")
				.setDynamicReport(structReport.build(),
						new ClassicLayoutManager()).build();
		return subReport;
	}

	/**
	 * Metodo que obtiene la clase de cada detalle que sera incorporado en los
	 * reportes del tipo cabecera-detalle.
	 * 
	 * @param clazz
	 *            Clase de la cabecera
	 * @param detail
	 *            Detalle que se desea agregar al reporte
	 * @return Clase del detalle
	 * @throws ReportException
	 */
	private Class<?> getClazzDetail(Class<?> clazz, Detail detail)
			throws ReportException {

		Class<?> clazzDetail = null;
		try {
			if (detail.getField().contains(".")) {
				clazzDetail = Object.class;
			} else {
				Field field = clazz.getDeclaredField(detail.getField());
				clazzDetail = (Class<?>) ((ParameterizedType) field
						.getGenericType()).getActualTypeArguments()[0];
			}
		} catch (NoSuchFieldException e) {
			throw new ReportException(e);
		}
		return clazzDetail;
	}

	/**
	 * Metodo que define el estilo que debe ser aplicado al titulo principal de
	 * los reportes.
	 * 
	 * @return StyleTitle
	 */
	public Style getStyleTitle() {

		Style styleTitle = new Style();
		styleTitle.setFont(Font.ARIAL_MEDIUM_BOLD);
		return styleTitle;
	}

	/**
	 * Metodo que define el estilo para las columnas principales de los
	 * reportes.
	 * 
	 * @return StyleColumnHeader
	 */

	public Style getStyleColumnHeader() {

		Style styleColumns = new Style();
		styleColumns.setTransparent(false);
		styleColumns.setBackgroundColor(Color.darkGray);
		styleColumns.setFont(Font.ARIAL_SMALL_BOLD);
		styleColumns.setTextColor(Color.white);
		styleColumns.setHorizontalAlign(HorizontalAlign.CENTER);
		styleColumns.setVerticalAlign(VerticalAlign.MIDDLE);
		return styleColumns;
	}

	/**
	 * @return
	 */
	private Style getStyleColumnHeaderBlank() {

		Style styleColumns = new Style();
		styleColumns.setTransparent(true);
		return styleColumns;
	}

	public Style getStyleTitleTransparentUnderline() {

		Style styleColumns = new Style();
		styleColumns.setFont(new Font(BIG, "Arial", false, false, true));
		styleColumns.setHorizontalAlign(HorizontalAlign.LEFT);
		styleColumns.setTransparent(true);
		return styleColumns;
	}

	public Style getStyleColumnHeaderTransparentUnderlineTop() {

		Style styleColumns = new Style();
		styleColumns.setBorderTop(Border.PEN_1_POINT());
		styleColumns.setHorizontalAlign(HorizontalAlign.CENTER);
		styleColumns.setTransparent(true);
		return styleColumns;
	}

	/**
	 * Metodo que define el estilo para las columnas secundarias de los
	 * reportes.
	 * 
	 * @return StyleColumnHeader
	 */

	public Style getStyleColumnHeaderDetails() {

		Style styleColumns = new Style();
		styleColumns.setTransparent(false);
		styleColumns.setBackgroundColor(Color.gray);
		styleColumns.setFont(Font.ARIAL_SMALL_BOLD);
		styleColumns.setTextColor(Color.white);
		styleColumns.setHorizontalAlign(HorizontalAlign.CENTER);
		styleColumns.setVerticalAlign(VerticalAlign.MIDDLE);
		return styleColumns;
	}

	/**
	 * Proporciona el estilo para el pivot utilizado al realizar el crosstab.
	 * 
	 * @return
	 */

	public Style getStyleColumnPivot() {

		return new Style();
	}

	/**
	 * Agrega al reporte un mensaje personalizado en el caso de que el reporte
	 * no tenga datos que puedan ser visualizados.
	 * 
	 * @param structReport
	 * @return
	 */
	private FastReportBuilder setWhenNotData(FastReportBuilder structReport) {

		structReport.setWhenNoData(
				I18nHelper.getMessage("BASE_REPORT_NOT_DATA"), null);
		return structReport;
	}

	private FastReportBuilder setWhenNotDataEmpty(FastReportBuilder structReport) {

		structReport.setWhenNoData(" ", null);
		return structReport;
	}

	public FastReportBuilder setAlignReport(FastReportBuilder report,
			Align align) {

		if (align.equals(Align.HORIZONTAL)) {
			setTemplateLandScape(report);
		} else {
			setTemplatePortrait(report);

		}
		return report;

	}

	/**
	 * Adiciona al reporte el template base con orientación horizontal.
	 * 
	 * @param report
	 * @return
	 */
	private FastReportBuilder setTemplatePortrait(FastReportBuilder report) {

		report.setTemplateFile(FILE_PORTRAIT_LOCATION, true, false, true, true);
		return report;
	}

	/**
	 * Adiciona al reporte el template base con orientación vertical.
	 * 
	 * @param report
	 * @return
	 */
	private FastReportBuilder setTemplateLandScape(FastReportBuilder report) {

		report.setTemplateFile(FILE_LANDSCAPE_LOCATION, true, false, true, true);
		return report;
	}

	/**
	 * Adiciona al reporte un template especifico, definido por el usuario.
	 * 
	 * @param path
	 *            Ubicacion del template que se desea adicionar al reporte. Debe
	 *            ser el nombre del directorio, seguido del nombre del jrxml.
	 * @param report
	 * @return
	 */
	private FastReportBuilder setTemplate(String path, FastReportBuilder report) {

		report.setTemplateFile(FILE_LOCATION + path, true, false, true, true);
		return report;
	}

}
