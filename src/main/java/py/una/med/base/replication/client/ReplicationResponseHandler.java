/*
 * @ReplicationResponseHandler.java 1.0 Nov 26, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import static py.una.med.base.util.Checker.notNull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.util.KarakuReflectionUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Component
public class ReplicationResponseHandler {

	private String[] ID_FIELDS = { "id", "lastId" };
	private String[] CHANGE_FIELDS = { "entities", "data" };

	/**
	 * @param t1
	 * @return
	 */
	public Pair<String, Collection<?>> getChanges(Object t1) {

		return new MutablePair<String, Collection<?>>(getLastId(t1),
				getItems(t1));
	}

	/**
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Collection getItems(Object response) {

		notNull(response, "Cant get changes from null response");
		Field f = KarakuReflectionUtils.findField(response.getClass(),
				CHANGE_FIELDS);

		if (f == null) {
			f = ReflectionUtils
					.findField(response.getClass(), null, List.class);
		}

		notNull(f, "Cant get the id field, "
				+ "use the @ReplicationData annotation or create "
				+ "a field with name %s, please see %s",
				Arrays.toString(CHANGE_FIELDS), response.getClass().getName());
		f.setAccessible(true);
		Collection c = (Collection) ReflectionUtils.getField(f, response);

		notNull(c, "Null collection is not allowed in a replication response, "
				+ "please send a empty list. Response type: %s", response
				.getClass().getSimpleName());
		return c;
	}

	/**
	 * @param response
	 * @return
	 */
	private String getLastId(Object response) {

		notNull(response, "Cant get id from null response");

		Field f = KarakuReflectionUtils.findField(response.getClass(),
				ID_FIELDS);

		notNull(f, "Cant get the id field, please use the @ReplicationId "
				+ "annotation, or create a field with name %s, see %s",
				Arrays.toString(ID_FIELDS), response.getClass());

		f.setAccessible(true);

		Object id = ReflectionUtils.getField(f, response);
		notNull(id, "Id null in response is not allowed");

		return id.toString();
	}
}
