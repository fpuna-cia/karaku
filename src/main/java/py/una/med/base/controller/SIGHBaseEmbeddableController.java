package py.una.med.base.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.restrictions.Where;
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
public abstract class SIGHBaseEmbeddableController<T, K extends Serializable>
		extends SIGHAdvancedController<T, K> implements
		ISIGHAdvancedController<T, K>, ISIGHEmbeddableController {

	/**
	 * Constante que determina el valor por defecto de la cantidad de registros
	 * a ser mostrada por este controlador en modo vista cuando esta en modo
	 * embebido.
	 * 
	 * @see #isEmbedded
	 */
	public static final int ROWS_PER_PAGE_IF_EMBEDDED = 5;

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
	public List<T> getEntities() {

		if (isEmbedded && getMainController().isEditingHeader()) {
			return Collections.emptyList();
		}
		return super.getEntities();
	}

	@Override
	public ISIGHMainController getMainController() {

		return mainController;
	}

	@Override
	public String getHeaderPath() {

		return mainController.getHeaderPath();
	}

	@Override
	public void delegateChild(Object child) {

		if (!getClazz().isAssignableFrom(child.getClass())) {
			throw new IllegalArgumentException(
					"Intentando delegar un hijo que no corresponde a la clase del controller");
		}
		mainController.configureChildBean(child);

	}

	public Object getHeaderBean() {

		return mainController.getHeaderBean();
	}

	@Override
	public void init() {

		reloadEntities();
	}

	@Override
	public Where<T> getBaseWhere() {

		Where<T> where = super.getBaseWhere();

		if (isEmbedded) {
			if (where.getExample() == null) {
				where.setExample(getBaseEntity());
			}
			mainController.configureBaseWhere(where, getClazz());
		}
		return where;
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String doEdit() {

		if (mainController != null) {
			delegateChild(getBean());
		}
		return super.doEdit();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String doDelete() {

		return super.doDelete();
	}

	/**
	 * Metodo que se encarga de la craecion, delega la responsabilidad al
	 * controler principal y luego llama a {@link SIGHAdvancedController}
	 * heredado para que se encarge de guardar.
	 */
	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doCreate() {

		if (mainController != null) {
			delegateChild(getBean());
		}
		return super.doCreate();
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

	@SuppressWarnings("unchecked")
	@Override
	public String getDefaultPermission() {

		if (mainController == null) {
			return super.getDefaultPermission();
		} else {
			return ((ISIGHBaseController<T, K>) mainController)
					.getDefaultPermission();
		}
	}

	public boolean canEditDetail() {

		if (!isEmbedded) {
			return true;
		} else {
			return mainController.embeddableListCanEdit();
		}
	}

	public boolean canDeleteDetail() {

		if (!isEmbedded) {
			return true;
		} else {
			return mainController.embeddableListCanDelete();
		}
	}

	@Override
	public int getRowsForPage() {

		if (isEmbedded) {
			return ROWS_PER_PAGE_IF_EMBEDDED;
		} else {
			return super.getRowsForPage();
		}

	}

	@Override
	public void preSearch() {

		super.preSearch();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public void doSearch() {

		controllerHelper.updateModel(getMessageIdName() + "_pgSearch");

		setExample(getBean());
		reloadEntities();
	}

	public boolean isEditable(String campo) {

		if (getMode() == null) {
			setMode(Mode.VIEW);
		}
		switch (getMode()) {
			case VIEW:
				return true;
			case EDIT:
				return true;
			case NEW:
				return true;
			case DELETE:
				return false;
			case SEARCH:
				return true;
			default:
				break;
		}
		return true;
	}
}
