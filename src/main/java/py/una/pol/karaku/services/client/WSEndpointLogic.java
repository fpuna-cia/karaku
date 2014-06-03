/*
 * @WSEndpointDAO.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.SIGHBaseLogic;
import py.una.pol.karaku.repo.ISIGHBaseDao;

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
