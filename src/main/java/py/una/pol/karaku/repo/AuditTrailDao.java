package py.una.pol.karaku.repo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.domain.AuditTrail;

@Repository
@Transactional
public class AuditTrailDao extends KarakuBaseDao<AuditTrail, Integer> implements
		IAuditTrailDao {

	public AuditTrailDao() {

	}

}
