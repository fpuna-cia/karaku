/*
 * @EncuestaPlantillaDAO 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.repo;

import org.springframework.stereotype.Repository;
import py.una.med.base.repo.SIGHBaseDao;
import py.una.med.base.survey.domain.EncuestaPlantilla;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Repository
public class EncuestaPlantillaDAO extends SIGHBaseDao<EncuestaPlantilla, Long>
		implements IEncuestaPlantillaDAO {

}
