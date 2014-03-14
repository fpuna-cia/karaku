package py.una.med.base.util;

import java.util.Collections;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;

/**
 * Lista de objetos en memoria con paginación.
 * 
 * 
 * 
 * @author Nathalia Ochoa
 * @author Claudia Valenzuela
 * @since 1.0
 * @version 1.0 23/08/2013
 * 
 */
public class SIGHListHelperInMemory<T> implements KarakuListHelperProvider<T> {

	private List<T> listMemory;
	private SimpleFilter simpleFilter;
	private PagingHelper pagingHelper;

	public SIGHListHelperInMemory(List<T> list) {

		this(new SimpleFilter(), list);
	}

	public SIGHListHelperInMemory(SimpleFilter simpleFilter, List<T> list) {

		setListMemory(list);
		this.simpleFilter = simpleFilter;

	}

	@Override
	public List<T> getEntities() {

		List<T> toShow = doFilter(listMemory, getSimpleFilter().getOption(),
				getSimpleFilter().getValue());

		long size = toShow.size();
		getHelper().udpateCount(size);

		ISearchParam isp = getHelper().getISearchparam();
		int from = isp.getOffset();
		int to = isp.getOffset() + isp.getLimit();
		if (to > listMemory.size()) {
			to = listMemory.size();
		}

		return toShow.subList(from, to);
	}

	/**
	 * Realiza el filtro de elementos.
	 * 
	 * <p>
	 * Este método es utilizado antes de calcular la paginación y debe retornar
	 * una sublista de los elementos
	 * <p>
	 * 
	 * 
	 * @param toFilter
	 * @param option
	 * @param value
	 * @return
	 */
	public List<T> doFilter(List<T> toFilter, String option, String value) {

		return toFilter;
	}

	@Override
	public SimpleFilter getSimpleFilter() {

		return simpleFilter;
	}

	@Override
	public List<SelectItem> getFilterOptions() {

		return Collections.emptyList();
	}

	public List<T> getListMemory() {

		return listMemory;
	}

	public final void setListMemory(List<T> listMemory) {

		if (listMemory == null) {
			setListMemory(Collections.<T> emptyList());
			return;
		}
		this.listMemory = listMemory;
	}

	@Override
	public void setBaseWhere(Where<T> where) {

		// TODO Auto-generated method stub
	}

	@Override
	public PagingHelper getHelper() {

		if (pagingHelper == null) {
			pagingHelper = new PagingHelper(5);
		}

		return pagingHelper;
	}

}
