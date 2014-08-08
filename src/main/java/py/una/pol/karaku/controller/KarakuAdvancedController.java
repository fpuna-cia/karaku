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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.SearchHelper;
import py.una.pol.karaku.exception.UniqueConstraintException;
import py.una.pol.karaku.model.DisplayName;
import py.una.pol.karaku.security.HasRole;
import py.una.pol.karaku.security.KarakuSecurity;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.KarakuConverter;
import py.una.pol.karaku.util.StringUtils;

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
public abstract class KarakuAdvancedController<T, K extends Serializable>
		extends KarakuBaseController<T, K> implements
		IKarakuAdvancedController<T, K> {

	private static final String DELETE_FAILURE = "BASE_ABM_DELETE_FAILURE";
	private static final String EDIT_FAILURE = "BASE_ABM_EDIT_FAILURE";
	private static final String CREATE_FAILURE = "BASE_ABM_CREATE_FAILURE";
	private static final String CREATE_SUCCESS = "BASE_ABM_CREATE_SUCCESS";
	private static final String EDIT_SUCCESS = "BASE_ABM_EDIT_SUCCESS";
	private static final String DELETE_SUCCESS = "BASE_ABM_DELETE_SUCCESS";

	@Autowired
	private ControllerHelper helper;

	@Autowired
	private SearchHelper searchHelper;

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
	}

	/**
	 * Retorna la {@link Class} a la que este controller representa.
	 * 
	 * @return {@link Class}
	 */
	protected Class<T> getClazz() {

		return getBaseLogic().getDao().getClassOfT();
	}

	private String getField(final String value) {

		for (Field f : getClazz().getDeclaredFields()) {
			DisplayName displayName = f.getAnnotation(DisplayName.class);
			if (displayName == null) {
				continue;
			}
			if (I18nHelper.getName(displayName).equals(value)) {
				if ("".equals(displayName.path().trim())) {
					return f.getName();
				} else {
					return f.getName() + "." + displayName.path();
				}
			}
		}
		return null;
	}

	/**
	 * Delega la responsabilidad da {@link SearchHelper} para realizar búsquedas
	 * genericas.
	 * 
	 * @return {@link Where} configurado, utilizar este {@link Where} como punto
	 *         de partida.
	 */
	@Override
	public Where<T> getSimpleFilters() {

		Where<T> where = new Where<T>();
		if (!StringUtils.isValid(getFilterOption(), getFilterValue())) {
			return where;
		}

		return searchHelper.buildWhere(getClazz(), getField(getFilterOption()),
				getFilterValue());
	}

	/**
	 * Retorna un converter general para ser utilizado por cualquier combo
	 * 
	 * @return Converter universal
	 */
	public KarakuConverter getConverter() {

		return KarakuConverter.getInstance();
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
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String preCreate() {

		setMode(Mode.NEW);
		setBean(getBaseEntity());
		return goNew();
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_DELETE)
	public String doDelete() {

		try {
			delete(getBean());
			reloadEntities();
			helper.createGlobalFacesMessageSimple(FacesMessage.SEVERITY_INFO,
					getMessageDeleteSuccess());
			return postDelete();
		} catch (Exception e) {
			if (!handleException(helper.convertException(e, getClazz()))) {
				log.warn("doCreate failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						getMessageDeleteFailure(), e.getMessage());
			}
			return "";
		}
	}

	/**
	 * Método que se encarga de invocar a la lógica y de eliminar el bean
	 * actual.
	 * <p>
	 * Es un método de conveniencia para reescribir solamente las partes
	 * relevantes a la eliminacióñ de la entidad y omitir detalles como la
	 * generación de mensajes y manejo de excepciones.
	 * </p>
	 * <p>
	 * Cualquier excepción lanzada aquí será manejada en
	 * {@link #handleException(Exception)}.
	 * </p>
	 * <p>
	 * Si el caso de uso dicta que se deben eliminar mas de una entidad, se
	 * recomienda que en este método se llame a la lógica y que sea ella la
	 * encargada de realizar la operación, retornando el elemento relevante para
	 * mostrar al usuario. Se recomienda el uso de la lógica pues aquí no se
	 * pueden realizar transacciones.
	 * </p>
	 * 
	 * @param entity
	 *            entidad a actualizar
	 * @return T entidad actualizada.
	 */
	protected void delete(final T entity) {

		log.info("Delete llamado");
		getBaseLogic().remove(entity);
	}

	@Override
	@HasRole(KarakuSecurity.DEFAULT_EDIT)
	public String doEdit() {

		try {
			edit(getBean());
			reloadEntities();
			helper.createGlobalFacesMessageSimple(FacesMessage.SEVERITY_INFO,
					getMessageEditSuccess());
			return postEdit();
		} catch (Exception e) {
			if (!handleException(helper.convertException(e, getClazz()))) {
				log.warn("doEdit failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						getMessageEditFailure(), e.getMessage());
			}
			return "";
		}
	}

	/***
	 * Retorna el mensaje a ser visualizado cuando un registro es creado con
	 * éxito.
	 * 
	 * @return Mensaje internacionalizado.
	 */
	protected String getMessageCreateSuccess() {

		return getMessage(CREATE_SUCCESS);

	}

	/***
	 * Retorna el mensaje a ser visualizado cuando un registro no es creado con
	 * éxito.
	 * 
	 * @return Mensaje internacionalizado.
	 */
	protected String getMessageCreateFailure() {

		return getMessage(CREATE_FAILURE);

	}

	/***
	 * Retorna el mensaje a ser visualizado cuando un registro es editado con
	 * éxito.
	 * 
	 * @return Mensaje internacionalizado.
	 */
	protected String getMessageEditSuccess() {

		return getMessage(EDIT_SUCCESS);

	}

	/***
	 * Retorna el mensaje a ser visualizado cuando un registro no es editado con
	 * éxito.
	 * 
	 * @return Mensaje internacionalizado.
	 */
	protected String getMessageEditFailure() {

		return getMessage(EDIT_FAILURE);

	}

	/***
	 * Retorna el mensaje a ser visualizado cuando un registro es eliminado con
	 * éxito.
	 * 
	 * @return Mensaje internacionalizado.
	 */
	protected String getMessageDeleteSuccess() {

		return getMessage(DELETE_SUCCESS);

	}

	/***
	 * Retorna el mensaje a ser visualizado cuando un registro no es eliminado
	 * con éxito.
	 * 
	 * @return Mensaje internacionalizado.
	 */
	protected String getMessageDeleteFailure() {

		return getMessage(DELETE_FAILURE);

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
	 * encargada de realizar la operación, retornando el elemento relevante para
	 * mostrar al usuario. Se recomienda el uso de la lógica pues aquí no se
	 * pueden realizar transacciones.
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
	@HasRole(KarakuSecurity.DEFAULT_CREATE)
	public String doCreate() {

		try {
			setBean(create(getBean()));
			reloadEntities();
			helper.createGlobalFacesMessageSimple(FacesMessage.SEVERITY_INFO,
					getMessageCreateSuccess());
			return postCreate();
		} catch (Exception e) {
			if (!handleException(helper.convertException(e, getClazz()))) {
				log.warn("doCreate failed", e);
				helper.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
						getMessageCreateFailure(), e.getMessage());
			}
			return "";
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Esta implementación captura las excepciones del tipo
	 * {@link UniqueConstraintException} y las excepciones del tipo
	 * {@link DataIntegrityViolationException} y intenta generar un mensaje de
	 * error pertinente.
	 * </p>
	 * 
	 */
	@Override
	public boolean handleException(final Exception e) {

		if (e instanceof UniqueConstraintException) {
			UniqueConstraintException unique = (UniqueConstraintException) e;
			if (unique.getFields().size() == 1) {
				String field = unique.getFields().get(0);
				controllerHelper.addWarnMessage(field,
						getMessage("FIELD_DUPLICATE"),
						getMessage("FIELD_DUPLICATE_DETAIL"));
			}
			if (unique.getFields().size() > 1) {
				controllerHelper.addGlobalWarnMessage(
						getMessage("FIELDS_DUPLICATED"),
						getMessage("FIELDS_DUPLICATED_DETAIL", unique
								.getFields().toArray()));
			}
			return true;

		} else if (e instanceof DataIntegrityViolationException) {

			// TODO: buscar una solucion mas generica para este caso, de
			// identificar excepciones causadas por constraints de foreign keys

			DataIntegrityViolationException integrityException = (DataIntegrityViolationException) e;

			/* controlar que su causa sea del tipo constraintException */
			if (integrityException.getCause() instanceof ConstraintViolationException) {

				/* controlar que el constraint sea de fk */
				ConstraintViolationException constraintException = (ConstraintViolationException) integrityException
						.getCause();

				if (constraintException.getConstraintName().startsWith("fk")) {
					controllerHelper.createGlobalFacesMessage(
							FacesMessage.SEVERITY_WARN,
							"ENTITY_REQUIRED_DETAIL");
					return true;
				}
			}
		}
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
