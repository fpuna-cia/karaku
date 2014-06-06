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
package py.una.pol.karaku.test.test.replication.layers;

import javax.annotation.Nonnull;
import org.hibernate.Session;
import py.una.pol.karaku.repo.KarakuBaseDao;

/**
 * Dao para los test de replicacion
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 * 
 */
public class ReplicatedEntityDao extends KarakuBaseDao<ReplicatedEntity, Long> {

	Session session;

	public void setSession(Session session) {

		this.session = session;
	}

	@Override
	public @Nonnull
	Session getSession() {

		Session toRet = session;
		if (toRet == null) {
			toRet = super.getSession();
		}
		return toRet;
	}

}
