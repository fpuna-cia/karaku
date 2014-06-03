/*
 * @EncuestaPlantillaPreguntaLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.SIGHBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.repo.ISIGHBaseDao;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.repo.IEncuestaPlantillaPreguntaDAO;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Service
@Transactional
public class EncuestaPlantillaPreguntaLogic extends
		SIGHBaseLogic<EncuestaPlantillaPregunta, Long> implements
		IEncuestaPlantillaPreguntaLogic {

	@Autowired
	private IEncuestaPlantillaPreguntaDAO dao;

	@Override
	public ISIGHBaseDao<EncuestaPlantillaPregunta, Long> getDao() {

		return dao;
	}

	@Override
	public List<EncuestaPlantillaPregunta> getQuestionsByBlock(
			EncuestaPlantillaBloque block) {

		return dao.getQuestionsByBlock(block);
	}

	@Override
	public Long getCantPreguntas(long id) {

		Where<EncuestaPlantillaPregunta> w = Where.get();
		w.addClause(Clauses.eq("bloque.id", id));
		return getCount(w);
	}
}
