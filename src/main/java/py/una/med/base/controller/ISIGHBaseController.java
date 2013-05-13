/**
 * @ISIGHBaseController 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
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

	void clearFilters();

	String doCancel();

	String doCreate();

	String doDelete();

	String doEdit();

	void doSearch();

	ISIGHBaseLogic<T, ID> getBaseLogic();

	abstract List<String> getBaseSearchItems();

	T getBean();

	List<T> getEntities();

	Where<T> getFilters();

	String getMessageIdName();

	Mode getMode();

	PagingHelper<T, ID> getPagingHelper();

	/**
	 * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
	 * este objeto.
	 */
	List<SelectItem> getSearchSelectItemsList();

	String getUsarController();

	String goDelete();

	String goEdit();

	String goList();

	String goNew();

	String goView();

	boolean isCreate();

	boolean isDelete();

	boolean isEdit();

	boolean isSearch();

	boolean isView();

	String postCreate();

	String postDelete();

	String postEdit();

	void postSearch();

	String preCreate();

	String preDelete();

	/**
	 * Metodo que sera invocado antes de editar un elemento, este metodo debe
	 * suponer que {@link ISIGHBaseController#getBean()} retornara la entidad a
	 * editar
	 * 
	 * @return cadena que representa la pagina donde se editara el bean
	 */
	String preEdit();

	/**
	 * Metodo que sera invocado antes de mostrar la lista de elementos
	 * 
	 * @return cadena que representa la pagina donde se mostrara la lista
	 */
	String preList();

	void preSearch();

	String preView();

	void setBean(T bean);

	void setFilterValue(String filterValue);

	/**
	 * Metodo que sera invocado cuando se desea generar algun reporte, el mismo
	 * recoje los parametros de filtros ingresados y retorna el where
	 * correspondiente. Dicho where sera utilizado posteriormente para realizar
	 * la consulta a la base de datos.<br>
	 * 
	 * @return Entidad que representa los filtros ingresados
	 */
	Where<T> getWhereReport();

	/**
	 * Metodo que sera invocado cuando se desea generar algun reporte, el mismo
	 * recoje los parametros de filtros ingresados que seran desplegados en el
	 * reporte.<br>
	 * 
	 * @return String que representa los criterios de seleccion
	 */
	Map<String, Object> getParamsFilter(Map<String, Object> paramsReport);

	/**
	 * /** Metodo que sera invocado cuando se desea generar algun reporte
	 * correspondiente a la grilla, el mismo invoca al servicio que generara el
	 * reporte fisicamente.<br>
	 * 
	 * @param type
	 *            Tipo de exportacion puede ser xls o pdf
	 * @return Reporte generado
	 */
	void generateReport(String type);

	/**
	 * /** Metodo que sera invocado cuando se desea generar algun reporte
	 * correspondiente a un registro en especifico, el mismo invoca al servicio
	 * que generara el reporte fisicamente.<br>
	 * 
	 * @param type
	 *            Tipo de exportacion puede ser xls o pdf
	 * @return Reporte generado
	 */
	void generateReportDetail(String type);

	/**
	 * Invoca al controllerHelper y obtiene las columnas que son visualizadas en
	 * la grilla.<br>
	 * 
	 * @return Columnas definidas en la grilla del ABM
	 */
	List<Column> getColumns();

	/**
	 * Genera el titulo del reporte simple, dependiendo del nombre de la
	 * entidad.<br>
	 * 
	 * @return Titulo del reporte
	 */
	String getHeaderReport();

	/**
	 * Retorna una cadena generada con un permiso por defecto
	 * 
	 * @return
	 */
	String getDefaultPermission();

	String getDeletePermission();

	String getCreatePermission();

	String getEditPermission();

	/**
	 * Metodo vacio que debe ser implementado en la clase que desea definir
	 * alguna configuración especial en la obtención de valores desde la base de
	 * datos
	 * 
	 * @param sp
	 *            parametro de busqueda definido en el paginHelper y a aser
	 *            confirgurado
	 */
	void configureSearchParam(ISearchParam sp);
}
