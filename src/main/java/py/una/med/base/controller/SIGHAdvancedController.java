package py.una.med.base.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.model.DisplayName;
import py.una.med.base.security.HasRole;
import py.una.med.base.security.SIGHSecurity;
import py.una.med.base.util.ControllerHelper;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.SIGHConverterV2;
import py.una.med.base.util.StringUtils;

/**
 * Controler que sirve de soporte para vistas avanzadas o mas genericas.
 * 
 * @author Arturo Volpe
 * 
 * @param <T>
 *            entidad
 * @param <ID>
 *            clave primaria de la entidad
 * @since 1.1
 * @version 1.4 07/02/2013
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public abstract class SIGHAdvancedController<T, ID extends Serializable>
		extends SIGHBaseController<T, ID> implements
		ISIGHAdvancedController<T, ID> {

	@Autowired
	ControllerHelper helper;

	private final Logger log = LoggerFactory.getLogger(getClass());

	T bean;

	@Override
	public abstract ISIGHBaseLogic<T, ID> getBaseLogic();

	@Override
	public List<String> getBaseSearchItems() {

		List<String> aRet = new ArrayList<String>();
		Class<T> clazz = getClazz();
		for (Field f : clazz.getDeclaredFields()) {
			DisplayName displayName = f.getAnnotation(DisplayName.class);
			if (displayName != null) {
				aRet.add(I18nHelper.getName(displayName));
			}
		}
		return aRet;
	};

	protected Class<T> getClazz() {

		return getBaseLogic().getDao().getClassOfT();
	}

	private Field getField(final String value) {

		for (Field f : getClazz().getDeclaredFields()) {
			DisplayName displayName = f.getAnnotation(DisplayName.class);
			if (displayName == null) {
				continue;
			}
			if (I18nHelper.getName(displayName).equals(value)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * Retorna un Where con todos los filtros aplicados, esto incluye tanto
	 * busquedas avanzadas como busquedas simples
	 */
	@Override
	public Where<T> getFilters() {

		Where<T> where = new Where<T>();
		if (getExample() != null) {
			where.setExample(new EntityExample<T>(getExample()));
			return where;
		}
		if (!StringUtils.isValid(getFilterOption())) {
			return where;
		}
		if (!StringUtils.isValid(getFilterValue())) {
			return where;
		}
		try {
			Field f = getField(getFilterOption());
			DisplayName displayName = f.getAnnotation(DisplayName.class);
			if (displayName == null) {
				throw new RuntimeException(
						"Llamado a filtro generico sin la anotacion DisplayName, agrege la antoacion o reimplemente este metodo y capture este tipo de campos");
			}
			T example = getBaseEntity();
			f.setAccessible(true);
			if (!"".equals(displayName.path())) {

				String consulta = f.getName() + "." + displayName.path();
				where.addClause(Clauses.iLike(consulta, getFilterValue()));
				return where;
			}
			if (f.getType().equals(String.class)) {
				f.set(example, getFilterValue());
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			if (f.getType().isAssignableFrom(Number.class)) {
				f.set(example, Integer.valueOf(getFilterValue()));
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			if (f.getType().equals(Date.class)) {
				try {
					f.set(example, new SimpleDateFormat("dd/MM/yyyy")
							.parse(getFilterValue()));
				} catch (ParseException parseException) {
					createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
							"La fecha debe tener el formato dd/MM/yyyy");
					return null;
				}
				where.setExample(new EntityExample<T>(example));
				return where;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return where;
	}

	/**
	 * Retorna un converter general para ser utilizado por cualquier combo
	 * 
	 * @return Converter universal
	 */
	public SIGHConverterV2 getConverter() {

		return new SIGHConverterV2();
	}

	/**
	 * Retorna una entidad base a ser usada como ejemplo base
	 * 
	 * @return Entidad recien creada
	 */
	public T getBaseEntity() {

		return getBaseLogic().getNewInstance();
	}

	@Override
	public T getBean() {

		if (bean == null) {
			bean = getBaseLogic().getNewInstance();
		}
		return bean;
	}

	@Override
	public void setBean(final T bean) {

		this.bean = bean;
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String preCreate() {

		setMode(Mode.NEW);
		setBean(getBaseEntity());
		return goNew();
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_DELETE)
	public String doDelete() {

		try {
			delete(getBean());
			return postDelete();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						"BASE_ABM_DELETE_FAILURE");
			}
			return "";
		}
	}

	protected void delete(final T entity) {

		log.info("Delete llamado");
		getBaseLogic().remove(entity);
		helper.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
				"BASE_ABM_DELETE_SUCCESS");
	}

	@Override
	@HasRole(SIGHSecurity.DEFAULT_EDIT)
	public String doEdit() {

		try {
			edit(getBean());
			return postEdit();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						"BASE_ABM_EDIT_FAILURE", e.getMessage());
			}
			return "";
		}
	}

	protected T edit(final T entity) {

		return edit(entity, true);
	}

	protected T edit(T entity, final boolean withMessage) {

		log.info("Do edit llamado");
		entity = getBaseLogic().update(entity);
		if (withMessage) {
			helper.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
					"BASE_ABM_EDIT_SUCCESS");
		}
		return entity;
	}

	/**
	 * Crea el actual bean, este metodo no lanza ninguna excepcion, si desea
	 * capturar la excepcion que lanza la creacion utilize la fucnion crear
	 * asegurandose de retornar lo que retorna el metodo {@link #postCreate()}.
	 */
	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doCreate() {

		try {
			setBean(create(getBean()));
			return postCreate();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						"BASE_ABM_CREATE_FAILURE", e.getMessage());
			}
			return "";
		}
	}

	@Override
	public boolean handleException(final Exception e) {

		return false;
	}

	protected T create(T entity) {

		log.info("Create llamado");
		entity = getBaseLogic().add(entity);
		createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
				"BASE_ABM_CREATE_SUCCESS");
		return entity;
	}

}
