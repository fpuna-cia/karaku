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
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.Clause;
import py.una.pol.karaku.dao.where.NumberLike;

/**
 * Helper que se encarga de crear los alias necesarios para que se pueda navegar
 * en una relación entre joins
 * <p>
 * Este es un helper particular pues {@link NumberLike} es a la vez
 * {@link Clause} y {@link Criterion}
 * </p>
 * 
 * @see BaseClauseHelper
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1 08/02/2013
 * 
 */
@Component
public final class NumberLikeExpressionHelper extends
		BaseClauseHelper<NumberLike> {

	/**
	 * Esta implementación retorna una copia del {@link NumberLike} pasado con
	 * el alias configurado correctamente.
	 * 
	 * <p>
	 * Se retorna una copia del {@link NumberLike} pues si se modifica el
	 * {@link NumberLike} original para que contenga el alias correcto, las
	 * siguientes invocaciones a este método retornarían una version corrupta de
	 * la claúsula. Este método debe ser idempotente.
	 * </p>
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull NumberLike clause, @Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria,
				clause.getPropiedad(), aliases);
		return new NumberLike(aliasWithProperty, clause.getValor(),
				clause.getMatchMode());

	}

}
