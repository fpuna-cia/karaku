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
package py.una.pol.karaku.replication.server;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public class Bundle<T> implements Iterable<Change<T>> {

	/**
	 * Identificador utilizado cuando no se conoce el estado.
	 */
	@Nonnull
	public static final String ZERO_ID = "ZERO";

	/**
	 * Identificador utilizado cuando se envia el primer cambio.
	 */
	public static final String FIRST_CHANGE = "0";

	private Deque<Change<T>> changes;

	private String lastId;

	/**
	 *
	 */
	public Bundle() {

		this(null);
	}

	/**
	 * @param identificador
	 *            a usar cuando no hay cambios.
	 */
	public Bundle(String lastId) {

		changes = new LinkedList<Change<T>>();
		this.lastId = lastId;
	}

	@Nonnull
	public String getLastId() {

		String last = lastId;
		Change<T> c = changes.peekLast();

		if (c != null) {
			last = c.getId();
		}

		if (last == null) {
			last = Bundle.ZERO_ID;
		}

		return last;
	}

	/**
	 * Tamaño del cambio.
	 * 
	 * @return
	 */
	public int size() {

		return changes.size();
	}

	@Override
	public Iterator<Change<T>> iterator() {

		return changes.iterator();
	}

	public Change<T> add(@Nonnull T entity, @Nonnull String id) {

		Change<T> nC = new Change<T>(entity, id);
		changes.add(nC);
		return nC;
	}

	/**
	 * Retorna un {@link Set} que contiene todas las entidades que sufrieron
	 * cambios.
	 * 
	 * @return set de entidades, nunca <code>null</code>.
	 */
	@Nonnull
	public Set<T> getEntities() {

		Set<T> set = new HashSet<T>(size());
		for (Change<T> c : this) {
			set.add(c.getEntity());
		}
		return set;
	}
}
