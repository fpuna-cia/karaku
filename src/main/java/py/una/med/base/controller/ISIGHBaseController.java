/**
 * @ISIGHBaseController 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.reports.Column;
import py.una.med.base.security.HasDefaultPermissions;
import py.una.med.base.util.PagingHelper;

/**
 * Interface que define el controlador básico para ser utilizado por una página
 * Facelets. Sirve tanto para manejar vistas y páginas de edición.
 * 
 * <br />
 * Un controlador que implementa esta interfaz solo podrá realizar casos de usos
 * simples, para Karaku, un caso de uso simple es aquel que no tiene la forma
 * cabecera detalle, para estos casos se usa la conjunción de este controlador y
 * {@link ISIGHMainController} (para el controlador cabecera) y
 * {@link ISIGHEmbeddableController} (para los controladores detalle). <br />
 * Ejemplos de uso:
 * 
 * <br />
 * <b>Vista de edición, modificación, y listado:</b>
 * 
 * <pre>
 * 		&lt;sigh:abm controller="#{etniaController}"
 * 			urlForm="/views/identificacion/etnia/fields.xhtml" />
 * </pre>
 * 
 * <b>Listado de elementos:</b>
 * 
 * <pre>
 * 		&lt;sigh:list controller="#{etniaController}" 
 * 			urlColumns="/views/identificacion/etnia/columns.xhtml"
 * 			urlSearchForm="/views/identificacion/etnia/fields.xhtml" />
 * </pre>
 * 
 * Siendo:
 * <ul>
 * <li><b>etniaController</b>: un {@link Component} que implemente esta
 * interfaz, tipicamente heredando de {@link SIGHBaseController} o
 * {@link SIGHAdvancedController}. En el mejor de los casos un controlador solo
 * debe implementar el método {@link #getBaseLogic()}.</li>
 * <li><b>urlColumns</b>: un archivo facelets que define un conjunto de
 * {@link UIColumn} que se utiliza para mostrar la lista de entidades.</li>
 * <li><b>urlform</b>: un archivo facelets que define un conjunto de
 * {@link UIInput} y {@link UIOutput} que se utiliza para la edición del
 * {@link #getBean()}</li>
 * </ul>
 * 
 * 
 * @author Arturo Volpe
 * @author Nathalia Ochoa
 * 
 * @param T
 *            entidad a ser manipulada (Si es una clase que hereda de
 *            {@link BaseEntity}, ID debe ser {@link Long}
 * @param ID
 *            Clase de la clave primaria, tipicamente {@link Long}
 * @since 1.0
 * @version 2.0 22/01/2013
 * @see SIGHBaseController SIGHBaseController, implementación que no utiliza
 *      reflexion.
 * @see SIGHAdvancedController SIGHAdvancedController, implementación que
 *      utiliza reflexion para realizar búsquedas automatizadas
 * @see ISIGHMainController ISIGHMainController interfaz que define la cabecera
 *      de un formulario complejo
 * @see ISIGHEmbeddableController ISIGHEmbeddableController interfaz que define
 *      el detalle de un caso de uso complejo
 */
