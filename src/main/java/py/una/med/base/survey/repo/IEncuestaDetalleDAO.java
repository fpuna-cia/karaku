/*
 * @IEncuestaDetalleDAO 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.repo;

import java.util.List;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.Encuesta;
import py.una.med.base.survey.domain.EncuestaDetalle;
import py.una.med.base.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.domain.EncuestaPlantillaPregunta;
import py.una.med.base.survey.domain.OpcionRespuesta;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
public interface IEncuestaDetalleDAO extends
		ISIGHBaseDao<EncuestaDetalle, Long> {

	List<?> getRespuestas(Encuesta encuesta, EncuestaPlantillaBloque block);

	/**
	 * @param encuestaDetalle
	 * @return
	 */
	List<OpcionRespuesta> getRespuestasSelected(long encuestaDetalle);

	/**
	 * @param encuestaDetalle
	 * @return
	 */
	List<EncuestaDetalleOpcionRespuesta> getDetailsRespuestasSelected(
			long encuestaDetalle);

	EncuestaDetalle getEncuestaDetalleByPreguntaEncuesta(Encuesta encuesta,
			EncuestaPlantillaPregunta pregunta);
}
