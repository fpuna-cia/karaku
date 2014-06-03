/*
 * @PrincipalMethodHandler.java 1.0 Feb 14, 2013
 *
 * Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javassist.util.proxy.MethodHandler;
import org.hibernate.Session;
import py.una.pol.karaku.dao.annotations.MainInstance;

/**
 * MethodHandler (proxy) que se encarga de interceptar las llamadas a los
 * métodos lazy y realiza la consulta.
 *
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 14, 2013
 *
 */
public class MainInstanceMethodHandler<T> implements MethodHandler {

	private boolean consulted;

	private final Session session;

	private T embedded;

	private final String hql;

	private final MainInstance principal;

	private final Class<T> clazz;

	private final Object entity;

	public MainInstanceMethodHandler(Session session, MainInstance principal,
			Object entity, Class<T> clazz) {

		this.clazz = clazz;
		consulted = false;
		this.session = session;

		this.entity = entity;
		this.principal = principal;
		hql = MainInstanceHelper.generateHQL(entity, principal);
	}

	@SuppressWarnings("unchecked")
	protected void initialize() {

		consulted = true;
		embedded = (T) MainInstanceHelper.fetchAttribute(session, hql,
				principal, entity);
	}

	@Override
	public Object invoke(Object proxy, Method method, Method proceed,
			Object[] args) throws IllegalAccessException,
			InvocationTargetException {

		if (!consulted) {
			initialize();
		}

		return method.invoke(embedded, args);
	}

	/**
	 * @return clazz
	 */
	public Class<T> getClazz() {

		return clazz;
	}

}
