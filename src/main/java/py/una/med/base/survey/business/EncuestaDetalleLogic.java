/*
 * @EncuestaDetalleLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.Encuesta;
import py.una.med.base.survey.domain.EncuestaDetalle;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.domain.OpcionRespuesta;
import py.una.med.social.business.SocialBaseLogic;
import py.una.med.social.repo.IEncuestaDetalleDAO;

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
public class EncuestaDetalleLogic extends SocialBaseLogic<EncuestaDetalle>
		implements IEncuestaDetalleLogic {

	@Autowired
	private IEncuestaDetalleDAO dao;

	@Override
	public ISIGHBaseDao<EncuestaDetalle, Long> getDao() {

		return dao;
	}

	/**
	 * Obtiene las respuestas registradas de un determinado bloque para una
	 * encuesta en particular
	 */
	@Override
	public List<?> getRespuestas(Encuesta encuesta,
			EncuestaPlantillaBloque block) {

		return dao.getRespuestas(encuesta, block);
	}

	@Override
	public List<OpcionRespuesta> getRespuestasSelected(long encuestaDetalle) {

		return dao.getRespuestasSelected(encuestaDetalle);
	}

}
