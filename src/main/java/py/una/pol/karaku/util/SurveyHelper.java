/*
 * @SurveyHelper.java 1.0 02/07/2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.reports.FieldBlockBuilder;
import py.una.pol.karaku.reports.GridBlockBuilder;
import py.una.pol.karaku.reports.KarakuReportBlock;
import py.una.pol.karaku.reports.KarakuReportBlockField;
import py.una.pol.karaku.reports.KarakuReportBlockGrid;
import py.una.pol.karaku.survey.business.IEncuestaPlantillaPreguntaLogic;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaDetalle;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.survey.repo.IEncuestaDetalleDAO;
import py.una.pol.karaku.survey.repo.IEncuestaPlantillaBloqueDAO;

/**
 * Clase que proporciona las funcionalidades comunes o más utilizadas en una
 * encuesta.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 02/07/2014
 * 
 */
@Service
@Transactional
public class SurveyHelper {

	private static final String EMPTY_STRING = "";
	private static final String TAB = "    ";
	private static final String MARKED = "(X)";
	private static final String NO_MARKED = "(  )";

	@Autowired
	private IEncuestaPlantillaBloqueDAO bloqueDao;

	@Autowired
	private IEncuestaPlantillaPreguntaLogic eppLogic;

	@Autowired
	private IEncuestaDetalleDAO edDao;

	/**
	 * Construye los bloques de una determinada encuesta.
	 * 
	 * @param encuesta
	 *            Encuestas cuyos bloques se desean construir
	 * @return Lista de bloques de la encuesta con sus respuestas
	 *         correspondientes.
	 */
	public List<KarakuReportBlock> buildReport(Encuesta encuesta) {

		ReportBuilder builder = new ReportBuilder();

		EncuestaPlantilla plantilla = encuesta.getPlantilla();

		/* Obtenemos los bloques de la plantilla */
		List<EncuestaPlantillaBloque> bloques = this.bloqueDao
				.getBlocksByTemplate(plantilla);

		for (EncuestaPlantillaBloque bloque : bloques) {

			/* Obtenmos las perguntas del bloque */
			List<EncuestaPlantillaPregunta> preguntas = eppLogic
					.getQuestionsByBlock(bloque);

			if (bloque.isGrilla()) {
				builder.addBlock(buildDataDataTable(encuesta, preguntas, bloque));

			} else {

				builder.addBlock(buildDataField(encuesta, preguntas, bloque));

			}

		}

		return builder.getBlocks();
	}

	/**
	 * Construye un bloque del tipo SIMPLE de la encuesta.
	 * 
	 * @param encuesta
	 *            Encuesta en cuestión
	 * @param preguntas
	 *            Preguntas del bloque
	 * @param bloque
	 *            Bloque de la encuesta
	 * @return Bloque construido correctamente con sus respuestas
	 *         correspondientes.
	 */
	protected KarakuReportBlockField buildDataField(Encuesta encuesta,
			List<EncuestaPlantillaPregunta> preguntas,
			EncuestaPlantillaBloque bloque) {

		FieldBlockBuilder block = new FieldBlockBuilder(bloque.getTitulo(),
				false).setWidthLabel(17).setWidthValue(50);

		for (EncuestaPlantillaPregunta epp : preguntas) {

			// respuesta a la pregunta en cuestion
			EncuestaDetalle ed = edDao.getEncuestaDetalleByPreguntaEncuesta(
					encuesta, epp);
			String pregunta = epp.getDescripcion();

			/* el caso de radio o check */
			if (!epp.isCheck() && !epp.isRadio()) {
				String respuesta = EMPTY_STRING;

				if (ed != null) {
					respuesta = ed.getRespuesta();
				}
				block.addField(pregunta, false, respuesta);

			} else {
				// Construimos en base a las opciones disponibles y las
				// seleccionadas(si aplica)
				String respuesta = toStringOpciones(ed,
						epp.getOpcionRespuesta());

				block.addField(pregunta, false, respuesta);

			}

		}
		return block.build();
	}

	/**
	 * @param ed
	 * @param opciones
	 * @param respuesta
	 * @return
	 */
	protected String toStringOpcionesSeleccionadas(
			List<OpcionRespuesta> opciones,
			List<OpcionRespuesta> seleccionadas, String respuesta) {

		String build = EMPTY_STRING;

		for (OpcionRespuesta or : opciones) {
			String opcion = or.getDescripcion();

			if (seleccionadas.contains(or)) {
				build = StringUtils.join("", build, MARKED, opcion);

				if (or.isCompletar()) {
					build = StringUtils.join("", build, respuesta);
				}
			} else {
				build = StringUtils.join("", build, NO_MARKED, opcion);
			}
			build = StringUtils.join("", build, TAB);
		}
		return build;
	}

	/**
	 * Contruye las opciones disponibles para una determinada pregunta
	 * 
	 * @param opciones
	 * @param respuesta
	 * @return
	 */
	protected String toStringOpciones(EncuestaDetalle ed,
			List<OpcionRespuesta> opciones) {

		List<OpcionRespuesta> seleccionadas = edDao.getRespuestasSelected(ed);

		if (ed != null && ListHelper.hasElements(seleccionadas)) {

			return toStringOpcionesSeleccionadas(opciones, seleccionadas,
					ed.getRespuesta());
		}

		String build = EMPTY_STRING;
		for (OpcionRespuesta or : opciones) {
			build = StringUtils.join("", build, NO_MARKED, or.getDescripcion(),
					TAB);

		}
		return build;
	}

	/**
	 * Construye un bloque del tipo GRILLA de la encuesta.
	 * 
	 * @param encuesta
	 *            Encuesta en cuestión
	 * @param preguntas
	 *            Preguntas del bloque
	 * @param bloque
	 *            Bloque de la encuesta
	 * @return Bloque construido correctamente con sus respuestas
	 *         correspondientes.
	 */
	protected KarakuReportBlockGrid buildDataDataTable(Encuesta encuesta,
			List<EncuestaPlantillaPregunta> preguntas,
			EncuestaPlantillaBloque bloque) {

		GridBlockBuilder block = new GridBlockBuilder(bloque.getTitulo());

		/* Agregamos una columna por cada pregunta del bloque */
		for (EncuestaPlantillaPregunta pregunta : preguntas) {
			block.addColumn(pregunta.getDescripcion(), false, pregunta.getId()
					.toString());
		}

		List<String[]> data = new ArrayList<String[]>();

		// Respuestas de la encuesta
		List<EncuestaDetalle> respuestas = edDao
				.getRespuestas(encuesta, bloque);

		long columns = eppLogic.getCantPreguntas(bloque.getId());
		int nroFila = 1;
		String[] row = new String[(int) columns];
		for (EncuestaDetalle detalle : respuestas) {
			// restamos 1 debido a que el orden de la pregunta empieza en 1 y el
			// vector del dataTable en 0

			if (nroFila != detalle.getNumeroFila()) {
				data.add(row);
				nroFila = detalle.getNumeroFila();
				row = new String[(int) columns];
			}
			row[detalle.getNumeroPregunta() - 1] = detalle.getRespuesta();

		}
		data.add(row);

		return block.build(data);
	}

}
