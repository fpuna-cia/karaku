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
package py.una.pol.karaku.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.ILike;

/**
 * {@link BaseClauseHelper} que se encarga de configurar las consultas del tipo
 * {@link ILike}, no tiene limite de anidaciones.
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Component
public class LikeExpressionHelper extends BaseClauseHelper<ILike> {

	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull ILike clause, @Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		return Restrictions.ilike(aliasWithProperty, clause.getValue()
				.toString(), clause.getMode().getMatchMode());
	}
}
