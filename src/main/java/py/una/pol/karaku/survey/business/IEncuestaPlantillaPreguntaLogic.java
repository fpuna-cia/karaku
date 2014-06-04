/*
 * @IEncuestaPlantillaPreguntaLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import java.util.List;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
public interface IEncuestaPlantillaPreguntaLogic extends
		IKarakuBaseLogic<EncuestaPlantillaPregunta, Long> {

	List<EncuestaPlantillaPregunta> getQuestionsByBlock(
			EncuestaPlantillaBloque block);

	/**
	 * Retorna la cantidad de preguntas que posee un determinado bloque.
	 * 
	 * @param id
	 *            Identificador del bloque
	 * @return Cantidad de preguntas
	 */
	Long getCantPreguntas(long id);
}
