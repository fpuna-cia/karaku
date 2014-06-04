/*
 * @EncuestaDetalleDAO 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.repo;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.OrderParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.dao.select.Select;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.repo.KarakuBaseDao;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaDetalle;
import py.una.pol.karaku.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Repository
public class EncuestaDetalleDAO extends KarakuBaseDao<EncuestaDetalle, Long>
		implements IEncuestaDetalleDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private IEncuestaDetalleOpcionRespuestaDAO encuestaDetalleOpcionRespuestaDAO;

	@Override
	public List<EncuestaDetalle> getRespuestas(Encuesta encuesta,
			EncuestaPlantillaBloque block) {

		String orden = "pregunta.orden";
		String fila = "numeroFila";
		Where<EncuestaDetalle> where = Where.get();
		where.addClause(Clauses.eq("encuesta", encuesta));
		where.addClause(Clauses.eq("pregunta.bloque", block));

		Select select = Select.build(orden, fila, "respuesta");
		SearchParam params = new SearchParam();
		params.addOrder(new OrderParam(true, fila));
		params.addOrder(new OrderParam(true, orden));
		return get(select, where, params);
	}

	@Override
	public EncuestaDetalle getEncuestaDetalleByPreguntaEncuesta(
			Encuesta encuesta, EncuestaPlantillaPregunta pregunta) {

		EncuestaDetalle example = new EncuestaDetalle();
		example.setEncuesta(encuesta);
		example.setPregunta(pregunta);

		return getByExample(example);
	}

	@Override
	public List<OpcionRespuesta> getRespuestasSelected(
			EncuestaDetalle encuestaDetalle) {

		EncuestaDetalleOpcionRespuesta example = new EncuestaDetalleOpcionRespuesta();
		example.setEncuestaDetalle(encuestaDetalle);
		Where<EncuestaDetalleOpcionRespuesta> where = new Where<EncuestaDetalleOpcionRespuesta>();
		where.setExample(example);
		List<OpcionRespuesta> list = new ArrayList<OpcionRespuesta>();
		for (EncuestaDetalleOpcionRespuesta detalle : encuestaDetalleOpcionRespuestaDAO
				.getAll(where, null)) {
			list.add(detalle.getOpcionRespuesta());
		}
		return list;
	}

	@Override
	public List<EncuestaDetalleOpcionRespuesta> getDetailsRespuestasSelected(
			EncuestaDetalle encuestaDetalle) {

		EncuestaDetalleOpcionRespuesta example = new EncuestaDetalleOpcionRespuesta();
		example.setEncuestaDetalle(encuestaDetalle);
		Where<EncuestaDetalleOpcionRespuesta> where = new Where<EncuestaDetalleOpcionRespuesta>();
		where.setExample(example);

		List<EncuestaDetalleOpcionRespuesta> result = new ArrayList<EncuestaDetalleOpcionRespuesta>();

		for (EncuestaDetalleOpcionRespuesta detalle : encuestaDetalleOpcionRespuestaDAO
				.getAll(where, null)) {
			log.debug("Recuperando las opciones de respuesta.."
					+ detalle.getOpcionRespuesta().toString());
			detalle.setOpcionRespuesta(detalle.getOpcionRespuesta());
			result.add(detalle);
		}
		return result;
	}
}
