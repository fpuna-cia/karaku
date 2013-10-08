/*
 * @SIGHListHelper.java 1.0 Feb 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.search.SearchHelper;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.model.DisplayName;

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

	public SIGHListHelper(Class<T> clazz, ISIGHBaseLogic<T, K> logic) {

		this(clazz, new SimpleFilter(), logic);
	}

	public SIGHListHelper(Class<T> clazz, SimpleFilter simpleFilter,
			ISIGHBaseLogic<T, K> logic) {

		this.clazz = clazz;
		this.simpleFilter = simpleFilter;
		this.logic = logic;
		this.helper = new PagingHelper(ROWS_FOR_PAGE);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.util.KarakuListHelperProvider#getEntities()
	 */
	@Override
	public List<T> getEntities() {

		LOG.debug("Get entities of {} ", this.getClass());
		Where<T> where = this.getFilters(this.clazz, this.example,
				this.simpleFilter);
		Long totalSize = this.logic.getCount(where);
		this.helper.udpateCount(totalSize);
		ISearchParam isp = this.helper.getISearchparam();
		this.configureSearchParams(isp);
		return this.logic.getAll(where, isp);
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

		Where<T> where = this.getBaseWhere();

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
	 * Permite extender los parametros de busquedas utilizados
	 **/
	public void configureSearchParams(ISearchParam isp) {

		return;
	}
}
