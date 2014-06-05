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
package py.una.pol.karaku.test.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.replication.UriCache;
import py.una.pol.karaku.util.DateProvider;

/**
 * Componente que hereda de {@link UriCache} para dar mejor soporte a test.
 * 
 * <p>
 * Se crea el mÃ©todo {@link #hasInCache(String)} para que se pueda verificar
 * fÃ¡cilmente si se obtuvieron las referencias.
 * </p>
 * <p>
 * Y el mÃ©todo {@link #getEntityName(Class)} se sobreescribe para dar soporte a
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
