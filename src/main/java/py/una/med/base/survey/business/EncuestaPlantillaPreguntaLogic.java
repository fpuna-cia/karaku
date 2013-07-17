/*
 * @EncuestaPlantillaPreguntaLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.domain.EncuestaPlantillaPregunta;
import py.una.med.social.business.SocialBaseLogic;
import py.una.med.social.repo.IEncuestaPlantillaPreguntaDAO;

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
		SocialBaseLogic<EncuestaPlantillaPregunta> implements
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
}
