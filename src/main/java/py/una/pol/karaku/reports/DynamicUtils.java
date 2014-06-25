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

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.entity.annotations.Time;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.reports.KarakuReportBlockSign.Sign;
import py.una.pol.karaku.reports.KarakuReportDetails.Detail;
import py.una.pol.karaku.util.FormatProvider;
import py.una.pol.karaku.util.I18nHelper;
import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
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

	private static final int BIG = 12;
	private static final int WIDTH_SEPARATOR = 10;
	private static final int WIDTH_DATE = 10;
	/**
	 * Ubicacion del template base dentro de la aplicacion.
	 */
	private static final String FILE_PORTRAIT_CRITERIA_LOCATION = "report/base/PortraitWithCriteria.jrxml";
	private static final String FILE_PORTRAIT_LOCATION = "report/base/Portrait.jrxml";
	private static final String FILE_LOCATION = "report/";
	private static final String FILE_LANDSCAPE_CRITERIA_LOCATION = "report/base/LandscapeWithCriteria.jrxml";
	private static final String FILE_LANDSCAPE_LOCATION = "report/base/Landscape.jrxml";
	private static final String NOT_DATA = "BASE_REPORT_NOT_DATA";

	@Autowired
	private FormatProvider fp;

	/**
	 * Crea una estructura de reporte configurada con las características
	 * generales sin template alguno.
	 * 
	 * @return Reporte con las configuraciones generales
	 */
	public FastReportBuilder newInstance() {

		FastReportBuilder structReport = new FastReportBuilder();
		this.setWhenNotData(structReport);
		structReport.setUseFullPageWidth(true);

		return structReport;
	}

	/**
	 * Crea una estructura de reporte vertical configurada con las
	 * características generales.
	 * 
	 * <p>
	 * Este reporte tendra la sección de criterios
	 * </p>
	 * 
	 * @return Reporte con las configuraciones generales
	 */
	public FastReportBuilder newInstancePortrait() {

		return this.newInstancePortrait(true);
	}

	/**
	 * Crea una estructura de reporte vertical configurada con las
	 * características generales.
	 * 
	 * @param criteria
	 *            <li><b><code>true</code></b> Sí se desea que el reporte tenga
	 *            la sección de criterios. <li><b><code>false</code></b> Caso
	 *            contrario.
	 * 
	 * 
	 * @return Reporte con las configuraciones generales
	 */
	public FastReportBuilder newInstancePortrait(boolean criteria) {

		FastReportBuilder structReport = this.newInstance();
		this.setTemplatePortrait(structReport, criteria);
		return structReport;
	}

	/**
	 * Crea una estructura de reporte horizontal configurada con las
	 * características generales.
	 * 
	 * @param criteria
	 *            <li><b><code>true</code></b> Sí se desea que el reporte tenga
	 *            la sección de criterios. <li><b><code>false</code></b> Caso
	 *            contrario.
	 * @return Reporte con las configuraciones generales
	 */
	public FastReportBuilder newInstanceLandScape(boolean criteria) {

		FastReportBuilder structReport = this.newInstance();
		this.setTemplateLandScape(structReport, criteria);
		return structReport;

	}

	/**
	 * Metodo que crea un reporte dinamico(en memoria), utilizando el template
	 * base (con la sección de criterios)para la configuracion general. Se
	 * utiliza para los reportes simples.
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

		FastReportBuilder structReport = this.newInstancePortrait(true);
		this.buildColumnHeader(structReport, columns, clazz);

		return structReport.build();
	}

	/**
	 * Método que crea un reporte dinámico(en memoria), utilizando el template
	 * base (con la sección de criterios) para la configuracion general. Se
	 * utiliza para los reportes simples, cuya grilla no define los atributos
	 * fisicos de la entidad, sino son atributos transformados.
	 * 
	 * @param columns
	 *            Lista de columnas -> [header, field] que deben ser generadas
	 *            de forma dinamica.
	 * @return Reporte dinamico estructurado listo para ser generado
	 * @throws ReportException
	 */
	public <T> DynamicReport buildReportSimple(List<Column> columns)
			throws ReportException {

		FastReportBuilder structReport = this.newInstancePortrait(true);
		this.buildColumnHeader(structReport, columns);

		return structReport.build();
	}

	/**
	 * Construye un reporte dinamico, el cual posee una lista de bloques, donde
	 * cada bloque es una lista de fields o una grilla de elementos.
	 * 
	 * @param blocks
	 *            bloques del reporte.
	 * @return reporte dinamico con bloques del tipo fields
	 * @throws ReportException
	 */

	public <T> DynamicReport buildReportBlock(Align align, boolean criteria,
			List<KarakuReportBlock> blocks) throws ReportException {

		FastReportBuilder structReport = getInstanceByAlign(align, criteria);
		structReport.setAllowDetailSplit(false);

		this.addBlocks(structReport, blocks);

		return structReport.build();
	}

	public <T> DynamicReport buildReportBlock(boolean criteria,
			List<KarakuReportBlock> blocks) throws ReportException {

		FastReportBuilder structReport = this.newInstancePortrait(criteria);
		structReport.setAllowDetailSplit(false);

		this.addBlocks(structReport, blocks);

		return structReport.build();
	}

	public <T> DynamicReport buidReportBlockGrid(
			List<KarakuReportBlockGrid> blocks) throws ReportException {

		FastReportBuilder structReport = this.newInstancePortrait(true);

		this.addBlocksGrid(structReport, blocks);

		return structReport.build();
	}

	/**
	 * 
	 * Construye un reporte dinamico (maestro) formado por bloques del tipo
	 * field y del tipo firma, es decir un reporte que posee una lista de
	 * columnas horizontales y al final del reporte posee una serie de firmas
	 * (se aplica a los reportes que necesitan ser firmados).
	 * 
	 * @param criteria
	 *            <li><b><code>true</code></b> Sí se desea que el reporte tenga
	 *            la sección de criterios. <li><b><code>false</code></b> Caso
	 *            contrario.
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
	public <T> DynamicReport buidReportFields(boolean criteria,
			List<KarakuReportBlock> blocks, List<KarakuReportBlockSign> signs)
			throws ReportException {

		FastReportBuilder structReport = this.newInstancePortrait(criteria);

		this.addBlocks(structReport, blocks);
		this.addBlocksSign(structReport, signs);

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
	 * @param criteria
	 *            <li><b><code>true</code></b> Sí se desea que el reporte tenga
	 *            la sección de criterios. <li><b><code>false</code></b> Caso
	 *            contrario.
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 * @return Reporte dinamico estructurado listo para ser generado
	 * @throws ReportException
	 */
	public <T> DynamicReport buildReportDetail(KarakuReportDetails report,
			Align align, boolean criteria, Class<T> clazz)
			throws ReportException {

		FastReportBuilder structReportHead = this.newInstance();

		this.setAlignReport(structReportHead, align, criteria);

		this.buildColumnHeader(structReportHead, report.getColumns(), clazz);

		if (!(report.getDetails() == null)) {
			this.addDetails(structReportHead, report, clazz);
		}

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
			KarakuReportDetails report, Class<T> clazz) throws ReportException {

		FastReportBuilder structReportHead = this.newInstance();
		this.setTemplate(path, structReportHead);

		this.addDetails(structReportHead, report, clazz);

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
			KarakuReportDetails report, Class<T> clazz) throws ReportException {

		Style titleStyle = new Style();
		titleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		for (Detail detail : report.getDetails()) {
			structReportHead.addField(detail.getField(),
					Collection.class.getName());

			FastReportBuilder structSubreport = this.newInstance();
			structSubreport.setTitle(detail.getTitle());
			structSubreport.setTitleStyle(titleStyle);

			Class<?> clazzDetail = this.getClazzDetail(clazz, detail);

			this.buildColumnDetail(structSubreport, detail.getColumns(),
					clazzDetail);

			DynamicReport subreport = structSubreport.build();

			structReportHead.addConcatenatedReport(subreport,
					new ClassicLayoutManager(), detail.getField(),
					DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD,
					DJConstants.DATA_SOURCE_TYPE_COLLECTION);
		}
		return structReportHead;
	}

	/**
	 * Agrega un subreporte al reporte maestro, donde el dataSource del
	 * subReporte se encuentra en la lista de parametros.
	 * 
	 * @param structReportHead
	 *            Reporte maestro o principal.
	 * @param subReport
	 *            Subreporte que se desea agregar
	 * @param field
	 *            Nombre del parametro donde se encuentra el dataSource del
	 *            subreporte
	 * @return Reporte maestro con el subreporte concatenado
	 * @throws ReportException
	 */
	public FastReportBuilder addDetailByParameter(
			FastReportBuilder structReportHead, FastReportBuilder subReport,
			String field) throws ReportException {

		subReport.setDefaultStyles(this.getStyleTitle(), null,
				this.getStyleColumnHeader(), this.getStyleColumnDetail());

		Subreport subAux = new SubReportBuilder()
				.setDataSource(DJConstants.DATA_SOURCE_ORIGIN_PARAMETER,
						DJConstants.DATA_SOURCE_TYPE_JRDATASOURCE, field)
				.setDynamicReport(subReport.build(), new ClassicLayoutManager())
				.build();

		structReportHead.addConcatenatedReport(subAux);

		return structReportHead;
	}

	/**
	 * Agrega un subreporte al reporte maestro, donde el dataSource del
	 * subReporte se encuentra en un field del tipo <b>List</b> del dataSource
	 * principal, por ejemplo: <li><b>documentos</b> Lista de documentos de una
	 * persona</li>
	 * 
	 * @param structReportHead
	 *            Reporte maestro o principal.
	 * @param subReport
	 *            Subreporte que se desea agregar
	 * @param field
	 *            Nombre del parametro donde se encuentra el dataSource del
	 *            subreporte
	 * @return Reporte maestro con el subreporte concatenado
	 * @throws ReportException
	 */
	public FastReportBuilder addDetailByField(
			FastReportBuilder structReportHead, FastReportBuilder subReport,
			String field) throws ReportException {

		structReportHead.addField(field, Collection.class.getName());
		subReport.setDefaultStyles(this.getStyleTitle(), null,
				this.getStyleColumnHeader(), this.getStyleColumnDetail());

		structReportHead.addConcatenatedReport(subReport.build(),
				new ClassicLayoutManager(), field,
				DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD,
				DJConstants.DATA_SOURCE_TYPE_COLLECTION);

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

		FastReportBuilder structReport = this.newInstanceLandScape(true);

		structReport.addHeaderCrosstab(crosstab).setUseFullPageWidth(true);

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
	public <T> FastReportBuilder buildColumnHeader(
			FastReportBuilder structReport, List<Column> columns, Class<T> clazz)
			throws ReportException {

		if (Object.class.equals(clazz)) {
			addColumn(structReport, columns);
		} else {
			addColumn(structReport, columns, clazz);
		}

		structReport.setDefaultStyles(null, null, this.getStyleColumnHeader(),
				this.getStyleColumnDetail());
		return structReport;
	}

	/**
	 * Metodo que genera las columnas principales de un reporte de forma
	 * dinamica,ademas configura los estilos correspondientes.
	 * 
	 * @param structReport
	 *            Estructura del reporte
	 * @param columns
	 *            Lista de columnas que deben ser generadas
	 * @return Reporte con las correspondientes columnas generadas y estilos
	 *         aplicados.
	 * @throws ReportException
	 */
	public <T> FastReportBuilder buildColumnHeader(
			FastReportBuilder structReport, List<Column> columns)
			throws ReportException {

		this.addColumn(structReport, columns);

		structReport.setDefaultStyles(null, null, this.getStyleColumnHeader(),
				this.getStyleColumnDetail());
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

		if (Object.class.equals(clazz)) {
			addColumn(structReport, columns);
		} else {
			addColumn(structReport, columns, clazz);
		}

		structReport
				.setDefaultStyles(this.getStyleTitle(), null,
						this.getStyleColumnHeaderDetails(),
						this.getStyleColumnDetail());

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

	public <T> FastReportBuilder addColumn(FastReportBuilder structReport,
			List<Column> columns, Class<T> clazz) throws ReportException {

		for (Column column : columns) {
			try {
				if (column.getField().contains(".")) {
					structReport.addColumn(column.getTitle(),
							column.getField(), Object.class.getName(),
							column.getWidth());
				} else {
					Field field = ReflectionUtils.findField(clazz,
							column.getNameField());

					structReport.addColumn(column.getTitle(),
							column.getField(), field.getType().getName(),
							getWidthColumn(column, field),
							this.getStyleColumn(column, field));
				}

			} catch (ClassNotFoundException e) {
				throw new ReportException(e);
			} catch (SecurityException e) {
				throw new ReportException(e);
			}
		}
		return structReport;
	}

	/**
	 * Configura la alineación de las columnas de los reportes.
	 * 
	 * @param field
	 *            Field que se desea alinear
	 * 
	 * @return
	 */
	private HorizontalAlign getAlignColumn(Field field) {

		if (Number.class.isAssignableFrom(field.getType())) {
			return HorizontalAlign.RIGHT;
		} else {
			if (Date.class.isAssignableFrom(field.getType())) {
				return HorizontalAlign.CENTER;
			}
		}
		return HorizontalAlign.LEFT;
	}

	/**
	 * Verifica si la columna es del tipo {@link Date} y coloca un ancho fijo a
	 * la misma.
	 * 
	 * @param column
	 * @param field
	 * @return
	 */
	protected int getWidthColumn(Column column, Field field) {

		if (Date.class.isAssignableFrom(field.getType())) {
			return WIDTH_DATE;
		} else {
			return column.getWidth();
		}
	}

	/**
	 * Obtiene el estilo para una columna en particular, si posee un patron se
	 * establece dicho patron de lo contrario solo la alineación.
	 * 
	 * @param column
	 * @return
	 */
	private Style getStyleColumn(Column column) {

		if (column.getPattern() == null) {
			return this.getStyleColumn(column.getAlign());

		} else {
			return this.getStyleColumn(column.getAlign(), column.getPattern());
		}
	}

	private Style getStyleColumn(HorizontalAlign align) {

		Style style = this.getStyleColumnDetail();
		style.setHorizontalAlign(align);
		return style;
	}

	/**
	 * Construye el estilo de la columna del reporte
	 * 
	 * @param column
	 *            Columna del reporte
	 * @param field
	 *            Field asociado a la columna
	 * @return
	 */
	private Style getStyleColumn(Column column, Field field) {

		Style style = this.getStyleColumnDetail();
		if (column.getPattern() == null) {
			style.setPattern(getPattern(field));
		} else {
			style.setPattern(column.getPattern());
		}
		if (column.getAlign() == null) {
			style.setHorizontalAlign(getAlignColumn(field));
		} else {
			style.setHorizontalAlign(column.getAlign());
		}
		return style;
	}

	/**
	 * Verifica el pattern que se debe aplicar a la columna
	 * 
	 * <li>Si es del tipo {@link Date} se verifica el tipo que se encuentra
	 * asociada a la anotacion {@link Time} para establecer el pattern a
	 * utilizar.
	 * 
	 * @param field
	 */
	protected String getPattern(Field field) {

		if (Date.class.isAssignableFrom(field.getType())) {
			if (field.isAnnotationPresent(Time.class)) {
				switch (field.getAnnotation(Time.class).type()) {
					case DATE:
						return fp.getDateFormat();
					case DATETIME:
						return fp.getDateTimeFormat();
					case TIME:
						return fp.getTimeFormat();
					default:
						break;
				}
			} else {
				return fp.getDateFormat();
			}
		}
		return null;
	}

	private Style getStyleColumn(HorizontalAlign align, String pattern) {

		Style style = this.getStyleColumnDetail();
		style.setHorizontalAlign(align);
		style.setPattern(pattern);
		return style;
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
	public FastReportBuilder addColumn(FastReportBuilder structReport,
			List<Column> columns) throws ReportException {

		try {
			for (Column column : columns) {

				structReport.addColumn(column.getTitle(), column.getField(),
						Object.class.getName(), column.getWidth(),
						getStyleColumn(column));
			}
			return structReport;
		} catch (ClassNotFoundException e) {
			throw new ReportException(e);
		}
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
			List<KarakuReportBlock> blocks) throws ReportException {

		for (KarakuReportBlock block : blocks) {
			// Si es un bloque de fields
			if (block instanceof KarakuReportBlockField) {
				this.buildBlock(structReport, (KarakuReportBlockField) block);
			} else {
				// Si es un bloque de firmas
				if (block instanceof KarakuReportBlockSign) {
					this.buildBlockSign(structReport,
							(KarakuReportBlockSign) block);
				} else {
					// Si es un bloque de grillas
					this.buildBlockGrid(structReport,
							(KarakuReportBlockGrid) block);
				}
			}
		}

		return structReport;
	}

	public <T> FastReportBuilder addBlocksGrid(FastReportBuilder structReport,
			List<KarakuReportBlockGrid> blocks) throws ReportException {

		for (KarakuReportBlockGrid block : blocks) {
			this.buildBlockGrid(structReport, block);
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
			List<KarakuReportBlockSign> blocks) throws ReportException {

		for (KarakuReportBlockSign block : blocks) {
			this.buildBlockSign(structReport, block);
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
			KarakuReportBlockField block) throws ReportException {

		FastReportBuilder structBlockReport = new FastReportBuilder();
		structBlockReport.setDefaultStyles(
				this.getStyleTitleTransparentUnderline(), this.getStyleTitle(),
				this.getStyleColumnHeaderBlank(), null);

		structBlockReport.setUseFullPageWidth(true);
		this.setWhenNotData(structBlockReport);

		try {

			structBlockReport.setTitle(block.getTitle());
			structBlockReport.setTopMargin(0);
			structBlockReport.setBottomMargin(0);
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
			KarakuReportBlockGrid block) throws ReportException {

		FastReportBuilder structBlockReport = this.newInstance();
		structBlockReport.setDefaultStyles(this.getStyleTitle(), null,
				this.getStyleColumnHeader(), getStyleColumnDetail());

		structBlockReport.setTitle(block.getTitle());
		structBlockReport.setTopMargin(0);
		structBlockReport.setBottomMargin(0);

		this.addColumn(structBlockReport, block.getColumns());

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
			KarakuReportBlockSign block) throws ReportException {

		FastReportBuilder structBlockReport = new FastReportBuilder();
		structBlockReport.setDefaultStyles(this.getStyleTitle(),
				this.getStyleTitle(),
				this.getStyleColumnHeaderTransparentUnderlineTop(), null);

		structBlockReport.setUseFullPageWidth(true);
		structBlockReport.setTopMargin(0);
		this.setWhenNotDataEmpty(structBlockReport);

		try {

			for (Sign sign : block.getSigns()) {

				structBlockReport.addColumn(sign.getValue(), "label",
						String.class, sign.getWidth());
				if (!sign.equals(block.getSigns().get(
						block.getSigns().size() - 1))) {
					structBlockReport.addColumn(this.buildColumnSeparator());
				}

			}

			structReportHead.addConcatenatedReport(this.buildReportSeparator());

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

		return ColumnBuilder.getNew().setStyle(this.getStyleColumnHeader())
				.setColumnProperty("label", String.class)
				.setWidth(WIDTH_SEPARATOR).build();
	}

	private Subreport buildReportSeparator() {

		FastReportBuilder structReport = new FastReportBuilder();
		structReport.setDefaultStyles(this.getStyleTitle(),
				this.getStyleTitle(),
				this.getStyleColumnHeaderTransparentUnderlineTop(), null);
		structReport.addColumn(this.buildColumnSeparator());
		structReport.setUseFullPageWidth(true);
		this.setWhenNotDataEmpty(structReport);

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

	public Style getStyleColumnDetail() {

		Style styleColumns = new Style();
		styleColumns.setBorder(Border.THIN());
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
	 * @return StyleColumnHeader Estilo de las columnas de la grilla.
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
	public FastReportBuilder setWhenNotData(FastReportBuilder structReport) {

		structReport.setWhenNoData(I18nHelper.getMessage(NOT_DATA), null);
		return structReport;
	}

	private FastReportBuilder setWhenNotDataEmpty(FastReportBuilder structReport) {

		structReport.setWhenNoData(" ", null);
		return structReport;
	}

	/**
	 * Configura el template del reporte de acuerdo a la alineación y sección de
	 * criterios.
	 * 
	 * @param report
	 *            Reporte que se desea configurar.
	 * @param align
	 *            Alineación del reporte: <li>Horizontal <li>Vertical
	 * @param criteria
	 *            <li><b><code>true</code></b> Sí se desea que el reporte tenga
	 *            la sección de criterios. <li><b><code>false</code></b> Caso
	 *            contrario.
	 * @return
	 */
	public FastReportBuilder setAlignReport(FastReportBuilder report,
			Align align, boolean criteria) {

		if (align.equals(Align.HORIZONTAL)) {
			this.setTemplateLandScape(report, criteria);
		} else {
			this.setTemplatePortrait(report, criteria);

		}
		return report;

	}

	/**
	 * Adiciona al reporte el template base con orientación horizontal.
	 * 
	 * @param report
	 * @return
	 */
	public FastReportBuilder setTemplatePortrait(FastReportBuilder report,
			boolean criteria) {

		if (criteria) {
			report.setTemplateFile(FILE_PORTRAIT_CRITERIA_LOCATION, true,
					false, true, true);
		} else {
			report.setTemplateFile(FILE_PORTRAIT_LOCATION, true, false, true,
					true);
		}

		return report;
	}

	/**
	 * Adiciona al reporte el template base con orientación vertical.
	 * 
	 * @param report
	 * @return
	 */
	public FastReportBuilder setTemplateLandScape(FastReportBuilder report,
			boolean criteria) {

		if (criteria) {
			report.setTemplateFile(FILE_LANDSCAPE_CRITERIA_LOCATION, true,
					false, true, true);
		} else {
			report.setTemplateFile(FILE_LANDSCAPE_LOCATION, true, false, true,
					true);
		}
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

	/**
	 * Método que genera una expresión para hacer la conversión del tipo de dato
	 * {@link Quantity} a {@link Bigdecimal}
	 * 
	 * @param value
	 *            Field del dataSource que se desea convertir.
	 * @return
	 */
	public CustomExpression getQuantityCustomExpression(final String value) {

		return new CustomExpression() {

			private static final long serialVersionUID = 2200118849765160960L;

			@SuppressWarnings("rawtypes")
			@Override
			public Object evaluate(Map fields, Map variables, Map parameters) {

				return ((Quantity) fields.get(value)).bigDecimalValue();
			}

			@Override
			public String getClassName() {

				return BigDecimal.class.getName();
			}
		};
	}

	public FastReportBuilder getInstanceByAlign(Align align, boolean criteria) {

		if (align.equals(align.HORIZONTAL)) {

			return newInstanceLandScape(criteria);
		} else {
			return newInstancePortrait(criteria);
		}
	}

}
