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
package py.una.pol.karaku.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.pol.karaku.business.IKarakuBaseLogic;

/**
 * 
 * @deprecated Utilizar {@link IKarakuAdvancedController}
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

	IKarakuBaseLogic<T, K> getBaseLogic();

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
