/*
 * @EncuestaDetalleLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.Encuesta;
import py.una.med.base.survey.domain.EncuestaDetalle;
import py.una.med.base.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.domain.EncuestaPlantillaPregunta;
import py.una.med.base.survey.domain.OpcionRespuesta;
import py.una.med.base.survey.repo.IEncuestaDetalleDAO;

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
public class EncuestaDetalleLogic extends SIGHBaseLogic<EncuestaDetalle, Long>
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
	public List<EncuestaDetalle> getRespuestas(Encuesta encuesta,
			EncuestaPlantillaBloque block) {

		return dao.getRespuestas(encuesta, block);
	}

	@Override
	public List<OpcionRespuesta> getRespuestasSelected(
			EncuestaDetalle encuestaDetalle) {

		return dao.getRespuestasSelected(encuestaDetalle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.survey.business.IEncuestaDetalleLogic#
	 * getDetailsRespuestasSelected(long)
	 */
	@Override
	public List<EncuestaDetalleOpcionRespuesta> getDetailsRespuestasSelected(
			EncuestaDetalle encuestaDetalle) {

		return dao.getDetailsRespuestasSelected(encuestaDetalle);
	}

	@Override
	public EncuestaDetalle getEncuestaDetalleByPreguntaEncuesta(
			Encuesta encuesta, EncuestaPlantillaPregunta pregunta) {

		return dao.getEncuestaDetalleByPreguntaEncuesta(encuesta, pregunta);
	}
}
