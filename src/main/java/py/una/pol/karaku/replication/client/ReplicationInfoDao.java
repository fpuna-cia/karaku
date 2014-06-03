/*
 * @ReplicationInfoDao.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import org.springframework.stereotype.Repository;
import py.una.med.base.repo.SIGHBaseDao;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
@Repository
public class ReplicationInfoDao extends SIGHBaseDao<ReplicationInfo, Long>
		implements IReplicationInfoDao {

}
