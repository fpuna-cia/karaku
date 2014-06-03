/*
 * @ReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.server;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.List;
import javax.annotation.Nonnull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.model.SIGHRevisionEntity;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.util.StringUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
@Service
@Transactional
public class EnversReplicationProvider implements ReplicationProvider {

	@Autowired
	private SessionFactory factory;

	@Log
	private Logger log;

	@Autowired
	private FirstChangeProviderHandler firstChangeProviderHandler;

	/**
	 * Retorna las entidades que fueron modificadas desde <code>lastId</code>.
	 * 
	 * <p>
	 * Maneja cuatro casos:
	 * <ol>
	 * <li>El ultimo cambio es {@link Bundle#ZERO_ID}, lo que significa que es
	 * la primera replicación, en este caso mandamos toda la tabla, y asignamos
	 * como último id a {@link Bundle#FIRST_CHANGE}</li>
	 * <li>El id del cambio es {@link Bundle#FIRST_CHANGE}, lo que significa que
	 * ya se ha enviado la tabla completa, pero ninguna vez se ha enviado un
	 * cambio auditado por envers. Se envía el delta del mismo</li>
	 * <li>El id del cambio es desconocido, o no corresponde con una revisión,
	 * para este caso se retorna la tabla completa, con el último id, la última
	 * revision conocida de envers.</li>
	 * <li>El id es conocido, se envía el delta de ese estado al estado actual
	 * de la tabla.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param clazz
	 *            entidad a buscar, debe ser {@link Shareable} y no nula.
	 * @param lastId
	 *            ultimo identificador conocido, si es nulo se considera
	 *            {@link Bundle#ZERO_ID}.
	 * @return cambios realizados con el id correspondiente.
	 * 
	 */
	@Override
	public <T extends Shareable> Bundle<T> getChanges(@Nonnull Class<T> clazz,
			String lastId) {

		notNull(clazz, "Can't get changes of null entity");

		String id = lastId;
		if (StringUtils.isInvalid(lastId)) {
			id = Bundle.ZERO_ID;
		}

		// SI la sincronización no existe se retorna todo.
		if (Bundle.ZERO_ID.equals(id)) {
			return getAll(clazz);
		}

		// Si el identificador no se conoce
		if (isUnknown(clazz, id)) {
			log.warn("Entity {} has invalid revision id, returning all",
					clazz.getSimpleName());
			return getAll(clazz);
		}

		return getDelta(clazz, id);
	}

	/**
	 * @param clazz
	 * @param lastId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends Shareable> Bundle<T> getDelta(Class<T> clazz,
			String lastId) {

		AuditReader ar = AuditReaderFactory.get(getSession());
		Number number = getLastId(clazz, lastId);
		List<Object[]> entities = ar.createQuery()
				.forRevisionsOfEntity(clazz, false, false)
				.add(AuditEntity.revisionNumber().gt(number)).getResultList();

		Bundle<T> bundle = new Bundle<T>(lastId);
		for (Object[] o : entities) {
			if (o == null) {
				continue;
			}
			bundle.add((T) notNull(o[0]), notNull(getId(o[1])));
		}
		return bundle;
	}

	/**
	 * @param clazz
	 * @param reader
	 * @param lastId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends Shareable> boolean isUnknown(Class<T> clazz,
			String lastId) {

		if (Bundle.FIRST_CHANGE.equals(lastId)) {
			return false;
		}

		AuditReader reader = AuditReaderFactory.get(getSession());
		List<T> entitiesAtRevision = reader.createQuery()
				.forRevisionsOfEntity(clazz, false, false)
				.add(AuditEntity.revisionNumber().eq(getLastId(clazz, lastId)))
				.getResultList();
		return (entitiesAtRevision == null) || entitiesAtRevision.isEmpty();
	}

	/**
	 * Este método retorna un Long por que {@link SIGHRevisionEntity} tiene un
	 * identificador que es long. Si se utiliza un int, como en
	 * {@link DefaultRevisionEntity} debe retornar un {@link Integer}.
	 * 
	 * @param clazz
	 * @param lastId
	 * @return
	 */
	protected <T extends Shareable> Number getLastId(Class<T> clazz,
			String lastId) {

		Long number;
		try {
			number = Long.valueOf(lastId);
		} catch (NumberFormatException pe) {
			log.warn("Entity {} has invalid revision id",
					clazz.getSimpleName(), pe);
			number = 0L;
		}
		return number;
	}

	@Nonnull
	private <T extends Shareable> Bundle<T> getAll(@Nonnull Class<T> clazz) {

		AuditReader reader = AuditReaderFactory.get(getSession());
		Number prior = (Number) reader.createQuery()
				.forRevisionsOfEntity(clazz, false, true)
				.addProjection(AuditEntity.revisionNumber().max())
				.getSingleResult();

		String lastId;
		// previous revision, la actual no será persistida.
		if (prior == null) {
			lastId = Bundle.FIRST_CHANGE;
		} else {
			lastId = String.valueOf(prior);
		}
		return firstChangeProviderHandler.getAll(clazz, notNull(lastId));

	}

	protected Session getSession() {

		return factory.getCurrentSession();
	}

	protected long getLongId(Object o) {

		if (o instanceof SIGHRevisionEntity) {
			return ((SIGHRevisionEntity) o).getId();
		}

		return ((DefaultRevisionEntity) o).getId();
	}

	protected String getId(Object o) {

		return String.valueOf(getLongId(o));
	}
}
