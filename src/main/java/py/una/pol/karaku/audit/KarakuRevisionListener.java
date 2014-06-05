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
package py.una.pol.karaku.audit;

import static py.una.pol.karaku.util.Checker.notNull;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.model.KarakuRevisionEntity;
import py.una.pol.karaku.replication.client.ReplicationContextHolder;

/**
 * Clase encargada de modificar los registros de auditoria para agregar el
 * usuario y el ip
 * 
 * @author Romina Fernandez
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 */
public class KarakuRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object arg0) {

		KarakuRevisionEntity revisionEntity = (KarakuRevisionEntity) arg0;

		if (!processReplicationChange(revisionEntity)
				&& !processJSFChange(revisionEntity)) {
			throw new KarakuRuntimeException(
					"Not context found!, please add a processTHISCONTEXTChange");
		}
	}

	private boolean processReplicationChange(KarakuRevisionEntity revisionEntity) {

		if (ReplicationContextHolder.getContext() == null) {
			revisionEntity.setUsername(null);
			return false;
		}
		revisionEntity.setUsername(ReplicationContextHolder.getContext()
				.getReplicationUser());
		return true;

	}

	private boolean processJSFChange(KarakuRevisionEntity sre) {

		if (SecurityContextHolder.getContext() == null
				|| SecurityContextHolder.getContext().getAuthentication() == null) {
			sre.setUsername(null);
			sre.setIp(null);
			return false;
		}

		notNull(SecurityContextHolder.getContext());
		notNull(SecurityContextHolder.getContext().getAuthentication());

		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		sre.setUsername(userName);

		String ip = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest().getRemoteAddr();
		sre.setIp(ip);
		return true;

	}

}
