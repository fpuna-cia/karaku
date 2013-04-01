package py.una.med.base.repo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.domain.AuditTrail;

@Repository
@Transactional
public class AuditTrailDao extends SIGHBaseDao<AuditTrail, Integer> implements
		IAuditTrailDao {

	public AuditTrailDao() {

	}

}
