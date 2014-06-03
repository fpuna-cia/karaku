/*
 * @IEncuestaPlantillaBloqueLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import java.util.List;
import py.una.pol.karaku.business.ISIGHBaseLogic;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
public interface IEncuestaPlantillaBloqueLogic extends
		ISIGHBaseLogic<EncuestaPlantillaBloque, Long> {

	/**
	 * @param plantilla
	 * @return
	 */
	List<EncuestaPlantillaBloque> getBlocksByTemplate(EncuestaPlantilla template);

}
