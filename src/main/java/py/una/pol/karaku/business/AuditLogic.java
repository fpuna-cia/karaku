/*
 * @AuditLogic 1.0 18/02/2013 Sistema integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.domain.AuditTrail;
import py.una.pol.karaku.domain.AuditTrailDetail;
import py.una.pol.karaku.repo.IAuditTrailDao;
import py.una.pol.karaku.repo.IAuditTrailDetailDao;
import py.una.pol.karaku.repo.ISIGHBaseDao;

/**
 * Clase que implementa la interfaz {@link IAuditLogic}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
@Service
@Transactional
public class AuditLogic extends SIGHBaseLogic<AuditTrail, Integer> implements
		IAuditLogic {

	@Autowired
	private IAuditTrailDao dao;

	@Autowired
	private IAuditTrailDetailDao detailDao;

	@Override
	public ISIGHBaseDao<AuditTrail, Integer> getDao() {

		return dao;
	}

	@Override
	public void saveAudit(AuditTrail auditTrail, List<AuditTrailDetail> details) {

		dao.add(auditTrail);
		for (AuditTrailDetail detail : details) {
			detailDao.add(detail);
		}
	}

}
