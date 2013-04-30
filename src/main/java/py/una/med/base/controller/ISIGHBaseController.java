/**
 * @ISIGHBaseController 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.controller.SIGHBaseController.Mode;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.reports.Column;
import py.una.med.base.util.PagingHelper;

/**
 * Interface que define el controlador basico
 * 
 * @author Arturo Volpe, Nathalia Ochoa
 * 
 * @param Entidad
 * @param Clave
 *            primaria
 * @since 1.0
 * @version 2.0 22/01/2013
 */
public interface ISIGHBaseController<T, ID extends Serializable> {

	public void clearFilters();

	public String doCancel();

	public String doCreate();

	public String doDelete();

	public String doEdit();

	public void doSearch();

	public ISIGHBaseLogic<T, ID> getBaseLogic();

	public abstract List<String> getBaseSearchItems();

	public T getBean();

	public List<T> getEntities();

	public Where<T> getFilters();

	public String getMessageIdName();

	public Mode getMode();

	public PagingHelper<T, ID> getPagingHelper();

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto.
	 */
	public List<SelectItem> getSearchSelectItemsList();

	public String getUsarController();

	public String goDelete();

	public String goEdit();

	public String goList();

	public String goNew();

	public String goView();

	public boolean isCreate();

	public boolean isDelete();

	public boolean isEdit();

	public boolean isSearch();

	public boolean isView();

	public String postCreate();

	public String postDelete();

	public String postEdit();

	public void postSearch();

	public String preCreate();

	public String preDelete();

	/**
	 * Metodo que sera invocado antes de editar un elemento, este metodo debe
	 * suponer que {@link ISIGHBaseController#getBean()} retornara la entidad a
	 * editar
	 * 
	 * @return cadena que representa la pagina donde se editara el bean
	 */
	public String preEdit();

	/**
	 * Metodo que sera invocado antes de mostrar la lista de elementos
	 * 
	 * @return cadena que representa la pagina donde se mostrara la lista
	 */
	public String preList();

	public void preSearch();

	public String preView();

	public void setBean(T bean);

	public void setFilterValue(String filterValue);

	/**
	 * Metodo que sera invocado cuando se desea generar algun reporte, el mismo
	 * recoje los parametros de filtros ingresados y retorna el where
	 * correspondiente. Dicho where sera utilizado posteriormente para realizar
	 * la consulta a la base de datos.<br>
	 * 
	 * @return Entidad que representa los filtros ingresados
	 */
	public Where<T> getWhereReport();

	/**
	 * Metodo que sera invocado cuando se desea generar algun reporte, el mismo
	 * recoje los parametros de filtros ingresados que seran desplegados en el
	 * reporte.<br>
	 * 
	 * @return String que representa los criterios de seleccion
	 */
	public HashMap<String, Object> getParamsFilter(
			HashMap<String, Object> paramsReport);

	/**
	 * /** Metodo que sera invocado cuando se desea generar algun reporte
	 * correspondiente a la grilla, el mismo invoca al servicio que generara el
	 * reporte fisicamente.<br>
	 * 
	 * @param type
	 *            Tipo de exportacion puede ser xls o pdf
	 * @return Reporte generado
	 */
	public void generateReport(String type);

	/**
	 * /** Metodo que sera invocado cuando se desea generar algun reporte
	 * correspondiente a un registro en especifico, el mismo invoca al servicio
	 * que generara el reporte fisicamente.<br>
	 * 
	 * @param type
	 *            Tipo de exportacion puede ser xls o pdf
	 * @return Reporte generado
	 */
	public void generateReportDetail(String type);

	/**
	 * Invoca al controllerHelper y obtiene las columnas que son visualizadas en
	 * la grilla.<br>
	 * 
	 * @return Columnas definidas en la grilla del ABM
	 */
	public LinkedList<Column> getColumns();

	/**
	 * Genera el titulo del reporte simple, dependiendo del nombre de la
	 * entidad.<br>
	 * 
	 * @return Titulo del reporte
	 */
	public String getHeaderReport();

	/**
	 * Retorna una cadena generada con un permiso por defecto
	 * 
	 * @return
	 */
	public String getDefaultPermission();

	public String getDeletePermission();

	public String getCreatePermission();

	public String getEditPermission();
	
	/**
	 * Metodo vacio que debe ser implementado en la clase que desea definir
	 * alguna configuración especial en la obtención de valores desde la base de
	 * datos 
	 * 
	 * @param sp parametro de busqueda definido en el paginHelper y a aser confirgurado
	 */
	public void configureSearchParam(ISearchParam sp);
}
