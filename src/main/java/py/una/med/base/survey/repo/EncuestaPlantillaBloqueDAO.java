/*
 * @EncuestaPlantillaBloqueDAO 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.repo;

import java.util.List;
import org.springframework.stereotype.Repository;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.OrderParam;
import py.una.med.base.dao.search.SearchParam;
import py.una.med.base.repo.SIGHBaseDao;
import py.una.med.base.survey.domain.EncuestaPlantilla;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.util.ListHelper;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Repository
public class EncuestaPlantillaBloqueDAO extends
		SIGHBaseDao<EncuestaPlantillaBloque, Long> implements
		IEncuestaPlantillaBloqueDAO {

	@Override
	public List<EncuestaPlantillaBloque> getBlocksByTemplate(
			EncuestaPlantilla template) {

		Where<EncuestaPlantillaBloque> where = new Where<EncuestaPlantillaBloque>();
		EncuestaPlantillaBloque example = new EncuestaPlantillaBloque();
		example.setEncuestaPlantilla(template);
		where.setExample(example);
		SearchParam sp = new SearchParam();
		sp.setOrders(ListHelper.getAsList(new OrderParam(true, "orden")));
		return getAll(where, sp);
	}
}
