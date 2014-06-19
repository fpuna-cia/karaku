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
import javax.faces.application.FacesMessage;
import org.apache.myfaces.orchestra.conversation.Conversation;
import org.richfaces.event.ItemChangeEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.security.AuthorityController;
import py.una.pol.karaku.security.HasRole;
import py.una.pol.karaku.security.KarakuSecurity;
import py.una.pol.karaku.util.ControllerHelper;

/**
 * 
 * Clase que implementa las funcionalidades basicas de un controller que tiene
 * varios controlles embebidos.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.1 Feb 18, 2013
 * @param <T>
 *            entidad
 * @param <ID>
 *            clase de la clave primaria de la entidad
 * 
 */
public abstract class KarakuBaseMainController<T, K extends Serializable>
		extends KarakuAdvancedController<T, K> implements
		IKarakuAdvancedController<T, K>, IKarakuMainController {

	protected static final String FAILURE = "fail";

	protected static final String SUCCESS = "success";

	@Log
	private Logger log;

	private boolean editingHeader;

	private List<IKarakuEmbeddableController> controllers;

	@Autowired
	private ControllerHelper helper;

	@Autowired
	private AuthorityController authorityController;

	@Override
	public abstract List<IKarakuEmbeddableController> configureEmbeddableControllers();

	@Override
	public abstract String getHeaderPath();

	@Override
	public abstract Object configureChildBean(Object childBean);

	@Override
	public void init() {

		getUsarController();
		for (IKarakuEmbeddableController controller : getEmbeddableControllers()) {
			controller.setMainController(this);
			controller.init();
		}
	}

	@Override
	public List<IKarakuEmbeddableController> getEmbeddableControllers() {

		if (controllers == null) {
			controllers = configureEmbeddableControllers();
		}

		return controllers;
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String doSave() {

		if (trySave()) {
			setMode(Mode.LIST);
			return goList();
		} else {
			return "";
		}
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String doSaveAndContinue() {

		if (trySave()) {
			this.setMode(Mode.EDIT);
			return goEdit();
		} else {
			return "";
		}
	}

	private boolean trySave() {

		String toRet;

		if (getMode().equals(Mode.NEW)) {
			toRet = doCreate();
			postCreate();
		} else {
			toRet = doEdit();
			postEdit();
		}

		if (toRet.equals(FAILURE)) {
			return false;
		} else {
			for (IKarakuEmbeddableController controller : getEmbeddableControllers()) {
				controller.save();
			}
			setEditingHeader(false);
			return true;
		}
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String doCreate() {

		try {
			setBean(create(getBean()));
			reloadEntities();
			helper.createGlobalFacesMessageSimple(FacesMessage.SEVERITY_INFO,
					getMessageCreateSuccess());

			return SUCCESS;
		} catch (Exception e) {
			if (!handleException(helper.convertException(e, getClazz()))) {
				log.warn("doCreate failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						getMessageCreateFailure(), e.getMessage());
			}
			return FAILURE;
		}
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_EDIT)
	public String doEdit() {

		try {
			edit(getBean());
			reloadEntities();
			helper.createGlobalFacesMessageSimple(FacesMessage.SEVERITY_INFO,
					getMessageEditSuccess());
			return SUCCESS;
		} catch (Exception e) {
			if (!handleException(helper.convertException(e, getClazz()))) {
				log.warn("doCreate failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						getMessageEditFailure(), e.getMessage());
			}
			return FAILURE;
		}
	}

	@Override
	public void save() {

	}

	@Override
	public void cancel() {

		if (Conversation.getCurrentInstance() != null) {
			Conversation.getCurrentInstance().invalidate();
		}
		for (IKarakuEmbeddableController controller : getEmbeddableControllers()) {
			controller.setMainController(null);
		}

	}

	@Override
	public String postEdit() {

		return "";
	}

	@Override
	public String postCreate() {

		return "";
	}

	@Override
	public String goList() {

		cancel();
		return super.goList();
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_EDIT)
	public String preEdit() {

		init();
		return super.preEdit();
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_DELETE)
	public String preDelete() {

		init();
		return super.preDelete();
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String preCreate() {

		init();
		setEditingHeader(true);
		return super.preCreate();
	}

	@Override
	public String preView() {

		init();
		return super.preView();
	}

	@Override
	public void tabChange(ItemChangeEvent event) {

	}

	/**
	 * Define si las tabs deben estar habilitadas o no
	 * 
	 * @return true si estan deshabilitadas y false si deben estar habilitadas
	 */
	public boolean getTabDisabled() {

		return getBaseLogic().getIdValue(getBean()) == null;
	}

	@Override
	public boolean isEditable(String campo) {

		if (!isEditingHeader() && !isSearch()) {
			return false;
		}
		return super.isEditable(campo);
	}

	@Override
	public Object getHeaderBean() {

		return getBean();
	}

	public boolean saveButtonVisible() {

		if (!isEditingHeader()) {
			return false;
		}
		if (this.getMode() == Mode.NEW
				&& authorityController.hasRole(getCreatePermission())) {
			return true;
		}
		if (getMode() == Mode.EDIT
				&& authorityController.hasRole(getEditPermission())) {
			return true;
		}
		return false;
	}

	public boolean cancelButtonVisible() {

		if (!isEditingHeader()) {
			return false;
		}
		if (this.getMode() == Mode.NEW) {
			return true;
		}
		if (getMode() == Mode.EDIT) {
			return true;
		}
		return false;
	}

	public boolean editButtonVisible() {

		if (getMode() == Mode.EDIT && !isEditingHeader()
				&& authorityController.hasRole(getEditPermission())) {
			return true;
		}
		return false;
	}

	@Override
	public String doCancel() {

		if (getMode() == Mode.NEW) {
			return preList();
		} else {
			resetHeaderBean();
			setEditingHeader(false);
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void resetHeaderBean() {

		setBean(getBaseLogic().getById((K) getHeaderBeanID()));
	}

	@Override
	public boolean isEditingHeader() {

		return editingHeader;
	}

	public void setEditingHeader(boolean editing) {

		this.editingHeader = editing;
	}

	@Override
	public String getCancelText() {

		return getMessage("BASE_ADVANCED_ABM_CANCEL");
	}

	public String beginEditHeader() {

		this.setMode(Mode.EDIT);
		setEditingHeader(true);
		return "";
	}

	@Override
	public boolean embeddableListCanEdit() {

		if (this.editingHeader) {
			return false;
		}
		if (this.getMode().equals(Mode.EDIT)
				&& authorityController.hasRole(getEditPermission())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean embeddableListCanDelete() {

		if (this.editingHeader) {
			return false;
		}
		if (embeddableListCanEdit()) {
			return true;
		}
		if (this.getMode().equals(Mode.DELETE)
				&& authorityController.hasRole(getDeletePermission())) {
			return true;
		}
		return false;

	}

	@Override
	public String preList() {

		cancel();
		return super.preList();
	}

}
