/*
 * @EncuestaPlantillaPreguntaDAO 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.repo;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.OrderParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.repo.SIGHBaseDao;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Repository
public class EncuestaPlantillaPreguntaDAO extends
		SIGHBaseDao<EncuestaPlantillaPregunta, Long> implements
		IEncuestaPlantillaPreguntaDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public List<EncuestaPlantillaPregunta> getQuestionsByBlock(
			EncuestaPlantillaBloque block) {

		Where<EncuestaPlantillaPregunta> where = new Where<EncuestaPlantillaPregunta>();
		EncuestaPlantillaPregunta example = new EncuestaPlantillaPregunta();
		example.setBloque(block);
		where.setExample(example);
		SearchParam sp = new SearchParam();
		sp.setOrders(ListHelper.getAsList(new OrderParam(true, "orden")));

		List<EncuestaPlantillaPregunta> result = new ArrayList<EncuestaPlantillaPregunta>();
		for (EncuestaPlantillaPregunta pregunta : getAll(where, sp)) {

			log.debug("Recuperando las opciones.."
					+ pregunta.getOpcionRespuesta().toString());
			pregunta.setOpcionRespuesta(pregunta.getOpcionRespuesta());
			result.add(pregunta);
		}
		return result;

	}
}
