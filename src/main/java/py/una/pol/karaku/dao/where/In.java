/*-
 * Copyright (c)
 *
 *      2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 *      2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 *      2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
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
package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Clase que implementa la cláusula IN.
 * 
 * @author Victor Ughelli
 * @since 1.0
 * @version 1.0 01/06/2015
 * 
 */
public class In implements Clause {

    private final Object[] list;
    @Nonnull
    private final String path;

    public In(Object[] list, String path) {

        super();
        this.list = list.clone();
        this.path = path;
    }

    @Override
    public Criterion getCriterion() {

        return Restrictions.in(this.path, this.list);
    }

    public Object[] getList() {

        return this.list.clone();
    }

    @Nonnull
    public String getPath() {

        return path;
    }

}
