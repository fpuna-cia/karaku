package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.audit.Audit;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.PagingHelper;
import py.una.med.base.util.SelectHelper;

@Controller
@ManagedBean
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Deprecated
public abstract class BaseController<T, K extends Serializable> implements
		IBaseController<T, K> {

	private static final int ROWS_FOR_PAGE = 10;

	public enum Mode {
		LIST, VIEW, EDIT, NEW, SEARCH, DELETE
	}

	private Boolean sucess = false;

	private Mode mode = Mode.VIEW;

	private String filterValue;

	private String filterOption;

	private T bean;

	@Autowired
	private I18nHelper i18nHelper;

	private T example;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private PagingHelper<T, K> pagingHelper;

	private String messageIdName;

	@Override
	public void preEdit() {

		mode = Mode.EDIT;
		log.info("Pre edit llamado");
	}

	public List<T> getAllEntities(ISearchParam searchParam) {

		return getBaseLogic().getAll(searchParam);
	}

	@Override
	public void doEdit() {

		getBaseLogic().update(bean);
		log.info("Do edit llamado");
	}

	@Override
	public void postEdit() {

		mode = Mode.LIST;
		log.info("post edit llamado");
	}

	@Override
	public void preDelete() {

		log.info("Pre Delete llamado");
		this.mode = Mode.DELETE;
	}

	@Override
	public void doDelete() {

		log.info("Do Delete llamado");
		getBaseLogic().remove(getBean());
		createFacesMessage(FacesMessage.SEVERITY_INFO, "MESSAGE_DELETE_SUCCESS");

	}

	@Override
	public void postDelete() {

		log.info("Post Delete llamado");
	}

	@Override
	public void preSearch() {

		log.info("pre search llamado");
		this.mode = Mode.SEARCH;
		setBean(getBaseLogic().getNewInstance());
	}

	@Audit(toAudit = { "filterOption", "filterValue" })
	public void doSimpleSearch() {

		setExample(null);
		log.info("doSimpleSearch llamado");
	}

	@Override
	@Audit(toAudit = { "filterOption", "filterValue" })
	public void doSearch() {

		FacesContext.getCurrentInstance().renderResponse();
		log.info("do search llamado");
		setExample(getBean());
	}

	@Override
	public void postSearch() {

		log.info("post search llamado");
		this.mode = Mode.VIEW;
		// setExample(null);

	}

	public int getRowsForPage() {

		return ROWS_FOR_PAGE;
	}

	@Override
	public List<T> getEntities() {

		log.info("Get Entidades llamado");

		if (pagingHelper == null) {
			pagingHelper = new PagingHelper<T, K>(getRowsForPage());
		}

		Long totalCount = getBaseLogic().getCount();
		pagingHelper.udpateCount(totalCount);
		ISearchParam sp = pagingHelper.getISearchparam();
		if (getExample() != null) {
			return getByExample(getExample(), sp);
		}
		if ((filterValue != null) && !filterValue.equals("")) {
			return getBySimpleFilter(getFilterValue(), sp);
		}
		return getAllEntities(sp);
	}

	public PagingHelper<T, K> getPagingHelper() {

		return pagingHelper;
	}

	@Audit(toAudit = { "hola.nombre", "chau.apellido" }, paramsToAudit = { "{0}.id" })
	private List<T> getByExample(T example2, ISearchParam sp) {

		return getBaseLogic().getAllByExample(new EntityExample<T>(example2),
				sp);
	}

	public abstract List<T> getBySimpleFilter(String filter, ISearchParam sp);

	@Override
	public String doCancel() {

		log.info("DoCancel llamado");
		return "";
	}

	@Override
	public abstract List<String> getBaseSearchItems();

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto.
	 */
	@Override
	public List<SelectItem> getSearchSelectItemsList() {

		return SelectHelper.getSelectItems(getBaseSearchItems());

	}

	@Override
	public String getFilterValue() {

		return filterValue;
	}

	@Override
	public void setFilterValue(String filterValue) {

		this.filterValue = filterValue;
	}

	@Override
	public T getBean() {

		if (bean == null) {
			bean = getBaseLogic().getNewInstance();
		}
		return bean;
	}

	@Override
	public void setBean(T bean) {

		this.bean = bean;
	}

	public boolean isEditable(String campo) {

		log.info("Verificando visibilidad de: " + campo);
		if (mode == null) {
			mode = Mode.VIEW;
		}
		switch (getMode()) {
			case LIST:
				return false;
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
	public void postCreate() {

		log.info("postCreate llamado");
	}

	@Override
	public void doCreate() {

		log.info("doCreate llamado");
		getBaseLogic().add(bean);

		createFacesMessage(FacesMessage.SEVERITY_INFO, "MESSAGE_SAVE_SUCESS");
		postCreate();
	};

	@Override
	public void preCreate() {

		mode = Mode.NEW;
		setBean(getBaseLogic().getNewInstance());
		log.info("preCreate llamado");
	}

	public String getFilterOption() {

		return filterOption;
	}

	public void setFilterOption(String filterOption) {

		this.filterOption = filterOption;
	}

	public T getExample() {

		return example;
	}

	public void setExample(T example) {

		this.example = example;
	}

	public void createFacesMessage(Severity severity, String code) {

		createFacesMessage(severity, "", code);
	}

	public void createFacesMessage(Severity severity, String summary,
			String code) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String mensaje = getMessage(code);

		FacesMessage msg = new FacesMessage(severity, summary, mensaje);
		facesContext.addMessage(getMessageIdName(), msg);
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
	}

	@Override
	public boolean isCreate() {

		return mode == Mode.NEW;
	}

	@Override
	public boolean isEdit() {

		return mode == Mode.EDIT;
	}

	@Override
	public boolean isView() {

		return mode == Mode.VIEW;
	}

	@Override
	public void preView() {

		mode = Mode.VIEW;
		log.info("pre View llamado");
	};

	public void clearFilters() {

		log.info("Clear Filters called");
		setExample(null);
		setFilterValue("");
	}

	@Override
	public boolean isSearch() {

		return mode == Mode.SEARCH;
	}

	@Override
	public abstract ISIGHBaseLogic<T, K> getBaseLogic();

	public Boolean getSucess() {

		if (sucess) {
			sucess = !sucess;
			return true;
		} else {
			return false;
		}
	}

	public void setSucess(Boolean sucess) {

		this.sucess = sucess;
	}

	public Mode getMode() {

		return mode;
	}

	public void setMode(Mode mode) {

		this.mode = mode;
	}

}
