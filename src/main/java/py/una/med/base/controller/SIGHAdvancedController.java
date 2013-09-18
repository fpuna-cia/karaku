package py.una.med.base.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.exception.NotDisplayNameException;
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
public abstract class SIGHAdvancedController<T, K extends Serializable> extends
		SIGHBaseController<T, K> implements ISIGHAdvancedController<T, K> {

	@Autowired
	private ControllerHelper helper;


	@Override
	public abstract ISIGHBaseLogic<T, K> getBaseLogic();

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

	@Override
	public Where<T> getSimpleFilters() {

		Where<T> where = new Where<T>();
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
				throw new NotDisplayNameException();
			}

			if (!"".equals(displayName.path())) {

				String consulta = f.getName() + "." + displayName.path();
				where.addClause(Clauses.iLike(consulta, getFilterValue()));
				return where;
			}
			T example = getBaseEntity();
			f.setAccessible(true);
			if (f.getType().equals(String.class)) {
				f.set(example, getFilterValue());
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			if (Number.class.isAssignableFrom(f.getType())) {
				Method method = f.getType().getMethod("valueOf", String.class);
				Object o = method.invoke(null, getFilterValue());
				f.set(example, o);
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			if (f.getType().equals(Date.class)) {
				try {

					f.set(example, new SimpleDateFormat("dd-MM-yyyy")
							.parse(getFilterValue()));
				} catch (ParseException parseException) {
					helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
							"La fecha debe tener el formato dd-MM-yyyy");
					return null;
				}
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			throw new NotDisplayNameException();
		} catch (Exception e) {
			log.error("Error al obtener los filtros", e);
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
			reloadEntities();
			return postDelete();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				log.warn("doCreate failed", e);
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
			reloadEntities();
			helper.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
					"BASE_ABM_EDIT_SUCCESS");
			return postEdit();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				log.warn("doCreate failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						"BASE_ABM_EDIT_FAILURE", e.getMessage());
			}
			return "";
		}
	}

	/**
	 * Método que se encarga de invocar a la lógica y de actualizar el bean
	 * actual.
	 * <p>
	 * Es un método de conveniencia para reescribir solamente las partes
	 * relevantes a la actualización de la entidad y omitir detalles como la
	 * generación de mensajes y manejo de excepciones.
	 * </p>
	 * <p>
	 * Cualquier excepción lanzada aquí será manejada en
	 * {@link #handleException(Exception)}.
	 * </p>
	 * <p>
	 * Si el caso de uso dicta que se deben modificar mas de una entidad, se
	 * recomienda que en este método se llame a la lógica y que sea ella la
	 * encargada de realizar la operación, retornando el elemento relevante
	 * para mostrar al usuario. Se recomienda el uso de la lógica pues aquí no
	 * se pueden realizar transacciones.
	 * </p>
	 * 
	 * @param entity
	 *            entidad a actualizar
	 * @return T entidad actualizada.
	 */
	protected T edit(final T entity) {

		log.info("Edit llamado");
		return getBaseLogic().update(entity);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Crea el actual bean, este método no lanza ninguna excepción, si desea
	 * capturar la excepción que lanza la creación utilize la función
	 * {@link #handleException(Exception)}, si desea modificar el comportamiento
	 * al crear un objeto, utilice la función {@link #create(Object)}.
	 * </p>
	 * 
	 * @see #create(Object)
	 * @see #handleException(Exception)
	 */
	@Override
	@HasRole(SIGHSecurity.DEFAULT_CREATE)
	public String doCreate() {

		try {
			setBean(create(getBean()));
			reloadEntities();
			helper.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO, "",
					"BASE_ABM_CREATE_SUCCESS");
			return postCreate();
		} catch (Exception e) {
			e = helper.convertException(e, getClazz());
			if (!handleException(e)) {
				log.warn("doCreate failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						"BASE_ABM_CREATE_FAILURE", e.getMessage());
			}
			return "";
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementación por defecto siempre retorna <code>false</code>, para que
	 * {@link #doCreate()} lance un mensaje genérico.
	 * </p>
	 */
	@Override
	public boolean handleException(final Exception e) {

		return false;
	}

	/**
	 * Método que se encarga de invocar a la lógica y de persistir el bean
	 * actual.
	 * <p>
	 * Es un método de conveniencia para reescribir solamente las partes
	 * relevantes a la creación de la entidad y omitir detalles como la
	 * generación de mensajes y manejo de excepciones.
	 * </p>
	 * <p>
	 * Cualquier excepción lanzada aquí será manejada en
	 * {@link #handleException(Exception)}.
	 * </p>
	 * <p>
	 * Si el caso de uso dicta que se deben crear mas de una entidad, se
	 * recomienda que en este método se llame a la lógica y que sea ella la
	 * encargada de guardar la lista, retornando el elemento relevante para
	 * mostrar al usuario. Se recomienda el uso de la lógica pues aquí no se
	 * pueden realizar transacciones.
	 * </p>
	 * 
	 * @param entity
	 *            entidad a crear
	 * @return T entidad creada.
	 */
	protected T create(T entity) {

		log.info("Create llamado");
		return getBaseLogic().add(entity);
	}

}
