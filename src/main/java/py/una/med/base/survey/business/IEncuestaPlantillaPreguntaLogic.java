/*
 * @IEncuestaPlantillaPreguntaLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.business;

import java.util.List;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.domain.EncuestaPlantillaPregunta;
import py.una.med.social.business.ISocialBaseLogic;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
public interface IEncuestaPlantillaPreguntaLogic extends
		ISocialBaseLogic<EncuestaPlantillaPregunta> {

	List<EncuestaPlantillaPregunta> getQuestionsByBlock(
			EncuestaPlantillaBloque block);
}
