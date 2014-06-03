/*
 * @TestUriInterceptor.java 1.0 Dec 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.util;

import org.hibernate.Session;
import py.una.med.base.dao.entity.interceptors.UriInterceptor;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 11, 2013
 * 
 */
public class TestUriInterceptor extends UriInterceptor {

	Session session;

	@Override
	protected Session getSession() {

		if (session != null) {
			return session;
		} else {
			return super.getSession();
		}
	}

	/**
	 * Session que se utilizara.
	 * 
	 * @param session
	 */
	public void setSession(Session session) {

		this.session = session;
	}
}
