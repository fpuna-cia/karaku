/*
 * @SIGHBaseMainController.java 1.0
 */
package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.apache.myfaces.orchestra.conversation.Conversation;
import org.richfaces.event.ItemChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.security.HasRole;
import py.una.med.base.security.SIGHSecurity;
import py.una.med.base.util.ControllerHelper;

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
public abstract class SIGHBaseMainController<T, K extends Serializable> extends
		SIGHAdvancedController<T, K> implements ISIGHAdvancedController<T, K>,
		ISIGHMainController {

	boolean editingHeader;

	private List<ISIGHEmbeddableController> controllers;

	@Autowired
	private ControllerHelper helper;

	@Autowired
	private AuthorityController authorityController;

	@Override
	public abstract List<ISIGHEmbeddableController> configureEmbeddableControllers();

	@Override
	public abstract String getHeaderPath();

	@Override
	public abstract Object configureChildBean(Object childBean);

	@Override
	public void init() {

		getUsarController();
		for (ISIGHEmbeddableController controller : getEmbeddableControllers()) {
			controller.setMainController(this);
			controller.init();
		}
	}

	@Override
	public List<ISIGHEmbeddableController> getEmbeddableControllers() {

		if (controllers == null) {
			controllers = configureEmbeddableControllers();
		}

		return controllers;
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doSave() {

		if (getMode().equals(Mode.NEW)) {
			super.doCreate();
			return preList();
		} else {
			super.doEdit();
			for (ISIGHEmbeddableController controller : getEmbeddableControllers()) {
				controller.save();
			}
			return preList();
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doSaveAndContinue() {

		try {
			if (getMode().equals(Mode.NEW)) {
				setBean(create(getBean()));
			} else {
				setBean(edit(getBean()));
			}
			for (ISIGHEmbeddableController controller : getEmbeddableControllers()) {
				controller.save();
			}
			setEditingHeader(false);
			this.setMode(Mode.EDIT);
			return goEdit();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						"BASE_ABM_EDIT_FAILURE", e.getMessage());
			}
			return "";
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
		for (ISIGHEmbeddableController controller : getEmbeddableControllers()) {
			controller.setMainController(null);
		}

	}

	@Override
	public String goList() {

		cancel();
		return super.goList();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String preEdit() {

		init();
		return super.preEdit();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String preDelete() {

		init();
		return super.preDelete();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
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

		if (getBaseLogic().getIdValue(getBean()) == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEditable(String campo) {

		if (!isEditingHeader()) {
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
