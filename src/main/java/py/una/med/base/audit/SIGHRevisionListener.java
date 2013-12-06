package py.una.med.base.audit;

import static py.una.med.base.util.Checker.notNull;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.model.SIGHRevisionEntity;
import py.una.med.base.replication.client.ReplicationContextHolder;

/**
 * Clase encargada de modificar los registros de auditoria para agregar el
 * usuario y el ip
 * 
 * @author Romina Fernandez
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 */
public class SIGHRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object arg0) {

		SIGHRevisionEntity revisionEntity = (SIGHRevisionEntity) arg0;

		if (!processReplicationChange(revisionEntity)
				&& !processJSFChange(revisionEntity)) {
			throw new KarakuRuntimeException(
					"Not context found!, please add a processTHISCONTEXTChange");
		}
	}

	private boolean processReplicationChange(SIGHRevisionEntity revisionEntity) {

		try {
			notNull(ReplicationContextHolder.getContext());
			revisionEntity.setUsername(ReplicationContextHolder.getContext()
					.getReplicationUser());
			return true;
		} catch (IllegalArgumentException iae) {
			revisionEntity.setUsername(null);
			return false;
		}

	}

	private boolean processJSFChange(SIGHRevisionEntity sre) {

		try {
			notNull(SecurityContextHolder.getContext());
			notNull(SecurityContextHolder.getContext().getAuthentication());

			String userName = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			sre.setUsername(userName);

			String ip = ((ServletRequestAttributes) RequestContextHolder
					.currentRequestAttributes()).getRequest().getRemoteAddr();
			sre.setIp(ip);
			return true;
		} catch (IllegalArgumentException iae) {
			sre.setUsername(null);
			sre.setIp(null);
			return false;
		}

	}

}
