/*
 * @IEncuestaPlantillaPreguntaDAO 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.repo;

import java.util.List;
import py.una.pol.karaku.repo.IKarakuBaseDao;
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
public interface IEncuestaPlantillaPreguntaDAO extends
		IKarakuBaseDao<EncuestaPlantillaPregunta, Long>  {

	/**
	 * Obtiene todas las preguntas de un bloque en particular recibido como
	 * parametro
	 * 
	 * @param block
	 * @return lista de preguntas del bloque
	 */
	List<EncuestaPlantillaPregunta> getQuestionsByBlock(
			EncuestaPlantillaBloque block);

}
