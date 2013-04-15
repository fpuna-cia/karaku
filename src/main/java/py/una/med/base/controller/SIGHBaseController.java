package py.una.med.base.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import py.una.med.base.breadcrumb.BreadcrumbController;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.business.reports.SIGHBaseReportSimple;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.reports.Column;
import py.una.med.base.security.HasRole;
import py.una.med.base.security.SIGHSecurity;
import py.una.med.base.util.ControllerHelper;
import py.una.med.base.util.EntitySerializer;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.PagingHelper;
import py.una.med.base.util.SelectHelper;
import py.una.med.base.util.StringUtils;

@Controller
@ManagedBean
@Scope(value = "session")
public abstract class SIGHBaseController<T, ID extends Serializable> implements
		ISIGHBaseController<T, ID> {

	public static enum Mode {
		LIST, VIEW, EDIT, NEW, SEARCH, DELETE
	}

	@Autowired
	BreadcrumbController breadcrumController;

	private static final int ROWS_FOR_PAGE = 10;

	private Boolean sucess = false;

	private Mode mode = Mode.VIEW;

	private String filterValue;

	private String filterOption;

	private T bean;

	private T example;

	private final Logger log = LoggerFactory.getLogger(getClass());

	PagingHelper<T, ID> pagingHelper;

	private String messageIdName;

	@Autowired
	I18nHelper i18nHelper;

	@Override
	public void clearFilters() {

		log.info("Clear Filters called");
		setExample(null);
		setFilterValue("");
	}

	public void createFacesMessage(Severity severity, String code) {

		createFacesMessage(severity, "", code, getMessageIdName());
	}

	public void createFacesMessage(Severity severity, String summary,
			String code, String componentId) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		String mensaje = !"".equals(code) ? getMessage(code) : "";

		FacesMessage msg = new FacesMessage(severity, summary, mensaje);
		facesContext.addMessage(componentId, msg);
	}

	public void createGlobalFacesMessage(Severity severity, String code) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String mensaje = getMessage(code);

		FacesMessage msg = new FacesMessage(severity, "", mensaje);
		facesContext.addMessage(null, msg);
	}

	public void createGlobalFacesMessage(Severity severity, String summary,
			String code) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String mensaje = getMessage(code);
		String sum = !"".equals(summary) ? getMessage(summary) : "";

		FacesMessage msg = new FacesMessage(severity, sum, mensaje);
		facesContext.addMessage(null, msg);
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

			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"BASE_ABM_CREATE_SUCESS");
			postCreate();
			return goList();
		} catch (Exception e) {
			createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
					"BASE_ABM_CREATE_FAILURE", e.getMessage());
			return "";
		}

	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String doDelete() {

		try {
			log.info("Do Delete llamado");
			getBaseLogic().remove(getBean());
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"MESSAGE_DELETE_SUCCESS");
			return goList();
		} catch (Exception e) {
			createGlobalFacesMessage(FacesMessage.SEVERITY_ERROR, "",
					e.getMessage());
			return "";
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String doEdit() {

		try {
			getBaseLogic().update(bean);
			log.info("Do edit llamado");
			createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"BASE_ABM_EDIT_SUCCESS");
			return goList();
		} catch (Exception e) {
			log.error("Error al editar", e);
			createGlobalFacesMessage(FacesMessage.SEVERITY_ERROR, "",
					"BASE_ABM_EDIT_FAILURE");
			return "";
		}
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT)
	public void doSearch() {

		helper.updateModel("pgSearch");

		log.info("do search llamado");
		setExample(getBean());
	}

	// Para que las columnas ocupen el ancho de la pagina
	@HasRole(SIGHSecurity.DEFAULT)
	public void doSimpleSearch() {

		setExample(null);
		log.info("doSimpleSearch llamado");
	}

	/**
	 * Finds component with the given id
	 */
	private UIComponent findComponent(UIComponent c, String id) {

		if (id.equals(c.getId())) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public List<T> getAllEntities(ISearchParam searchParam) {

		return getBaseLogic().getAll(searchParam);
	}

	@Override
	public abstract ISIGHBaseLogic<T, ID> getBaseLogic();

	@Override
	public abstract List<String> getBaseSearchItems();

	@Override
	public T getBean() {

		if (bean == null) {
			bean = getBaseLogic().getNewInstance();
		}
		return bean;
	}

	private List<T> getByExample(T example2, ISearchParam sp) {

		return getBaseLogic().getAllByExample(new EntityExample<T>(example2),
				sp);
	}

	public List<T> getBySimpleFilter(ISearchParam sp) {

		return getBaseLogic().getAll(getFilters(), sp);
	}

	@Override
	public Where<T> getWhereReport() {

		if (getExample() != null) {
			Where<T> where = new Where<T>();
			where.setExample(getExample());
			return where;
		}
		if (getFilterValue() != null && !getFilterValue().equals("")) {
			Where<T> where = getFilters();
			return where;
		}
		return null;
	}

	@Override
	public HashMap<String, Object> getParamsFilter(
			HashMap<String, Object> paramsReport) {

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

	@Autowired
	SIGHBaseReportSimple baseReportSimple;

	@Override
	public void generateReport(String type) {

		HashMap<String, Object> paramsReport = new HashMap<String, Object>();
		paramsReport.put("titleReport", getHeaderReport());
		Class<T> clazz = getBaseLogic().getDao().getClassOfT();
		baseReportSimple.generateReport(getParamsFilter(paramsReport), type,
				getColumns(), getBaseLogic(), getWhereReport(), clazz);
	}

	@Override
	public void generateReportDetail(String type) {

	}

	@Autowired
	ControllerHelper helper;

	@Override
	public LinkedList<Column> getColumns() {

		return helper.getColumns();
	}

	@Override
	public String getHeaderReport() {

		Class<T> clazz = getBaseLogic().getDao().getClassOfT();
		List<String> listName = StringUtils.split(clazz.getSimpleName());
		String nameList = StringUtils.pluralize(listName);
		return i18nHelper.getString("BASE_REPORT_NAME") + " " + nameList;
	}

	/**
	 * Returns the clientId for a component with id
	 */
	public String getClientId(String id) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		UIComponent c = findComponent(root, id);
		return c.getClientId(context);
	}

	@Override
	public List<T> getEntities() {

		log.info("Get Entidades llamado");

		if (pagingHelper == null) {
			pagingHelper = new PagingHelper<T, ID>(getRowsForPage());
		}

		pagingHelper.calculate(getBaseLogic(), null);
		ISearchParam sp = pagingHelper.getISearchparam();
		if (getExample() != null) {
			return getByExample(getExample(), sp);
		}
		if (StringUtils.isValid(getFilterOption())
				|| StringUtils.isValid(getFilterValue())) {
			if (!StringUtils.isValid(getFilterOption())
					|| !StringUtils.isValid(getFilterValue())) {
				createGlobalFacesMessage(FacesMessage.SEVERITY_WARN, "",
						"BASE_ABM_SEARCH_VALUE_OPTION_REQUIRED");
			} else {
				return getBySimpleFilter(sp);
			}
		}
		return getAllEntities(sp);
	}

	public T getExample() {

		return example;
	}

	public String getFilterOption() {

		return filterOption;
	}

	@Override
	public abstract Where<T> getFilters();

	public String getFilterValue() {

		return filterValue;
	}

	public String getMessage(String code) {

		return i18nHelper.getString(code);
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

	@Override
	public PagingHelper<T, ID> getPagingHelper() {

		return pagingHelper;
	}

	public int getRowsForPage() {

		return ROWS_FOR_PAGE;
	}

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto.
	 */
	@Override
	public List<SelectItem> getSearchSelectItemsList() {

		return SelectHelper.getSelectItems(getBaseSearchItems());

	}

	public Boolean getSucess() {

		if (sucess) {
			sucess = !sucess;
			return true;
		} else {
			return false;
		}
	}

	@Override
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

		log.info("Verificando visibilidad de: " + campo);
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

		log.info("postCreate llamado");
		return goList();
	}

	@Override
	public String postDelete() {

		log.info("Post Delete llamado");
		return goList();
	}

	@Override
	public String postEdit() {

		mode = Mode.LIST;
		log.info("post edit llamado");
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

	public void setExample(T example) {

		this.example = example;
	}

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

	public void setSucess(Boolean sucess) {

		this.sucess = sucess;
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

}
