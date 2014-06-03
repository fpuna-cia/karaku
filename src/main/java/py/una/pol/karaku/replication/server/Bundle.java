/*
 * @Bundle.java 1.0 Nov 7, 2013 Sistema Integral de Gestion Hospitalaria
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
