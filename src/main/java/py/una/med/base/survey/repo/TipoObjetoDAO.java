/*
 * @TipoObjetoDAO 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.survey.repo;

import org.springframework.stereotype.Repository;
import py.una.med.base.repo.SIGHBaseDao;
import py.una.med.base.survey.domain.TipoObjeto;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 27/05/2013
 * 
 */
@Repository
public class TipoObjetoDAO extends SIGHBaseDao<TipoObjeto, Long> implements
		ITipoObjetoDAO {

}
