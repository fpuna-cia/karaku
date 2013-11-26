/*
 * @ReplicationInfoDao.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
@Service
@Transactional
public class ReplicationInfoLogic extends SIGHBaseLogic<ReplicationInfo, Long> {

	@Autowired
	private ReplicationInfoDao dao;

	@Override
	public ISIGHBaseDao<ReplicationInfo, Long> getDao() {

		return dao;
	}

}
