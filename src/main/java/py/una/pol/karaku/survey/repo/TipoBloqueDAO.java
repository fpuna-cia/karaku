/*
 * @TipoBloqueDAO 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.survey.repo;

import org.springframework.stereotype.Repository;
import py.una.pol.karaku.repo.KarakuBaseDao;
import py.una.pol.karaku.survey.domain.TipoBloque;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 27/05/2013
 * 
 */
@Repository
public class TipoBloqueDAO extends KarakuBaseDao<TipoBloque, Long> implements
		ITipoBloqueDAO {

}
