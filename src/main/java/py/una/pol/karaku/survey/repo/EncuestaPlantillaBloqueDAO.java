/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.survey.repo;

import java.util.List;
import org.springframework.stereotype.Repository;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.OrderParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.repo.KarakuBaseDao;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.util.ListHelper;

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
		KarakuBaseDao<EncuestaPlantillaBloque, Long> implements
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
