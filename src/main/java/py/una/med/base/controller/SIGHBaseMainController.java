/*
 * @SIGHBaseMainController.java 1.0
 */
package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import org.apache.myfaces.orchestra.conversation.Conversation;
import org.richfaces.event.ItemChangeEvent;
import py.una.med.base.security.HasRole;
import py.una.med.base.security.SIGHSecurity;

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
public abstract class SIGHBaseMainController<T, ID extends Serializable>
		extends SIGHAdvancedController<T, ID> implements
		ISIGHAdvancedController<T, ID>, ISIGHMainController {

	@Override
	public abstract List<ISIGHEmbeddableController> configureEmbeddableControllers();

	@Override
	public abstract String getHeaderPath();

	@Override
	public abstract Object configureChildBean(Object childBean);

	@Override
	public void init() {

		for (ISIGHEmbeddableController controller : getEmbeddableControllers()) {
			controller.setMainController(this);
			controller.init();
		}
	}

	List<ISIGHEmbeddableController> controllers;

	@Override
	public List<ISIGHEmbeddableController> getEmbeddableControllers() {

		if (controllers == null) {
			controllers = configureEmbeddableControllers();
		}

		return controllers;
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String doDelete() {

		return super.doDelete();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doCreate() {

		for (ISIGHEmbeddableController controller : getEmbeddableControllers()) {
			controller.save();
		}
		super.doCreate();

		setMode(Mode.EDIT);
		postCreate();
		return "";
	}

	@Override
	public void save() {

		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() {

		Conversation.getCurrentInstance().invalidate();
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
	public String postCreate() {

		return "";
	}

	@Override
	public String preList() {

		return super.preList();
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
		return super.preCreate();
	}

	@Override
	public String preView() {

		init();
		return super.preView();
	}

	@Override
	public void tabChange(ItemChangeEvent event) {

		// PONER que si son iguale sno hace rnada

		if (event.getOldItem() == event.getNewItem()) {
			return;
		}
		if ("firstTab".equals(event.getOldItemName())
				|| event.getOldItemName() == null) {
			if (getBaseLogic().getIdValue(getBean()) != null) {
				edit(getBean(), false);
			}
		}

	}

	/**
	 * Define si las tabs deben estar habilitadas o no
	 * 
	 * @return true si setan deshabilitadas y false si deben estar habilitadas
	 */
	public boolean getTabDisabled() {

		if (getBaseLogic().getIdValue(getBean()) == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object getHeaderBean() {

		return getBean();
	}
}
