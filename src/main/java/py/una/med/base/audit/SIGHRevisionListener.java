package py.una.med.base.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import py.una.med.base.model.SIGHRevisionEntity;

/**
 * Clase encargada de modificar los registros de auditoria para agregar el
 * usuario y el ip
 * 
 * @author Romina Fernandez
 * @since 1.0
 * @version 1.0
 */
public class SIGHRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object arg0) {

		SIGHRevisionEntity revisionEntity = (SIGHRevisionEntity) arg0;
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		revisionEntity.setUsername(userName);

		String ip = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest().getRemoteAddr();
		revisionEntity.setIp(ip);
	}

}
