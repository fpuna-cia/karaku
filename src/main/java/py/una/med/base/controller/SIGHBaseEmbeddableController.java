package py.una.med.base.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.security.HasRole;
import py.una.med.base.security.SIGHSecurity;
import py.una.med.base.util.ControllerHelper;

/**
 * Implementacion base de la interfaz {@link ISIGHEmbeddableController}
 * 
 * @author Arturo Volpe
 * @since 2.0, 30/01/2013
 * @version 1.0
 * 
 * @param <T>
 *            Clase de la Entidad que maneja este controller
 * @param <ID>
 *            Clase del id de la entidad
 */
public abstract class SIGHBaseEmbeddableController<T, ID extends Serializable>
		extends SIGHAdvancedController<T, ID> implements
		ISIGHAdvancedController<T, ID>, ISIGHEmbeddableController {

	private ISIGHMainController mainController;

	private boolean isEmbedded;

	@Autowired
	private ControllerHelper helper;

	@Override
	public void setMainController(ISIGHMainController mainController) {

		isEmbedded = mainController != null;
		this.mainController = mainController;
	}

	@Override
	public String getHeaderPath() {

		return mainController.getHeaderPath();
	}

	@Override
	public void delegateChild(Object child) {

		if (!getClazz().isAssignableFrom(child.getClass())) {
			throw new RuntimeException(
					"Intentando delegar un hijo que no corresponde a la clase del controller");
		}
		mainController.configureChildBean(child);

	}

	@Override
	public List<T> getEntities() {

		if (isEmbedded) {
			Where<T> where = getWhere();
			mainController.configureBaseWhere(where, getClazz());
			if (mainController.getHeaderBeanID() != null) {
				List<T> aRet = getBaseLogic().getAll(where, null);
				return aRet;
			}
			return new ArrayList<T>();
		} else {
			return super.getEntities();
		}
	}

	public Object getHeaderBean() {

		return mainController.getHeaderBean();
	}

	@Override
	public void init() {

	}

	/**
	 * Retorna un where configurado para ser utilizado en las consultas, si hay
	 * algun example, o algun filtro se configura con los filtros adecuados.
	 * 
	 * @return Where base para agregar mas restricciones
	 */
	public Where<T> getWhere() {

		Where<T> where = getFilters();
		if (where == null) {
			where = new Where<T>();
		}

		EntityExample<T> example = where.getExample();
		if (example == null) {
			example = new EntityExample<T>(getBaseEntity());
			where.setExample(example);
		}
		return where;
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String doEdit() {

		try {
			if (mainController != null) {
				delegateChild(getBean());
			}
			edit(getBean());
			return postEdit();
		} catch (Exception e) {
			helper.showException(e);

			return "";
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String doDelete() {

		try {
			delete(getBean());
			return super.postDelete();
		} catch (Exception e) {

			helper.showException(e);
			return "";
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doCreate() {

		try {
			if (mainController != null) {
				delegateChild(getBean());

			}
			create(getBean());
			return postCreate();
		} catch (Exception e) {
			helper.showException(e);
			return "";
		}
	}

	@Override
	public void save() {

		// setMainController(null);
	}

	@Override
	public void cancel() {

		setMainController(null);
	}

	@Override
	public String goList() {

		if (isEmbedded) {
			return mainController.childrenGoView();
		}
		return super.goList();
	}

	@Override
	public String preEdit() {

		return super.preEdit();
	}

	@Override
	public String preCreate() {

		return super.preCreate();
	}

	@Override
	public abstract String goEdit();

	@Override
	public String goNew() {

		return goEdit();
	}

	@Override
	public String goDelete() {

		return goEdit();
	}

	@Override
	public String goView() {

		return goEdit();
	}

	@Override
	public String getDefaultPermission() {

		if (mainController == null) {
			return super.getDefaultPermission();
		} else {
			return ((SIGHBaseController) mainController).getDefaultPermission();
		}
	}

}
