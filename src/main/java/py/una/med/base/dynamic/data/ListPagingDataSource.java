/*
 * @ListPagingDataSource.java 1.0 Jun 24, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.data;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link PagingDataSource} implementado utilizando una lista de objetos, la
 * paginación se realiza en memoria.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 24, 2013
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ListPagingDataSource extends AbstractPagingDataSource {

	private final List totalItems;
	private List currentItems;

	/**
	 * 
	 */
	public ListPagingDataSource(List<?> items) {

		this.totalItems = items;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.data.PagingDataSource#getTotalCount()
	 */
	@Override
	public Long getTotalCount() {

		return (long) totalItems.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.data.PagingDataSource#getItems()
	 */
	@Override
	public List<?> getItems() {

		return currentItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.data.PagingDataSource#refresh()
	 */

	@Override
	public void refresh() {

		Long inicio = (getCurrentPage() - 1) * getRegistersPerPage();
		Long fin = getCurrentPage() * getRegistersPerPage();
		if (fin > getTotalCount()) {
			fin = getTotalCount();
		}
		if (currentItems == null) {
			currentItems = new ArrayList(getRegistersPerPage().intValue());
		}
		currentItems.clear();
		for (Long i = inicio; i < fin; i++) {
			currentItems.add(totalItems.get(i.intValue()));
		}

	}
}
