package py.una.med.base.util;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.dao.restrictions.Where;

/**
 *
 *
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 23/08/2013
 *
 */
public class SIGHListHelperInMemory<T> implements KarakuListHelperProvider<T> {

	private List<T> listMemory;
	private SimpleFilter simpleFilter;

	public SIGHListHelperInMemory(List<T> list) {

		this(new SimpleFilter(), list);
	}

	public SIGHListHelperInMemory(SimpleFilter simpleFilter, List<T> list) {

		this.listMemory = list;
		this.simpleFilter = simpleFilter;
	}

	@Override
	public List<T> getEntities() {

		return listMemory;
	}

	@Override
	public SimpleFilter getSimpleFilter() {

		return simpleFilter;
	}

	@Override
	public List<SelectItem> getFilterOptions() {

		return new ArrayList<SelectItem>();
	}

	public List<T> getListMemory() {

		return listMemory;
	}

	public void setListMemory(List<T> listMemory) {

		this.listMemory = listMemory;
	}

	@Override
	public void setBaseWhere(Where<T> where) {

		// TODO Auto-generated method stub

	}

	@Override
	public PagingHelper getHelper() {

		return null;
	}

}
