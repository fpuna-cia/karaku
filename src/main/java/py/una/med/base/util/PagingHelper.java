package py.una.med.base.util;

import java.io.Serializable;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.search.SearchParam;
import py.una.med.base.dao.util.EntityExample;

public class PagingHelper<T, ID extends Serializable> {

	private int rowsForPage = 10;
	private int page = 0;
	private T example;
	private Long currentCount;
	private Long totalCount;

	public PagingHelper(int rowsForPage) {

		this.rowsForPage = rowsForPage;
	}

	public ISearchParam getISearchparam() {

		SearchParam sp = new SearchParam();
		sp.setOffset(page * rowsForPage);
		sp.setLimit(rowsForPage);
		return sp;
	}

	public void next() {

		// TODO controlar que al llegar a la última pagina se deshabilite el
		// boton siguiente

		if (page + 1 > getMaxPage(currentCount)) {
			return;
		}
		this.page++;
	}

	public void goMaxPage() {

		this.page = getMaxPage(currentCount) - 1;

	}

	public void goInitPage() {

		this.page = 0;
	}

	public void last() {

		// TODO controlar que al llegar a la primera pagina se deshabilite el
		// boton atras
		if (page > 0) {
			this.page--;
		}

	}

	public int getPage() {

		return page;
	}

	public void setPage(int page) {

		this.page = page;
	}

	public String getFormattedPage() {

		Long firstRecord = Long.valueOf(page * rowsForPage + 1);
		Long limit = Long.valueOf(page * rowsForPage + rowsForPage);

		if (page + 1 == getMaxPage(currentCount)) {
			limit = currentCount;
		}

		if (currentCount == 0) {
			limit = 0L;
			firstRecord = 0L;
		}
		String formattedPage = firstRecord + "-" + limit + " de "
				+ currentCount;

		return formattedPage;
	}

	private int getMaxPage(Long count) {

		// TODO cambiar la forma de redondear hacia arriba, usar ceil

		double total = totalCount;
		double rowForPage = rowsForPage;
		int maxPage = (int) (count / rowsForPage);

		if (total / rowForPage > maxPage) {
			maxPage++;
		}

		return maxPage;
	}

	public boolean hasNext() {

		if (page + 1 == getMaxPage(currentCount)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean hasLast() {

		if (page == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void calculate(ISIGHBaseLogic<T, ID> logic, Where<T> where) {

		currentCount = logic.getCountByExample(new EntityExample<T>(example));
		totalCount = logic.getCount();
	}
}
