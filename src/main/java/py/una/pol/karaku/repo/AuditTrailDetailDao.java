package py.una.pol.karaku.repo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.domain.AuditTrailDetail;

@Repository
@Transactional
public class AuditTrailDetailDao extends KarakuBaseDao<AuditTrailDetail, Long>
		implements IAuditTrailDetailDao {

	public AuditTrailDetailDao() {

	}

}
