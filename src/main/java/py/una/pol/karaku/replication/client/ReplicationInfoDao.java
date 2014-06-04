/*
 * @ReplicationInfoDao.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.client;

import org.springframework.stereotype.Repository;
import py.una.pol.karaku.repo.KarakuBaseDao;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
@Repository
public class ReplicationInfoDao extends KarakuBaseDao<ReplicationInfo, Long>
		implements IReplicationInfoDao {

}
