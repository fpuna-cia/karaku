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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javax.annotation.Nonnull;
import javax.persistence.FetchType;
import javax.persistence.NonUniqueResultException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.annotations.MainInstance;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * Clase que se encarga de crear los proxies para manejar las anotaciones
 * MainInstance, además en caso de que el {@link FetchType} sea
 * {@link FetchType#EAGER} modifica la consulta para que los resultados sena
 * Traídos y luego los parsea para agregar al objeto.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 13, 2013
 * 
 */
@Component
@SuppressWarnings("unchecked")
public class MainInstanceHelper {

	@Log
	private Logger log;

	static Object fetchAttribute(final Session session, final String hql,
			final MainInstance principal, final Object parent) {

		if (!session.isOpen()) {
			throw new org.hibernate.LazyInitializationException(
					"Session is closed, failed to load Main instance "
							+ principal.path());
		}
		Query query = session.createQuery(hql);
		query.setMaxResults(2);
		query.setParameter("value", principal.value());
		query.setParameter("mainEntity", parent);
		List<Object> list = query.list();
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			throw new NonUniqueResultException("Attribute "
					+ principal.attribute() + " has more than 1 principal");
		}
		return list.get(0);
	}

	/**
	 * Genera un HQL para recuperar el atributo principal de una entidad, el hql
	 * es de la forma: <br>
	 * {@code "select p from ENTITY o join o.ATRIBUTO p where p.PATH like PRINCIPAL"}
	 * <br>
	 * <i>Notese que no se limita la cantidad de resultados, esto es para
	 * realizar otros controles, como que no se lanzen excepciones cuando hay
	 * mas de un atributo principal</i>
	 * 
	 * @param entity
	 *            Entidad raiz
	 * @param principal
	 *            Anotacion para obtener parametros
	 * @return hql preparado para traer el elemento
	 */
	static String generateHQL(final Object entity, final MainInstance principal) {

		String root = entity.getClass().getSimpleName();
		StringBuilder sb = new StringBuilder("select p ");
		sb.append("from ").append(root).append(" o ");
		sb.append("join o.").append(principal.attribute()).append(" p ");
		sb.append("where p.").append(principal.path()).append(" like :value");
		sb.append("and o like :mainEntity");
		return sb.toString();
	}

	/**
	 * Método principal que maneja la creación de instancias principales, se
	 * encarga de ejecutar todas las consultas del dao, además crea Proxies
	 * Automáticos cuando es detectada una anotación {@link MainInstance} que
	 * tenga como {@link FetchType}, {@link FetchType#LAZY} para que se carge
	 * dinámicamente mas tarde, el proxy funciona mientras la session este
	 * Abierta.
	 * <p>
	 * Cuando el {@link Where} tiene la propiedad {@link Where#isDistinct()},
	 * depende exclusivamente de la capacidad del método {@link T#hashCode()}
	 * para realizar su propósito de retornar elementos no duplicados.
	 * </p>
	 * 
	 * 
	 * @param session
	 *            Session, a la cual se ata el proxy, mientras este viva el
	 *            proxy funciona
	 * @param criteria
	 *            Criteria para filtrar los resultados de la consulta
	 * @param clase
	 *            Clase de lo esperado por la consulta
	 * @return Lista de elementos ya configurada.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	@Nonnull
	public <T> List<T> configureAndReturnList(@Nonnull final Session session,
			final Criteria criteria, final Class<T> clase,
			final Map<String, String> alias, final Where<T> where)
			throws IllegalAccessException {

		List<Field> fields = MainInstanceFieldHelper
				.getMainInstanceFields(clase);
		List<T> aRet;

		if (where != null && where.isDistinct()) {
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}

		if (ListHelper.hasElements(fields)) {
			aRet = applyMainInstance(criteria, alias, where, fields);
		} else {
			aRet = criteria.list();
		}

		if (aRet == null) {
			return new ArrayList<T>(0);
		}

		try {
			helpList(aRet, session);
		} catch (Exception e) {
			log.error("Imposible crear proxies para principales", e);
		}
		return aRet;
	}

	/**
	 * @param criteria
	 * @param alias
	 * @param where
	 * @param fields
	 * @return
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes" })
	private <T> List<T> applyMainInstance(final Criteria criteria,
			final Map<String, String> alias, final Where<T> where,
			List<Field> fields) throws IllegalAccessException {

		List<T> aRet;
		Collection<T> toBuild;
		if (where != null && where.isDistinct()) {
			toBuild = new LinkedHashSet<T>();
		} else {
			toBuild = new ArrayList<T>();
		}

		configureCriteria(criteria, fields, alias);
		List list = criteria.list();
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Map m = (Map) i.next();
			T entity = (T) m.get(Criteria.ROOT_ALIAS);
			toBuild.add(entity);
			for (Field f : fields) {
				MainInstance mi = f.getAnnotation(MainInstance.class);
				Object o = m.get(generateAlias(mi));
				f.setAccessible(true);
				f.set(entity, o);
				f.setAccessible(false);
			}
		}
		aRet = new ArrayList<T>(toBuild);
		return aRet;
	}

	/**
	 * 
	 * @param criteria
	 * @param fields
	 *            lista de fields que tienen la anotación {@link MainInstance},
	 *            solo atiende a los fields que son eager
	 * @return mapa de alias
	 */

	private Map<String, String> configureCriteria(final Criteria criteria,
			final List<Field> fields, final Map<String, String> alias) {

		for (Field f : fields) {

			MainInstance mi = f.getAnnotation(MainInstance.class);
			if (mi.fetch().equals(FetchType.LAZY)) {
				continue;
			}
			String newAlias = alias.get(mi.attribute());
			if (newAlias == null) {
				newAlias = generateAlias(mi);

				criteria.createCriteria(mi.attribute(), newAlias,
						JoinType.LEFT_OUTER_JOIN).add(
						Restrictions.or(
								Restrictions.like(mi.path(), mi.value()),
								Restrictions.isNull(mi.path())));
				alias.put(f.getName(), newAlias);
			} else {
				criteria.add(Restrictions.or(
						Restrictions.like(newAlias + "." + mi.path(),
								mi.value()),
						Restrictions.isNull(newAlias + "." + mi.path())));
			}

		}
		criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return alias;
	}

	private String generateAlias(final MainInstance mi) {

		return mi.attribute() + "_";
	}

	/**
	 * Dado <b>un</b> objeto, agrega el proxy que tiene sentido mientras dure la
	 * session
	 * 
	 * @param entity
	 *            entidad a aplicar los proxies
	 * @param session
	 *            Session hibernate durante la cual tendrá sentido el proxy
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public void help(final Object entity, final Session session)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		if (entity == null) {
			return;
		}

		for (Field f : MainInstanceFieldHelper.getMainInstanceFields(entity
				.getClass())) {
			MainInstance principal = f.getAnnotation(MainInstance.class);
			f.setAccessible(true);
			ReflectionUtils.setField(f, entity,
					newInstance(entity, session, principal, f.getType()));
		}
	}

	/**
	 * Método que agrega los proxy's para el {@link FetchType#LAZY}, para
	 * agregar otro tipo de fetch utilice
	 * {@link MainInstanceHelper#configureAndReturnList(Session, Criteria, Class)}
	 * 
	 * @param entities
	 * @param session
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws Exception
	 */
	public void helpList(@Nonnull final List<?> entities,
			@Nonnull final Session session) throws IllegalAccessException,
			NoSuchMethodException, InstantiationException,
			InvocationTargetException {

		if (!ListHelper.hasElements(entities)) {
			return;
		}
		Class<?> clazz = entities.get(0).getClass();
		for (Field f : MainInstanceFieldHelper.getMainInstanceFields(clazz)) {
			MainInstance principal = f.getAnnotation(MainInstance.class);
			f.setAccessible(true);
			for (Object object : entities) {
				if (principal.fetch() == FetchType.LAZY) {
					f.set(object,
							newInstance(object, session, principal, f.getType()));
				}
			}
		}
	}

	private <T> T newInstance(final Object entity, final Session em,
			final MainInstance principal, final Class<T> fieldType)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(fieldType);
		MethodHandler handler = new MainInstanceMethodHandler<T>(em, principal,
				entity, fieldType);

		return (T) factory.create(new Class<?>[0], new Object[0], handler);
	}
}
