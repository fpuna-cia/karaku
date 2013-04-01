package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.business.ISIGHBaseLogic;

public interface IBaseController<T, ID extends Serializable> {

	public String doCancel();

	public void doCreate();

	public void doDelete();

	public void doEdit();

	public void doSearch();

	public ISIGHBaseLogic<T, ID> getBaseLogic();

	public abstract List<String> getBaseSearchItems();

	public T getBean();

	public List<T> getEntities();

	public String getFilterValue();

	public String getMessageIdName();

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto.
	 */
	public List<SelectItem> getSearchSelectItemsList();

	public boolean isCreate();

	public boolean isEdit();

	public boolean isSearch();

	public boolean isView();

	public void postCreate();

	public void postDelete();

	public void postEdit();

	public void postSearch();

	public void preCreate();

	public void preDelete();

	public void preEdit();

	public void preSearch();

	public void preView();

	public void setBean(T bean);

	public void setFilterValue(String filterValue);

}
