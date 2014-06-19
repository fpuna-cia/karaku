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
package py.una.pol.karaku.util;

import static py.una.pol.karaku.util.Checker.notNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.SearchHelper;
import py.una.pol.karaku.dao.select.Select;
import py.una.pol.karaku.dao.util.EntityExample;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.model.DisplayName;
import py.una.pol.karaku.util.PagingHelper.ChangeListener;
import py.una.pol.karaku.util.SimpleFilter.ChangeListenerSimpleFilter;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 25, 2013
 * 
 */
public class KarakuListHelper<T, K extends Serializable> implements
		KarakuListHelperProvider<T> {

	/**
	 * 
	 */
	private static final String COLUMN_NOT_FOUND_IN_ENTITY_MESSAGE = "Column: {} not found.";
	/**
	 * Vector que contiene las columnas por las que se intentará ordenar.
	 */
	private static final String[] DEFAULT_SORT_COLUMNS = { "descripcion", "id" };
	/**
	 *
	 */
	private static final int ROWS_FOR_PAGE = 5;
	private SimpleFilter simpleFilter;
	private PagingHelper helper;
	private IKarakuBaseLogic<T, K> logic;
	private EntityExample<T> example;
	private Where<T> baseWhere;
	private List<SelectItem> filterOptions;
	private Class<T> clazz;
	private static final Logger LOG = LoggerFactory
			.getLogger(KarakuListHelper.class);
	private String[] columnsList;
	private List<T> entities;

	public KarakuListHelper(Class<T> clazz, IKarakuBaseLogic<T, K> logic) {

		this(clazz, null, logic);
	}

	public KarakuListHelper(Class<T> clazz, SimpleFilter simpleFilter,
			IKarakuBaseLogic<T, K> logic) {

		this.clazz = clazz;
		this.simpleFilter = simpleFilter;
		this.logic = logic;
	}

	/**
	 * @return baseWhere
	 */
	public Where<T> getBaseWhere() {

		return this.baseWhere;
	}

	/**
	 * @param baseWhere
	 *            baseWhere para setear
	 */
	@Override
	public void setBaseWhere(Where<T> baseWhere) {

		this.baseWhere = baseWhere;
	}

	@Override
	public List<T> getEntities() {

		if (this.entities == null) {
			loadEntities();
		}
		return this.entities;

	}

	protected void loadEntities() {

		LOG.debug("Get entities of {} ", this.getClass());
		Where<T> where = getFilters(this.clazz, this.example, getSimpleFilter());
		Long totalSize = this.logic.getCount(where);
		getHelper().udpateCount(totalSize);
		ISearchParam isp = this.helper.getISearchparam();
		configureSearchParams(isp);

		this.entities = buildQuery(where, isp);
	}

	public void setEntities(List<T> entities) {

		this.entities = entities;
	}

	@Override
	public void reloadEntities() {

		LOG.debug("Reset entities", this.getClass());
		this.entities = null;
	}

	protected List<T> buildQuery(Where<T> where, ISearchParam isp) {

		return loadDataFromDataBase(where, isp, buildSelect());

	}

	protected List<T> loadDataFromDataBase(Where<T> where, ISearchParam isp,
			Select select) {

		if (select != null) {
			return this.logic.get(select, where, isp);
		} else {
			return this.logic.getAll(where, isp);
		}
	}

	/**
	 * Construye el select en el caso de que se haya definido las columnas
	 * especificas que se desean traer de la base de datos. Si no se definieron
	 * las columnas, retorna null de manera a considerar todas las columnas de
	 * la entidad.
	 * 
	 * @return
	 */
	public Select buildSelect() {

		if (getColumnsList() == null) {
			return null;
		}
		return Select.build(getColumnsList());
	}

	public void setColumnsList(String ... attributes) {

		columnsList = attributes;
	}

	private String[] getColumnsList() {

		return columnsList;
	}

	/**
	 * @param example
	 *            example para setear
	 */
	public void setExample(EntityExample<T> example) {

		this.example = example;
	}

	/**
	 * Retorna un Where con todos los filtros aplicados, esto incluye tanto
	 * busquedas avanzadas como busquedas simples
	 */
	public Where<T> getFilters(Class<T> claz2, EntityExample<T> entityExample,
			SimpleFilter sf) {

		Where<T> base = getBaseWhere();
		Where<T> where;

		if (base == null) {
			where = new Where<T>();
		} else {
			where = base.getClone();
		}

		if (this.example != null) {
			where.setExample(entityExample);
			return where;
		}

		if (!StringUtils.isValid(sf.getOption(), sf.getValue())) {
			return where;
		}

		SearchHelper sh = Util.getSpringBeanByJSFContext(null,
				SearchHelper.class);
		String path = this.getField(claz2, sf.getOption());
		where.addClause(sh.getClause(claz2, path, sf.getValue()));

		return where;
	}

	private String getField(final Class<?> claz2, final String value) {

		for (Field f : claz2.getDeclaredFields()) {
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

	@Override
	public SimpleFilter getSimpleFilter() {

		if (simpleFilter == null) {
			simpleFilter = new SimpleFilter();

			simpleFilter.setChangeListener(new ChangeListenerSimpleFilter() {

				@Override
				public void onChange(SimpleFilter thizz, String value,
						String option) {

					reloadEntities();
				}

			});

		}

		return this.simpleFilter;
	}

	@Override
	public List<SelectItem> getFilterOptions() {

		if (this.filterOptions == null) {

			List<String> aRet = new ArrayList<String>();
			for (Field f : this.clazz.getDeclaredFields()) {
				DisplayName displayName = f.getAnnotation(DisplayName.class);
				if (displayName != null) {
					aRet.add(I18nHelper.getName(displayName));
				}
			}
			this.filterOptions = SelectHelper.getSelectItems(aRet);
		}
		return this.filterOptions;
	}

	@Override
	public PagingHelper getHelper() {

		if (this.helper == null) {
			this.helper = new PagingHelper(ROWS_FOR_PAGE);
			this.helper.setChangeListener(new ChangeListener() {

				@Override
				public void onChange(PagingHelper thizz, int previousPage,
						int currentPage) {

					reloadEntities();
				}
			});
		}

		return this.helper;
	}

	public void setHelper(PagingHelper helper) {

		this.helper = helper;
	}

	/**
	 * @param filterOptions
	 *            filterOptions para setear
	 */
	public void setFilterOptions(List<SelectItem> filterOptions) {

		this.filterOptions = filterOptions;
	}

	/**
	 * Limpia los filtros y lo reinicializa
	 */
	public void clearFilters() {

		this.getSimpleFilter().clear();
		this.setExample(null);
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
	public void configureSearchParams(ISearchParam sp) {

		for (String s : DEFAULT_SORT_COLUMNS) {
			try {
				this.logic.getDao().getClassOfT().getDeclaredField(s);
				sp.addOrder(notNull(s), true);
				return;
			} catch (SecurityException e) {
				LOG.trace(COLUMN_NOT_FOUND_IN_ENTITY_MESSAGE, s, e);
			} catch (NoSuchFieldException e) {
				LOG.trace(COLUMN_NOT_FOUND_IN_ENTITY_MESSAGE, s, e);
			}
		}
		throw new KarakuRuntimeException(
				"Tabla '"
						+ this.logic.getDao().getTableName()
						+ "' no posee las columnas por defecto para ordenar "
						+ Arrays.toString(DEFAULT_SORT_COLUMNS)
						+ " por favor, reescriba el método configureSearchparam en el picker: "
						+ this.getClass().getSimpleName());

	}
}