public interface ISIGHBaseController<T, ID extends Serializable> extends
		HasDefaultPermissions {

	/**
	 * Enumeración utilizada para determinar el estado en el que se encuentra un
	 * {@link ISIGHBaseController} en un momento dado.
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Aug 1, 2013
	 * @see #LIST
	 * @see #VIEW
	 * @see #EDIT
	 * @see #NEW
	 * @see #SEARCH
	 * @see #DELETE
	 * 
	 */
	static enum Mode {
		/**
		 * Determina cuando el controlador esta en su punto de entrada,
		 * desplegando la lista de opciones y mostrando los botones para crear,
		 * editar y buscar.
		 * 
		 * @see ISIGHBaseController#isList()
		 */
		LIST,
		/**
		 * Determina cuando el controlador esta en modo ver, esto es cuando se
		 * muestra el formulario pero el mismo no es editable.
		 * 
		 * @see ISIGHBaseController#isView()
		 */
		VIEW,
		/**
		 * Determina si el controlador esta en modo de edición, en este modo el
		 * {@link ISIGHBaseController#getBean()} es editable, y no se realizan
		 * ningún tipo de búsquedas
		 * 
		 * @see ISIGHBaseController#isEdit()
		 * @see ISIGHBaseController#doEdit()
		 */
		EDIT,
		/**
		 * Determina si el controlador esta en modo de creación, en este modo el
		 * {@link ISIGHBaseController#getBean()} es editable (y su id es null),
		 * y no se realizan ningún tipo de búsquedas.
		 * 
		 * @see ISIGHBaseController#isCreate()
		 * @see ISIGHBaseController#doCreate()
		 */
		NEW,
		/**
		 * Determina si el controlador esta en modo de búsqueda avanzada, en
		 * este modo el {@link ISIGHBaseController#getBean()} es editable (y su
		 * id es <code>null</code>).
		 * 
		 * @see ISIGHBaseController#isSearch()
		 * @see ISIGHBaseController#doSearch()
		 */
		SEARCH,
		/**
		 * Determina si el controlador esta en modo de eliminación, en este modo
		 * el {@link ISIGHBaseController#getBean()} no es editable.
		 * 
		 * @see ISIGHBaseController#isDelete()
		 * @see ISIGHBaseController#doDelete()
		 */
		DELETE;

	}

	/**
	 * Limpia los filtros de búsqueda, tanto de la búsqueda simple como de la
	 * compleja
	 * 
	 * @see #setFilterOption(String)
	 * @see #setFilterValue(String)
	 * @see #setExample(Object)
	 */
	void clearFilters();

	/**
	 * Asigna un objeto de la misma clase que la entidad para poder realizar
	 * búsquedas con este bean.
	 * 
	 * @param example
	 *            entidad que se usara de ejemplo
	 * @see BaseDAO#getAllByExample(Object, ISearchParam)
	 */
	void setExample(T example);

	/**
	 * Retorna el bean por el que se esta buscando
	 * 
	 * @see #doSearch()
	 * @see BaseDAO#getAllByExample(Object, ISearchParam)
	 * @return Example actual
	 */
	T getExample();

	/**
	 * Asigna un valor al filtro simple.
	 * 
	 * @param filterValue
	 *            cadena por al cual se filtrara
	 * @see #setFilterOption(String)
	 */
	void setFilterValue(String filterValue);

	/**
	 * Asigna un valor a la opción por la cual se busca.
	 * 
	 * @param filterOption
	 *            columna por la cual se filtrar
	 * @see #setFilterValue(String)
	 * @see #getBaseSearchItems()
	 * @see #getSearchSelectItemsList()
	 */
	void setFilterOption(String filterOption);

	/**
	 * Cancela todos los cambios realizados por el controlador y vuelve al paso
	 * anterior.
	 * 
	 * @return cadena de navegación para volver al paso previo
	 * @see #goList()
	 * @see #goView()
	 */
	String doCancel();

	/**
	 * Crea el {@link #getBean()}, lo persiste y vuelve al paso anterior.
	 * 
	 * @return cadena de navegación para la página siguiente a la creación,
	 *         retorna <code>""</code> o <code>null</code> si no pudo guardar.
	 * @see #postCreate()
	 * 
	 */
	String doCreate();

	/**
	 * Elimina el {@link #getBean()} de la base de datos.
	 * 
	 * @return cadena de navegación para la página siguiente a la eliminación
	 * @see #goList()
	 * @see #postCreate()
	 */
	String doDelete();

	/**
	 * Edita los cambios realizados al {@link #getBean()}.
	 * 
	 * @return cadena de navegación para la página siguiente a la edición.
	 * 
	 * @see #postEdit()
	 * @see #goList()
	 */
	String doEdit();

	/**
	 * Realiza los cambios necesarios al controlador para que la siguiente
	 * llamada a {@link #getEntities()} retorna un subconjunto de los registros.<br />
	 * Debe tomar en cuenta <b>solamente</b> la búsqueda avanzada.
	 * 
	 * @see #setExample(Object)
	 */
	void doSearch();

	/**
	 * Realiza los cambios necesarios al controlador para que la siguiente
	 * llamada a {@link #getEntities()} retorna un subconjunto de los registros.<br />
	 * Debe tomar en cuenta <b>solamente</b> la búsqueda simple.
	 * 
	 * @see #setFilterOption(String)
	 * @see #setFilterValue(String)
	 */
	void doSimpleSearch();

	/**
	 * Retorna una instancia de la lógica asociada a este controlador, necesaria
	 * para realizar las búsquedas y las operaciones relacioandas cno la base de
	 * datos.
	 * 
	 * @return lógica del caso de uso
	 */
	ISIGHBaseLogic<T, ID> getBaseLogic();

	/**
	 * Retorna el bean que esta siendo actualmente manipulado.
	 * 
	 * @see #setBean(Object)
	 * @return bean actualmente manipulado, o <code>null</code> si
	 *         {@link #isList()}
	 */
	T getBean();

	/**
	 * Retorna la lista de entidades que serán mostradas en la grilla.
	 * 
	 * @return entidades a mostrar.
	 */
	List<T> getEntities();

	/**
	 * Retorna un Where con todos los filtros aplicados, esto incluye
	 * <b>solo</b> búsquedas simples
	 * 
	 * @return {@link Where} configurado con todo lo necesario para hacer una
	 *         búsqueda simple
	 */
	Where<T> getSimpleFilters();

	/**
	 * Retorna una cadena única que representa a todos los controladores de esta
	 * clase.
	 * 
	 * @return cadena formada por el nombre de la clase más 'faces_message'
	 */
	String getMessageIdName();

	/**
	 * Determina el modo actual de un controlador.
	 * 
	 * @return {@link Mode} actual
	 * @see Mode
	 * 
	 */
	Mode getMode();

	/**
	 * Obtiene una referencia al {@link PagingHelper} actual utilizado por el
	 * controller. Para obtener la página actual utilizar los métodos
	 * {@link PagingHelper#getPage()} y {@link PagingHelper#getReadablePage()}.
	 * 
	 * @return {@link PagingHelper} actual
	 */
	PagingHelper getPagingHelper();

	/**
	 * Esta lista del tipo SelectItem es necesaria para las opciones de búsqueda
	 * de este caso de uso (búsqueda simple).
	 * 
	 * @return Lista de {@link SelectItem} necesarios para podes mostrar el
	 *         combobox
	 * @see SelectHelper
	 */
	List<SelectItem> getSearchSelectItemsList();

	/**
	 * Método invocado para ir a la vista de eliminación de registros, al ser
	 * invocado este método, se debe asegurar que {@link #getBean()} este
	 * configurado con la entidad a eliminar.
	 * 
	 * @return cadena de navegación para ir a la vista de eliminación
	 * @see #isDelete()
	 * @see #preDelete()
	 */
	String goDelete();

	/**
	 * Método invocado para ir a la vista de edición de registros, al ser
	 * invocado este método, se debe asegurar que {@link #getBean()} este
	 * configurado con la entidad a editar.
	 * 
	 * @return cadena de navegación para ir a la vista de edición
	 * @see #preEdit()
	 * @see #doEdit()
	 */
	String goEdit();

	/**
	 * Método invocado para ir a la vista de edición de registros, al ser
	 * invocado este método, se debe asegurar que {@link #getEntities()} pueda
	 * retornar la lista de entidades.
	 * 
	 * @return cadena de navegación para ir a la vista de lista
	 * @see #preList()
	 */
	String goList();

	/**
	 * Método invocado para ir a la vista de creación de registros, al ser
	 * invocado este método se debe asegurar que {@link #getBean()} retorne una
	 * instancia limpia y ya configurada para ser creada.
	 * <p>
	 * Cualquier lógica para entidades nueva debe ser realizada en
	 * {@link #preCreate()}, este método es solo para navegación.
	 * </p>
	 * 
	 * @see #preCreate()
	 * @return cadena de navegación para ir a la vista de creación
	 */
	String goNew();

	/**
	 * Método invocado para ir a la vista de creación de registros, al ser
	 * invocado este método se debe asegurar que {@link #getBean()} retorne una
	 * ya configurada para poder verse.
	 * <p>
	 * Cualquier lógica para visualiar la entidad debe ser realizada en
	 * {@link #preView()}, este método es solo para navegación.
	 * </p>
	 * 
	 * @see #preView()
	 * @return cadena de navegación para ir a la vista de visualización
	 */
	String goView();

	/**
	 * Verifica si el controlador esta en modo de creación.
	 * 
	 * @return <code>true</code> si esta en modo de creación, <code>false</code>
	 *         en cualquier otro caso
	 */
	boolean isCreate();

	/**
	 * Verifica si el controlador esta en modo de eliminación.
	 * 
	 * @return <code>true</code> si esta en modo de eliminación,
	 *         <code>false</code> en cualquier otro caso
	 */
	boolean isDelete();

	/**
	 * Verifica si el controlador esta en modo de edición.
	 * 
	 * @return <code>true</code> si esta en modo de edición, <code>false</code>
	 *         en cualquier otro caso
	 */
	boolean isEdit();

	/**
	 * Verifica si el controlador esta en modo de búsqueda avanzada.
	 * 
	 * @return <code>true</code> si esta en modo de búsqueda, <code>false</code>
	 *         en cualquier otro caso
	 */
	boolean isSearch();

	/**
	 * Verifica si el controlador esta en modo de vista de entidad.
	 * 
	 * @return <code>true</code> si esta en modo de vista, <code>false</code> en
	 *         cualquier otro caso
	 */
	boolean isView();

	/**
	 * Verifica si el controlador esta en modo de vista de entidades (entrada
	 * del caso de uso).
	 * 
	 * @return <code>true</code> si esta en modo de lista, <code>false</code> en
	 *         cualquier otro caso
	 */
	boolean isList();

	/**
	 * Método invocado una vez que se termino la creación del registro, para
	 * realizar cualquier lógica. <br />
	 * Utilizar {@link #getBean()} para obtener la instancia recién creada.
	 * 
	 * @return cadena de navegación para ir después de crear
	 * @see #goList()
	 */
	String postCreate();

	/**
	 * Método invocado una vez que se termino la eliminación del registro, para
	 * realizar cualquier lógica. <br />
	 * 
	 * @return cadena de navegación para ir después de eliminar
	 * @see #goList()
	 */
	String postDelete();

	/**
	 * Método invocado una vez que se termino la edición del registro (ya se
	 * persistio), para realizar cualquier lógica. <br />
	 * Utilizar {@link #getBean()} para obtener la instancia recién editada.
	 * 
	 * @return cadena de navegación para ir después de eliminar
	 * @see #goList()
	 */
	String postEdit();

	/**
	 * Método invocado una vez que se termino la búsqueda avanzada, para
	 * realizar cualquier lógica de limpieza. <br />
	 * Utilizar {@link #getExample()} para obtener la instancia recién editada y
	 * por la cual se buscó.
	 * 
	 * @see #goList()
	 */
	void postSearch();

	/**
	 * Método que será invocado antes de crear un elemento, este método debe
	 * suponer que {@link ISIGHBaseController#getBean()} retornara la entidad a
	 * crear. <br />
	 * Aquí se debe realizar la inicialización del bean, ya que
	 * {@link #getBean()} debe retornar la entidad a crear.
	 * 
	 * @return cadena que representa la página donde se creara el bean
	 */
	String preCreate();

	/**
	 * Método que será invocado antes de eliminar un elemento, este método debe
	 * suponer que {@link ISIGHBaseController#getBean()} retornara la entidad a
	 * eliminar
	 * 
	 * @return cadena que representa la página donde se creara el bean
	 */
	String preDelete();

	/**
	 * Método que será invocado antes de editar un elemento, este método debe
	 * suponer que {@link ISIGHBaseController#getBean()} retornara la entidad a
	 * editar
	 * 
	 * @return cadena que representa la pagina donde se editara el bean
	 */
	String preEdit();

	/**
	 * Método que será invocado antes de mostrar la lista de elementos
	 * 
	 * @return cadena de navegación que redirige a la lista
	 * @see #goList()
	 */
	String preList();

	/**
	 * Método invocado antes de ir al cuadro de búsqueda avanzada.
	 * 
	 * @see #doSearch()
	 */
	void preSearch();

	/**
	 * Método invocado antes de volver a la página donde se visualiza la grilla
	 * 
	 * @return cadena de navegación para ir a la lista
	 * @see #goList()
	 */
	String preView();

	/**
	 * Asigna un bean para que el controlador actue sobre él, todas las llamadas
	 * a los métodos que provocan modificaciones ({@link #doEdit()}
	 * {@link #doDelete()} {@link #doCreate()}) actuarán sobre este bean.
	 * 
	 * @param bean
	 *            a ser utilizado
	 * @see #doCreate()
	 * @see #doEdit()
	 * @see #doDelete()
	 */
	void setBean(T bean);

	/**
	 * Construye un {@link Where} que puede ser utilizado para realizar
	 * consultas y filtrar los resultados mostrados por este caso de uso.
	 * 
	 * @return {@link Where} configurado con los criterios de búsqueda
	 */
	Where<T> getBaseWhere();

	/**
	 * Método que será invocado cuando se desea generar algún reporte, el mismo
	 * recoge los parámetros de filtros ingresados y retorna el where
	 * correspondiente. Dicho where será utilizado posteriormente para realizar
	 * la consulta a la base de datos.
	 * 
	 * @return Entidad que representa los filtros ingresados
	 */
	Where<T> getWhereReport();

	/**
	 * Método que será invocado cuando se desea generar algún reporte, el mismo
	 * recoje los parámetros de filtros ingresados que serán desplegados en el
	 * reporte.
	 * 
	 * @param paramsReport
	 *            parámetros del reporte
	 * @return String que representa los criterios de selección
	 */
	Map<String, Object> getParamsFilter(Map<String, Object> paramsReport);

	/**
	 * Método que será invocado cuando se desea generar algún reporte
	 * correspondiente a la grilla, el mismo invoca al servicio que generara el
	 * reporte físicamente.
	 * <p>
	 * Notar que este método genera el reporte e interrupente todas las faces
	 * JSF.
	 * </p>
	 * 
	 * @param type
	 *            Tipo de exportación puede ser <code>xls</code> o
	 *            <code>pdf</code>
	 */
	void generateReport(String type);

	/**
	 * Método que será invocado cuando se desea generar algún reporte
	 * correspondiente a un registro en especifico, el mismo invoca al servicio
	 * que generara el reporte físicamente.
	 * <p>
	 * Notar que este método genera el reporte e interrupente todas las faces
	 * JSF.
	 * </p>
	 * 
	 * @param type
	 *            Tipo de exportación puede ser 'xls' o 'pdf'
	 */
	void generateReportDetail(String type);

	/**
	 * Escanea el archivo columns.xhtml donde se definen las columnas
	 * visualizadas en la grilla, y retorna las mismas.
	 * 
	 * @return Columnas definidas en la grilla del ABM
	 * @see ControllerHelper#getColumns()
	 */
	List<Column> getColumns();

	/**
	 * Genera el titulo del reporte simple, dependiendo del nombre de la
	 * entidad.
	 * 
	 * @return Titulo del reporte
	 */
	String getHeaderReport();

	/**
	 * Método que debe ser implementado en la clase que desea definir alguna
	 * configuración especial en la obtención de valores desde la base de datos
	 * 
	 * @param sp
	 *            parámetro de búsqueda definido en el paginHelper y a ser
	 *            configurado
	 */
	void configureSearchParam(ISearchParam sp);

	/**
	 * Retorna la cadena a ser visualizada como cabecera del caso de uso según
	 * la acción a ejecutar. Un ejemplo de lo retornado por este método puede
	 * ser:
	 * 
	 * <pre>
	 * &quot;Editando persona&quot;
	 * </pre>
	 * 
	 * @return cadena para resolver el archivo xhtml para buscar los datos de la
	 *         cabecera.
	 */
	String getHeaderText();

	/**
	 * Prepara la lista de entidades para que sea recargada la siguiente vez que
	 * se la necesite.
	 */
	void reloadEntities();

}
