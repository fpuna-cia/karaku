/*
 * @WSEndpointDAO.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.services.client;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.repo.KarakuBaseDao;

/**
 *
 * @author Arturo Volpe
 * @since 2.1
 * @version 1.0 Jun 11, 2013
 *
 */
@Repository
@Transactional
public class WSEndpointDAO extends KarakuBaseDao<WSEndpoint, Long> {

}
