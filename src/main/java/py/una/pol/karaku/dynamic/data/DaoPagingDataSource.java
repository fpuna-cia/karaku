/*
 * @DaoPagingDataSource.java 1.0 Jun 24, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dynamic.data;

import java.util.List;
import py.una.pol.karaku.dao.BaseDAO;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.domain.BaseEntity;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 24, 2013
 * 
 */
public class DaoPagingDataSource<T extends BaseEntity> extends
		AbstractPagingDataSource {

	private final BaseDAO<T, Long> dao;
	private List<T> items;

	/**
	 * 
	 */
	public DaoPagingDataSource(BaseDAO<T, Long> dao) {

		this.dao = dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#getTotalCount()
	 */
	@Override
	public Long getTotalCount() {

		return dao.getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#getItems()
	 */
	@Override
	public List<?> getItems() {

		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#refresh()
	 */
	@Override
	public void refresh() {

		ISearchParam sp = new SearchParam();
		sp.setLimit(getRegistersPerPage().intValue());
		sp.setOffset(getCurrentPage().intValue()
				* getRegistersPerPage().intValue());
		items = dao.getAll(sp);

	}

}
