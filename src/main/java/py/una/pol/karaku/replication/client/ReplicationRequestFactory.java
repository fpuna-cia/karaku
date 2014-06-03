/*
 * @ReplicationRequestFactory.java 1.0 Nov 26, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.client;

import static py.una.pol.karaku.util.Checker.notNull;
import java.lang.reflect.Field;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.util.KarakuReflectionUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Component
public class ReplicationRequestFactory {

	private static final String[] FIELDS = { "id", "lastId" };

	/**
	 * Crea un objeto del tipo request para una petición de sincronización.
	 * 
	 * @param ri
	 *            información de la replicación
	 * @return objeto request creado
	 * @throws KarakuRuntimeException
	 *             si no puede crear.
	 */
	@SuppressWarnings("unchecked")
	public <T> T createMessage(ReplicationInfo ri) {

		return (T) getRequest(ri.getRequestClazz(), ri.getLastId());
	}

	/**
	 * 
	 * @param ri
	 * @return
	 */
	private Object getRequest(Class<?> clazz, String id) {

		try {
			Object o = clazz.newInstance();
			Field idF = KarakuReflectionUtils.findField(clazz, FIELDS);
			notNull(idF, "Cant get the Sync ID in the request, "
					+ "please create a field with name id or use"
					+ "@ReplicationId, see '%s'", clazz);
			idF.setAccessible(true);
			ReflectionUtils.setField(idF, o, id);
			return o;
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					"Cant create request for ReplicateionInfo with id: " + id,
					e);
		}

	}
}
