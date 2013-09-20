package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.business.ISIGHBaseLogic;

@Deprecated
public interface IBaseController<T, ID extends Serializable> {

	String doCancel();

	void doCreate();

	void doDelete();

	void doEdit();

	void doSearch();

	ISIGHBaseLogic<T, ID> getBaseLogic();

	abstract List<String> getBaseSearchItems();

	T getBean();

	List<T> getEntities();

	String getFilterValue();

	String getMessageIdName();

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto.
	 */
	List<SelectItem> getSearchSelectItemsList();

	boolean isCreate();

	boolean isEdit();

	boolean isSearch();

	boolean isView();

	void postCreate();

	void postDelete();

	void postEdit();

	void postSearch();

	void preCreate();

	void preDelete();

	void preEdit();

	void preSearch();

	void preView();

	void setBean(T bean);

	void setFilterValue(String filterValue);

}
