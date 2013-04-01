/*
 * @SIGHListHelper.java 1.0 Feb 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.model.DisplayName;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 25, 2013
 * 
 */
public class SIGHListHelper<T, ID extends Serializable> {

	SimpleFilter simpleFilter;
	PagingHelper<T, ID> helper;
	ISIGHBaseLogic<T, ID> logic;
	EntityExample<T> example;
	private Where<T> baseWhere;
	private List<SelectItem> filterOptions;
	Class<T> clazz;

	public SIGHListHelper(Class<T> clazz, ISIGHBaseLogic<T, ID> logic) {

		this(clazz, new SimpleFilter(), logic);
	}

	public SIGHListHelper(Class<T> clazz, SimpleFilter simpleFilter,
			ISIGHBaseLogic<T, ID> logic) {

		this.clazz = clazz;
		this.simpleFilter = simpleFilter;
		this.logic = logic;
		helper = new PagingHelper<T, ID>(15);
	}

	/**
	 * @return baseWhere
	 */
	public Where<T> getBaseWhere() {

		return baseWhere;
	}

	/**
	 * @param baseWhere
	 *            baseWhere para setear
	 */
	public void setBaseWhere(Where<T> baseWhere) {

		this.baseWhere = baseWhere;
	}

	public List<T> getEntities() {

		Where<T> where = getFilters(clazz, example, simpleFilter);
		helper.calculate(logic, where);
		ISearchParam isp = helper.getISearchparam();
		return logic.getAll(where, isp);
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
		if (entityExample != null) {
			where.setExample(entityExample);
			return where;
		}
		if (!StringUtils.isValid(simpleFilter.getOption())) {
			return where;
		}
		if (!StringUtils.isValid(simpleFilter.getValue())) {
			return where;
		}
		try {
			Field f = getField(simpleFilter.getOption(), clazz);
			T example = clazz.newInstance();
			DisplayName displayName = f.getAnnotation(DisplayName.class);
			if (displayName == null) {
				throw new RuntimeException(
						"Llamado a filtro generico sin la anotacion DisplayName, "
								+ "agrege la antotacion o reimplemente este metodo "
								+ "y capture este tipo de campos");
			}
			f.setAccessible(true);
			if (!"".equals(displayName.path())) {

				String consulta = f.getName() + "." + displayName.path();
				where.addClause(Clauses.iLike(consulta, simpleFilter.getValue()));
				return where;
			}
			if (f.getType().equals(String.class)) {
				f.set(example, simpleFilter.getValue());
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			if (f.getType().isAssignableFrom(Number.class)) {
				f.set(example, Integer.valueOf(simpleFilter.getValue()));
				where.setExample(new EntityExample<T>(example));
				return where;
			}
			if (f.getType().equals(Date.class)) {
				try {
					f.set(example, new SimpleDateFormat("dd/MM/yyyy")
							.parse(simpleFilter.getValue()));
				} catch (ParseException parseException) {
					parseException.printStackTrace();
				}
				where.setExample(new EntityExample<T>(example));
				return where;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return where;
	}

	private static Field getField(String value, Class<?> clazz) {

		for (Field f : clazz.getDeclaredFields()) {
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
	 * @return simpleFilter
	 */
	public SimpleFilter getSimpleFilter() {

		return simpleFilter;
	}

	/**
	 * @return filterOptions
	 */
	public List<SelectItem> getFilterOptions() {

		if (filterOptions == null) {

			List<String> aRet = new ArrayList<String>();
			for (Field f : clazz.getDeclaredFields()) {
				DisplayName displayName = f.getAnnotation(DisplayName.class);
				if (displayName != null) {
					aRet.add(I18nHelper.getName(displayName));
				}
			}
			filterOptions = SelectHelper.getSelectItems(aRet);
		}
		return filterOptions;
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
}
