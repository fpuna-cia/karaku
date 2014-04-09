/*
 * @SIGHListHelper.java 1.0 Feb 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import static py.una.med.base.util.Checker.notNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.search.SearchHelper;
import py.una.med.base.dao.select.Select;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.model.DisplayName;
import py.una.med.base.util.PagingHelper.ChangeListener;
import py.una.med.base.util.SimpleFilter.ChangeListenerSimpleFilter;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 25, 2013
 * 
 */
public class SIGHListHelper<T, K extends Serializable> implements
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
	private ISIGHBaseLogic<T, K> logic;
	private EntityExample<T> example;
	private Where<T> baseWhere;
	private List<SelectItem> filterOptions;
	private Class<T> clazz;
	private static final Logger LOG = LoggerFactory
			.getLogger(SIGHListHelper.class);
	private String[] columnsList;
	private List<T> entities;

	public SIGHListHelper(Class<T> clazz, ISIGHBaseLogic<T, K> logic) {

		this(clazz, null, logic);
	}

	public SIGHListHelper(Class<T> clazz, SimpleFilter simpleFilter,
			ISIGHBaseLogic<T, K> logic) {

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
		Where<T> where = getFilters(this.clazz, this.example, simpleFilter);
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
	public Where<T> getFilters(Class<T> clazz, EntityExample<T> entityExample,
			SimpleFilter simpleFilter) {

		Where<T> where = getBaseWhere();

		if (where == null) {
			where = new Where<T>();
		}
		if (this.example != null) {
			where.setExample(entityExample);
			return where;
		}
		if (!StringUtils.isValid(simpleFilter.getOption(),
				simpleFilter.getValue())) {
			return where;
		}
		SearchHelper sh = Util.getSpringBeanByJSFContext(null,
				SearchHelper.class);
		String path = this.getField(clazz, simpleFilter.getOption());

		where.addClause(sh.getClause(clazz, path, simpleFilter.getValue()));
		return where;
	}

	private String getField(final Class<?> clazz, final String value) {

		for (Field f : clazz.getDeclaredFields()) {
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
