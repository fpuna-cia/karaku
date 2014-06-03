/*
 * @TestUriCache.java 1.0 Jan 7, 2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.replication.UriCache;
import py.una.med.base.util.DateProvider;

/**
 * Componente que hereda de {@link UriCache} para dar mejor soporte a test.
 * 
 * <p>
 * Se crea el método {@link #hasInCache(String)} para que se pueda verificar
 * fácilmente si se obtuvieron las referencias.
 * </p>
 * <p>
 * Y el método {@link #getEntityName(Class)} se sobreescribe para dar soporte a
 * clases anidadas como las que se utilizan en este test.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 27, 2013
 * 
 */
public class TestUriCache extends UriCache {

	Map<String, Date> inserts = new HashMap<String, Date>();

	@Autowired
	private DateProvider dateProvider;

	@Override
	public void addToCache(String uri, Object entity) {

		super.addToCache(uri, entity);
		inserts.put(uri, dateProvider.getNow());
	}

	public boolean hasBeenAddedNow(String uri) {

		return inserts.get(uri).equals(dateProvider.getNow());
	}

	public boolean hasInCache(String uri) {

		return getMap().containsKey(uri);
	}

	public int getCount() {

		return getMap().size();
	}

}
