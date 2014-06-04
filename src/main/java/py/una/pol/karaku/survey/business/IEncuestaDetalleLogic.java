/*
 * @IEncuestaDetalleLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import java.util.List;
import py.una.pol.karaku.business.IKarakuBaseLogic;
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
public interface IEncuestaDetalleLogic extends
		IKarakuBaseLogic<EncuestaDetalle, Long> {

	/**
	 * Obtiene la lista de respuestas relacionadas a un bloque en particular, de
	 * una determinada encuesta
	 * 
	 * @param encuesta
	 * @return lista de respuestas
	 */
	List<EncuestaDetalle> getRespuestas(Encuesta encuesta,
			EncuestaPlantillaBloque block);

	/**
	 * @param encuestaDetalle
	 * @return
	 */
	List<OpcionRespuesta> getRespuestasSelected(EncuestaDetalle encuestaDetalle);

	/**
	 * @param encuestaDetalle
	 * @return
	 */
	List<EncuestaDetalleOpcionRespuesta> getDetailsRespuestasSelected(
			EncuestaDetalle encuestaDetalle);

	EncuestaDetalle getEncuestaDetalleByPreguntaEncuesta(Encuesta encuesta,
			EncuestaPlantillaPregunta pregunta);

}
