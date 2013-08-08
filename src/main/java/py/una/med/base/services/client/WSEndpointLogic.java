/*
 * @WSEndpointDAO.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;

/**
 * 
 * @author Arturo Volpe
 * @since 2.1
 * @version 1.0 Jun 11, 2013
 * 
 */
@Service
@Transactional
public class WSEndpointLogic extends SIGHBaseLogic<WSEndpoint, Long> {

	@Autowired
	private WSEndpointDAO dao;

	@Override
	public ISIGHBaseDao<WSEndpoint, Long> getDao() {

		return dao;
	}
}
