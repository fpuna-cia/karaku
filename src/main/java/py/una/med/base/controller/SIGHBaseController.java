/*
 * SIGHBaseController.java
 */

package py.una.med.base.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.breadcrumb.BreadcrumbController;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.business.reports.SIGHBaseReportSimple;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.domain.Menu;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.exception.ReportException;
import py.una.med.base.jsf.utils.CurrentPageHelper;
import py.una.med.base.log.Log;
import py.una.med.base.reports.Column;
import py.una.med.base.security.HasRole;
import py.una.med.base.security.SIGHSecurity;
import py.una.med.base.util.ControllerHelper;
import py.una.med.base.util.EntitySerializer;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.PagingHelper;
import py.una.med.base.util.PagingHelper.ChangeListener;
import py.una.med.base.util.SelectHelper;
import py.una.med.base.util.StringUtils;

/**
 * Controlador base para todos los controladores del sistema, implementa las
 * funcionalidades definidas en {@link ISIGHBaseController}.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.5 Aug 1, 2013
 * @see ISIGHBaseController
 * 
 */
public abstract class SIGHBaseController<T, K extends Serializable> implements
		ISIGHBaseController<T, K> {

	/**
	 * Vector que contiene las columnas por las que se intentará ordenar.
	 */
	private static String[] DEFAULT_SORT_COLUMNS = { "descripcion", "id" };
	@Autowired
	private BreadcrumbController breadcrumController;

	@Autowired
	private CurrentPageHelper currentPageHelper;

	private static final int ROWS_FOR_PAGE = 10;

	private Mode mode = Mode.VIEW;

	private String filterValue;

	private String filterOption;

	private T bean;

	private T example;

	@Log
	protected Logger log;

	private PagingHelper pagingHelper;

	private String messageIdName;

	@Autowired
	private I18nHelper i18nHelper;

	@Autowired
	protected ControllerHelper controllerHelper;

	@Autowired
	private SIGHBaseReportSimple baseReportSimple;

	/**
	 * Lista de entidades mostradas actualmente, cualquier manipulación a ella
	 * se vera reflejada cuando se refresque la grilla, si se la marca como nula
	 * la siguiente vez que se pidan los datos será cargada. Utilizar el método
	 * {@link #reloadEntities()} para marcar la lista como sucia y que deba ser
	 * recargada.
	 */
	protected List<T> entities;

	@Override
	public void clearFilters() {

		log.info("Clear Filters called");
		setExample(null);
		setFilterValue("");
		setFilterOption("");
		reloadEntities();
	}

	public void createFacesMessage(Severity severity, String summary,
			String code, String componentId) {

		controllerHelper.createFacesMessage(severity, summary, code,
				componentId);
	}

	public void createGlobalFacesMessage(Severity severity, String code) {

		controllerHelper.createGlobalFacesMessage(severity, code);
	}

	public void createGlobalFacesMessage(Severity severity, String summary,
			String code) {

		controllerHelper.createGlobalFacesMessage(severity, summary, code);
	}

	@Override
	public String doCancel() {

		log.info("DoCancel llamado");
		return "";
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doCreate() {

		log.info("doCreate llamado");
		try {
			getBaseLogic().add(bean);
			reloadEntities();
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"BASE_ABM_CREATE_SUCCESS");
			postCreate();
			return goList();
		} catch (Exception e) {
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_WARN, "BASE_ABM_CREATE_FAILURE",
					e.getMessage());
			return "";
		}

	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String doDelete() {

		try {
			log.info("Do Delete llamado");
			getBaseLogic().remove(getBean());
			reloadEntities();
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"MESSAGE_DELETE_SUCCESS");
			return goList();
		} catch (Exception e) {
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			return "";
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String doEdit() {

		try {
			log.info("Do edit llamado");
			getBaseLogic().update(bean);
			reloadEntities();
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"BASE_ABM_EDIT_SUCCESS");
			return goList();
		} catch (Exception e) {
			log.error("Error al editar", e);
			controllerHelper.createGlobalFacesMessage(
					FacesMessage.SEVERITY_ERROR, "", "BASE_ABM_EDIT_FAILURE");
			return "";
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public void doSearch() {

		controllerHelper.updateModel("pgSearch");

		log.info("do search llamado");
		setExample(getBean());
		reloadEntities();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public void doSimpleSearch() {

		log.info("doSimpleSearch llamado");

		if (!StringUtils.isValid(getFilterOption())
				|| !StringUtils.isValid(getFilterValue())) {
			createGlobalFacesMessage(FacesMessage.SEVERITY_WARN, "",
					"BASE_ABM_SEARCH_VALUE_OPTION_REQUIRED");
			return;
		} else {
			setExample(null);

			reloadEntities();
		}

	}

	@Override
	public abstract ISIGHBaseLogic<T, K> getBaseLogic();

	/**
	 * Retorna una lista de cadenas representando las opciones por las cuales el
	 * caso de uso podra buscar, es un método de utilidad para
	 * {@link #getSearchSelectItemsList()}
	 * 
	 * @return lista de strings
	 * @see #getSearchSelectItemsList()
	 */
	public abstract List<String> getBaseSearchItems();

	@Override
	public T getBean() {

		if (bean == null) {
			bean = getBaseLogic().getNewInstance();
		}
		return bean;
	}

	@Override
	public Where<T> getWhereReport() {

		if (getExample() != null) {
			Where<T> where = new Where<T>();
			where.setExample(getExample());
			return where;
		}
		if (getFilterValue() != null && !getFilterValue().equals("")) {
			return getSimpleFilters();
		}
		return new Where<T>();
	}

	@Override
	public Map<String, Object> getParamsFilter(Map<String, Object> paramsReport) {

		if (example != null) {
			paramsReport.put("selectionFilters",
					EntitySerializer.serialize(getExample()));
			return paramsReport;
		}
		if (getFilterValue() != null && !getFilterValue().equals("")) {
			paramsReport.put("selectionFilters", getFilterOption() + ": "
					+ getFilterValue());
			return paramsReport;
		}
		return paramsReport;

	}

	@Override
	public void generateReport(String type) {

		HashMap<String, Object> paramsReport = new HashMap<String, Object>();
		paramsReport.put("titleReport", getHeaderReport());
		Class<T> clazz = getBaseLogic().getDao().getClassOfT();
		try {
			baseReportSimple
					.generateReport(getParamsFilter(paramsReport), type,
							getColumns(), getBaseLogic(), getWhereReport(),
							clazz);
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
					"BASE_REPORT_CREATE_SUCCESS");
		} catch (ReportException e) {
			log.warn("Generate report failed", e);
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
					"BASE_REPORT_CREATE_FAILURE");
		}
	}

	@Override
	public void generateReportDetail(String type) {

	}

	@Override
	public List<Column> getColumns() {

		return controllerHelper.getColumns();
	}

	@Override
	public String getHeaderReport() {

		Class<T> clazz = getBaseLogic().getDao().getClassOfT();
		List<String> listName = StringUtils.split(clazz.getSimpleName());
		String nameList = StringUtils.pluralize(listName);
		return i18nHelper.getString("BASE_REPORT_NAME") + " " + nameList;
	}

	@Override
	public List<T> getEntities() {

		if (entities == null) {
			loadEntities();
		}
		return entities;
	}

	public void loadEntities() {

		log.debug("SIGHBaseController.loadEntities()");

		Where<T> baseWhere = getBaseWhere();
		getPagingHelper().udpateCount(getBaseLogic().getCount(baseWhere));

		ISearchParam sp = getPagingHelper().getISearchparam();
		configureSearchParam(sp);

		entities = getBaseLogic().getAll(baseWhere, sp);
	}

	@Override
	public Where<T> getBaseWhere() {

		if (getExample() != null) {
			Where<T> where = new Where<T>();
			where.setExample(getExample());
			return where;
		}
		return getSimpleFilters();
	}

	/**
	 * Método que debe ser implementado en la clase que desea definir alguna
	 * configuración especial en la obtención de valores desde la base de datos
	 * 
	 * <p>
	 * La implementación por defecto, intenta ordenar por (si no puede ordenar
	 * por un atributo, pasa al siguiente):
	 * <ol>
	 * <li>descripcion</li>
	 * <li>id</li>
	 * </ol>
	 * </p>
	 * 
	 * @see #DEFAULT_SORT_COLUMNS
	 * @param sp
	 *            parámetro de búsqueda definido en el paginHelper y a ser
	 *            configurado
	 */
	@Override
	public void configureSearchParam(ISearchParam sp) {

		for (String s : DEFAULT_SORT_COLUMNS) {
			try {
				getBaseLogic().getDao().getClassOfT().getDeclaredField(s);
				sp.addOrder(s, true);
				return;
			} catch (Exception e) {
			}
		}
		throw new KarakuRuntimeException(
				"Tabla '"
						+ getBaseLogic().getDao().getTableName()
						+ "' no posee las columnas por defecto para ordenar "
						+ Arrays.toString(DEFAULT_SORT_COLUMNS)
						+ " por favor, reescriba el método configureSearchparam en el controlador: "
						+ getClass().getSimpleName());

	}

	@Override
	public T getExample() {

		return example;
	}

	public String getFilterOption() {

		return filterOption;
	}

	@Override
	public abstract Where<T> getSimpleFilters();

	public String getFilterValue() {

		return filterValue;
	}

	public String getMessage(String code) {

		return controllerHelper.getMessage(code);
	}

	/**
	 * Cada formulario tiene <h:message /> para mostrar los posibles errores,
	 * ponerle estáticamente un id es fácil pero hay un solo controlador
	 * genérico, por lo que el id debe ser único en ese caso. Si en una vista se
	 * quiere incluir varios formularios, surge el problema de duplicidad de
	 * componentes (dos o más h:message con el mismo id). <br/>
	 * Por lo tanto, se genera un id que tiene como prefijo el nombre de la
	 * entidad seguido de "faces_message". Ej: Usuario_faces_message
	 **/
	@Override
	public String getMessageIdName() {

		if (messageIdName == null) {
			messageIdName = getBaseLogic().getDao().getClassOfT()
					.getSimpleName()
					+ "_faces_message";
		}
		return messageIdName;
	};

	@Override
	public Mode getMode() {

		return mode;
	}

	/**
	 * Se crea un nuevo {@link PagingHelper} y se le asigna un
	 * {@link ChangeListener} que recarga la lista de entidades cada vez que hay
	 * un cambio
	 */
	@Override
	public PagingHelper getPagingHelper() {

		if (pagingHelper == null) {
			pagingHelper = new PagingHelper(getRowsForPage());
			pagingHelper.setChangeListener(new ChangeListener() {

				@Override
				public void onChange(PagingHelper thizz, int previousPage,
						int currentPage) {

					reloadEntities();
				}
			});
		}
		return pagingHelper;
	}

	public int getRowsForPage() {

		return ROWS_FOR_PAGE;
	}

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto (para la lista de filtros).
	 * 
	 * @return {@link List} de {@link SelectItem} que representan los criterios
	 *         por los cuales se puede buscar en este controller.
	 */
	@Override
	public List<SelectItem> getSearchSelectItemsList() {

		return SelectHelper.getSelectItems(getBaseSearchItems());

	}

	public String getUsarController() {

		// XXX Setea este controllador, como el controlador actual para el
		// breadcrum
		breadcrumController.setActualController(this);
		return "";
	}

	@Override
	public String goDelete() {

		return "abm.xhtml";
	}

	@Override
	public String goEdit() {

		return "abm.xhtml";
	}

	@Override
	public String goList() {

		return "list.xhtml";
	}

	@Override
	public String goNew() {

		return goEdit();
	}

	@Override
	public String goView() {

		return "abm.xhtml";
	}

	@Override
	public boolean isCreate() {

		return mode == Mode.NEW;
	}

	@Override
	public boolean isDelete() {

		return mode.equals(Mode.DELETE);
	}

	@Override
	public boolean isEdit() {

		return mode == Mode.EDIT;
	}

	public boolean isEditable(String campo) {

		log.trace("Verificando visibilidad de: " + campo);
		if (mode == null) {
			mode = Mode.VIEW;
		}
		switch (mode) {
			case VIEW:
				return false;
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

	@Override
	public boolean isSearch() {

		return mode == Mode.SEARCH;
	};

	@Override
	public boolean isView() {

		return mode == Mode.VIEW;
	}

	@Override
	public String postCreate() {

		mode = Mode.LIST;
		return goList();
	}

	@Override
	public String postDelete() {

		mode = Mode.LIST;
		return goList();
	}

	@Override
	public String postEdit() {

		mode = Mode.LIST;
		return goList();
	}

	@Override
	public void postSearch() {

		log.info("post search llamado");
		this.mode = Mode.VIEW;

	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String preCreate() {

		mode = Mode.NEW;
		setBean(getBaseLogic().getNewInstance());
		log.info("preCreate llamado");
		return goNew();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String preDelete() {

		log.info("Pre Delete llamado");
		this.mode = Mode.DELETE;
		return goDelete();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String preEdit() {

		mode = Mode.EDIT;
		log.info("Pre edit llamado");
		return goEdit();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public String preList() {

		setBean(null);
		this.mode = Mode.LIST;
		return goList();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public void preSearch() {

		log.info("pre search llamado");
		this.mode = Mode.SEARCH;
		setBean(getExample());
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public String preView() {

		mode = Mode.VIEW;
		log.info("pre View llamado");
		return goView();
	}

	@Override
	public void setBean(T bean) {

		this.bean = bean;
	}

	@Override
	public void setExample(T example) {

		this.example = example;
	}

	@Override
	public void setFilterOption(String filterOption) {

		this.filterOption = filterOption;
	}

	@Override
	public void setFilterValue(String filterValue) {

		this.filterValue = filterValue.toUpperCase();
	}

	public void setMode(Mode mode) {

		this.mode = mode;
	}

	@Override
	public String getDefaultPermission() {

		return "SIGH";
	}

	@Override
	public String getDeletePermission() {

		return getDefaultPermission();
	}

	@Override
	public String getCreatePermission() {

		return getDefaultPermission();
	}

	@Override
	public String getEditPermission() {

		return getDefaultPermission();
	}

	public String getCancelText() {

		if (getMode().equals(Mode.VIEW)) {
			return getMessage("BASE_FORM_CANCEL_VIEW");
		} else {
			return getMessage("BASE_FORM_CANCEL");
		}

	}

	@Autowired
	private BreadcrumbController breadController;

	@Override
	public String getHeaderText() {

		String header;
		switch (getMode()) {
			case EDIT: {
				header = "BASE_FORM_EDIT_HEADER";
				break;
			}
			case VIEW: {
				header = "BASE_FORM_VIEW_HEADER";
				break;
			}
			case DELETE: {
				header = "BASE_FORM_DELETE_HEADER";
				break;
			}
			case NEW: {
				header = "BASE_FORM_NEW_HEADER";
				break;
			}
			default: {
				header = "BREADCRUM_UNKNOWN";
			}
		}

		Menu actual = currentPageHelper.getCurrentMenu();
		if (actual == null) {
			return I18nHelper.getMessage(header);
		} else {
			return I18nHelper.getMessage(header) + " "
					+ I18nHelper.getMessage(actual.getName());
		}

	}

	@Override
	public void reloadEntities() {

		entities = null;
	}

	@Override
	public boolean isList() {

		return getMode().equals(Mode.LIST);
	}

}
