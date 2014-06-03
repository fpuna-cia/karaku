package py.una.pol.karaku.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.pol.karaku.business.ISIGHBaseLogic;

/**
 * 
 * @deprecated Utilizar {@link ISIGHAdvancedController}
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 12, 2013
 * 
 */
@Deprecated
public interface IBaseController<T, K extends Serializable> {

	String doCancel();

	void doCreate();

	void doDelete();

	void doEdit();

	void doSearch();

	ISIGHBaseLogic<T, K> getBaseLogic();

	List<String> getBaseSearchItems();

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
