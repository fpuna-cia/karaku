/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
