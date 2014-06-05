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
package py.una.pol.karaku.replication.client;

import static py.una.pol.karaku.util.Checker.notNull;
import java.lang.reflect.Field;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.util.KarakuReflectionUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Component
public class ReplicationRequestFactory {

	private static final String[] FIELDS = { "id", "lastId" };

	/**
	 * Crea un objeto del tipo request para una petición de sincronización.
	 * 
	 * @param ri
	 *            información de la replicación
	 * @return objeto request creado
	 * @throws KarakuRuntimeException
	 *             si no puede crear.
	 */
	@SuppressWarnings("unchecked")
	public <T> T createMessage(ReplicationInfo ri) {

		return (T) getRequest(ri.getRequestClazz(), ri.getLastId());
	}

	/**
	 * 
	 * @param ri
	 * @return
	 */
	private Object getRequest(Class<?> clazz, String id) {

		try {
			Object o = clazz.newInstance();
			Field idF = KarakuReflectionUtils.findField(clazz, FIELDS);
			notNull(idF, "Cant get the Sync ID in the request, "
					+ "please create a field with name id or use"
					+ "@ReplicationId, see '%s'", clazz);
			idF.setAccessible(true);
			ReflectionUtils.setField(idF, o, id);
			return o;
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					"Cant create request for ReplicateionInfo with id: " + id,
					e);
		}

	}
}
