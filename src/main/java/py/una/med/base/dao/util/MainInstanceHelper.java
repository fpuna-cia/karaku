/*
 * @MainInstanceHelper.java 1.0 Feb 13, 2013
 * 
 * Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javax.persistence.FetchType;
import javax.persistence.NonUniqueResultException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jfree.util.Log;
import py.una.med.base.dao.annotations.MainInstance;

/**
 * 
 * Clase que se encarga de crear los proxies para manejar las anotaciones
 * MainInstance, además en caso de que el {@link FetchType} sea
 * {@link FetchType#EAGER} modifica la consulta para que los resultados sena
 * Traídos y luego los parsea para agregar al objeto.s
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 13, 2013
 * 
 */
public class MainInstanceHelper {

	@SuppressWarnings("unchecked")
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
		if ((list == null) || (list.size() == 0)) {
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
		System.out.println(sb.toString());
		return sb.toString();
	}

	int c = 0;

	/**
	 * Método principal que maneja la creación de instancias principales, se
	 * encarga de ejecutar todas las consultas del dao, además crea Proxies
	 * Automáticos cuando es detectada una anotación {@link MainInstance} que
	 * tenga como {@link FetchType}, {@link FetchType#LAZY} para que se carge
	 * dinámicamente mas tarde, el proxy funciona mientras la session este
	 * Abierta.
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> configureAndReturnList(final Session session,
			final Criteria criteria, final Class<T> clase,
			final Map<String, String> alias) throws Exception {

		List<Field> fields = MainInstanceFieldHelper
				.getMainInstanceFields(clase);
		List<T> aRet = new ArrayList<T>();

		if (!((fields == null) || (fields.size() == 0))) {
			configureCriteria(criteria, fields, alias);
			List list = criteria.list();
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Map m = (Map) i.next();
				T entity = (T) m.get(Criteria.ROOT_ALIAS);
				aRet.add(entity);
				for (Field f : fields) {
					MainInstance mi = f.getAnnotation(MainInstance.class);

					Object o = m.get(generateAlias(mi));
					f.setAccessible(true);
					f.set(entity, o);
				}
			}
		} else {
			aRet = criteria.list();
		}

		try {
			helpList(aRet, session);
		} catch (Exception e) {
			Log.error("Imposible crear proxies para principales", e);
		}
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
	 * @param session
	 *            Session hibernate durante la cual tendrá sentido el proxy
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	public void help(final Object entity, final Session session)
			throws Exception {

		if (entity == null) {
			return;
		}

		for (Field f : MainInstanceFieldHelper.getMainInstanceFields(entity
				.getClass())) {
			MainInstance principal = f.getAnnotation(MainInstance.class);
			f.setAccessible(true);
			f.set(entity, newInstance(entity, session, principal, f.getType()));
		}
	}

	/**
	 * Método que agrega los proxy's para el {@link FetchType#LAZY}, para
	 * agregar otro tipo de fetch utilice
	 * {@link MainInstanceHelper#configureAndReturnList(Session, Criteria, Class)}
	 * 
	 * @param entities
	 * @param session
	 * @throws Exception
	 */
	public void helpList(final List<?> entities, final Session session)
			throws Exception {

		if ((entities == null) || (entities.size() == 0)) {
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
			throws Exception {

		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(fieldType);
		MethodHandler handler = new MainInstanceMethodHandler<T>(em, principal,
				entity, fieldType);

		@SuppressWarnings("unchecked")
		T value = (T) factory.create(new Class<?>[0], new Object[0], handler);
		return value;
	}
}
