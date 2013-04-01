package py.una.med.base.repo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.domain.AuditTrailDetail;

@Repository
@Transactional
public class AuditTrailDetailDao extends SIGHBaseDao<AuditTrailDetail, Long>
		implements IAuditTrailDetailDao {

	public AuditTrailDetailDao() {

	}

}
