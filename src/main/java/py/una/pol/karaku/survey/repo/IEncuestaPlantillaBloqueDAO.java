/*
 * @IEncuestaPlantillaBloqueDAO 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.repo;

import java.util.List;
import py.una.pol.karaku.repo.IKarakuBaseDao;
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
public interface IEncuestaPlantillaBloqueDAO extends
		IKarakuBaseDao<EncuestaPlantillaBloque, Long> {

	/**
	 * Obtiene los bloques que corresponden a una determinada plantilla de
	 * encuesta
	 * 
	 * @param encuesta
	 * @return bloques de la plantilla
	 */
	List<EncuestaPlantillaBloque> getBlocksByTemplate(EncuestaPlantilla template);

}
