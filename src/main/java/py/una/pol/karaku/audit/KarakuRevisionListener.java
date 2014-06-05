/*
 * 
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
