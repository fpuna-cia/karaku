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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.breadcrumb.BreadcrumbController;
import py.una.pol.karaku.business.reports.KarakuBaseReportSimple;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.jsf.utils.ICurrentpageHelper;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.reports.Column;
import py.una.pol.karaku.security.HasRole;
import py.una.pol.karaku.security.KarakuSecurity;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.EntitySerializer;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.PagingHelper;
import py.una.pol.karaku.util.PagingHelper.ChangeListener;
import py.una.pol.karaku.util.SelectHelper;
import py.una.pol.karaku.util.StringUtils;

/**
 * Controlador base para todos los controladores del sistema, implementa las
 * funcionalidades definidas en {@link IKarakuBaseController}.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.5 Aug 1, 2013
 * @see IKarakuBaseController
 * 
 */
public abstract class KarakuBaseController<T, K extends Serializable>
        implements IKarakuBaseController<T, K> {

    private static final String LIST_XHTML = "list.xhtml";
    private static final String ABM_XHTML = "abm.xhtml";
    private static final String REPORT_SELECTION_KEY = "selectionFilters";
    private static final String COLUMN_NOT_FOUND_IN_ENTITY_MESSAGE = "Column: {} not found in entity: {}";

    /**
     * Vector que contiene las columnas por las que se intentará ordenar.
     */
    private static final String[] DEFAULT_SORT_COLUMNS = { "descripcion", "id" };

    @Autowired
    private BreadcrumbController breadcrumController;

    @Autowired
    private ICurrentpageHelper currentPageHelper;

    private static final int ROWS_FOR_PAGE = 10;

    private Mode mode = Mode.VIEW;

    private String filterValue;

    private String filterOption;

    private T bean;

    private T example;

    /**
     * Logger de este controlador.
     */
    @Log
    protected Logger log;

    private PagingHelper pagingHelper;

    private String messageIdName;

    @Autowired
    private I18nHelper i18nHelper;

    /**
     * Controller helper.
     */
    @Autowired
    protected ControllerHelper controllerHelper;

    @Autowired
    private KarakuBaseReportSimple baseReportSimple;

    /**
     * Lista de entidades mostradas actualmente, cualquier manipulación a ella
     * se vera reflejada cuando se refresque la grilla, si se la marca como nula
     * la siguiente vez que se pidan los datos será cargada. Utilizar el método
     * {@link #reloadEntities()} para marcar la lista como sucia y que deba ser
     * recargada.
     */
    protected List<T> entities;

    @Override
    public void clearFilters() {

        this.log.info("Clear Filters called");
        this.mode = Mode.LIST;
        this.setExample(null);
        this.setFilterValue("");
        this.setFilterOption("");
        this.reloadEntities();
    }

    /**
     * Crea un nuevo mensaje para ser mostrado en la interfaz.
     * 
     * @param severity
     *            severidad
     * @param summary
     *            mensaje corto
     * @param detail
     *            detalle
     * @param componentId
     *            componente sobre el cual mostrar.
     */
    public void createFacesMessage(Severity severity, String summary,
            String detail, String componentId) {

        this.controllerHelper.createFacesMessage(severity, summary, detail,
                componentId);
    }

    /**
     * Crea un mensaje global.
     * 
     * @param severity
     *            severidad del mismo
     * @param detail
     *            clave del archivo de internacionalización
     */
    public void createGlobalFacesMessage(Severity severity, String detail) {

        this.controllerHelper.createGlobalFacesMessage(severity, detail);
    }

    /**
     * Crea un mensaje global.
     * 
     * @param severity
     *            severidad del mismo
     * @param detail
     *            clave del archivo de internacionalización
     * @param summary
     *            resumen del mensaje
     */
    public void createGlobalFacesMessage(Severity severity, String summary,
            String detail) {

        this.controllerHelper.createGlobalFacesMessage(severity, summary,
                detail);
    }

    @Override
    public String doCancel() {

        this.log.info("DoCancel llamado");
        return "";
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT_CREATE)
    public String doCreate() {

        this.log.info("doCreate llamado");
        try {
            this.getBaseLogic().add(this.bean);
            this.reloadEntities();
            this.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
                    "BASE_ABM_CREATE_SUCCESS");
            this.postCreate();
            return this.goList();
        } catch (Exception e) {
            log.warn("Cant create entity {}", e);
            this.controllerHelper.createGlobalFacesMessage(
                    FacesMessage.SEVERITY_WARN, "BASE_ABM_CREATE_FAILURE",
                    e.getMessage());
            return "";
        }

    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT_DELETE)
    public String doDelete() {

        try {
            this.log.info("Do Delete llamado");
            this.getBaseLogic().remove(this.getBean());
            this.reloadEntities();
            this.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
                    "MESSAGE_DELETE_SUCCESS");
            return this.goList();
        } catch (Exception e) {
            log.warn("Cant delete entity {}", e);
            this.controllerHelper.createGlobalFacesMessage(
                    FacesMessage.SEVERITY_ERROR, e.getMessage());
            return "";
        }
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT_EDIT)
    public String doEdit() {

        try {
            this.log.info("Do edit llamado");
            this.getBaseLogic().update(this.bean);
            this.reloadEntities();
            this.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
                    "BASE_ABM_EDIT_SUCCESS");
            return this.goList();
        } catch (Exception e) {
            this.log.error("Error al editar", e);
            this.controllerHelper.createGlobalFacesMessage(
                    FacesMessage.SEVERITY_ERROR, "BASE_ABM_EDIT_FAILURE");
            return "";
        }
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT)
    public void doSearch() {

        this.controllerHelper.updateModel("pgSearch");

        this.log.info("do search llamado");
        this.setExample(this.getBean());
        this.reloadEntities();
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT)
    public void doSimpleSearch() {

        this.log.info("doSimpleSearch llamado");
        setMode(Mode.LIST);
        if (!StringUtils.isValid(this.getFilterOption())
                || !StringUtils.isValid(this.getFilterValue())) {
            this.createGlobalFacesMessage(FacesMessage.SEVERITY_WARN,
                    "BASE_ABM_SEARCH_VALUE_OPTION_REQUIRED");
            return;
        } else {
            this.setExample(null);

            this.reloadEntities();
        }

    }

    /**
     * Retorna una lista de cadenas representando las opciones por las cuales el
     * caso de uso podra buscar, es un método de utilidad para
     * {@link #getSearchSelectItemsList()}
     * 
     * @return lista de strings
     * @see #getSearchSelectItemsList()
     */
    public abstract List<String> getBaseSearchItems();

    /**
     * Retorna el bean que actualmente se esta visualizando, editando o
     * borrando.
     * 
     * @return entidad actual
     */
    @Override
    public T getBean() {

        if (this.bean == null) {
            this.bean = this.getBaseLogic().getNewInstance();
        }
        return this.bean;
    }

    /**
     * Genera un {@link Where} para los reportes.
     * 
     * @return {@link Where} con los filtros aplicados.
     */
    @Override
    public Where<T> getWhereReport() {

        return getBaseWhere();
    }

    /**
     * Retorna la lista de parámetros por los cuales se filtrara el reporte.
     * <p>
     * Puede ser por la búsqueda simple o por la avanzada
     * </p>
     * 
     * @param paramsReport
     *            mapa de los reportes.
     * @return mismo mapa recibido, pero con los filtros.
     */
    @Override
    public Map<String, Object> getParamsFilter(Map<String, Object> paramsReport) {

        if (this.example != null) {
            paramsReport.put(REPORT_SELECTION_KEY, getParamsAdvancedReport());
            return paramsReport;
        }
        if (StringUtils.isValid(getFilterValue())) {
            paramsReport.put(REPORT_SELECTION_KEY, this.getFilterOption()
                    + ": " + this.getFilterValue());
            return paramsReport;
        }
        return paramsReport;

    }

    /**
     * Retorna un String Serializado con los atributos de la clase base para el
     * parametro de un reporte
     * 
     * @return
     */
    protected String getParamsAdvancedReport() {

        return EntitySerializer.serialize(this.getExample());
    }

    @Override
    public void generateReport(String type) {

        Map<String, Object> paramsReport = new HashMap<String, Object>();
        paramsReport.put("titleReport", this.getHeaderReport());
        Class<T> clazz = this.getBaseLogic().getDao().getClassOfT();
        try {
            this.baseReportSimple.generateReport(
                    this.getParamsFilter(paramsReport), type,
                    this.getColumns(), this.getBaseLogic(),
                    this.getWhereReport(), clazz);
            this.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
                    "BASE_REPORT_CREATE_SUCCESS");
        } catch (ReportException e) {
            this.log.warn("Generate report failed", e);
            this.createGlobalFacesMessage(FacesMessage.SEVERITY_INFO,
                    "BASE_REPORT_CREATE_FAILURE");
        }
    }

    @Override
    public void generateReportDetail(String type) {

    }

    /**
     * Retorna la lista de columnas para ser mostradas por el reporte.
     * 
     * @return lista de columnas
     */
    @Override
    public List<Column> getColumns() {

        return this.controllerHelper.getColumns();
    }

    @Override
    public String getHeaderReport() {

        Class<T> clazz = this.getBaseLogic().getDao().getClassOfT();
        List<String> listName = StringUtils.split(clazz.getSimpleName());
        String nameList = StringUtils.pluralize(listName);
        return this.i18nHelper.getString("BASE_REPORT_NAME") + " " + nameList;
    }

    /**
     * Retorna la lista de entidades
     * 
     * @return lista de entidades a mostrar
     */
    @Override
    public List<T> getEntities() {

        if (this.entities == null) {
            this.loadEntities();
        }
        return this.entities;
    }

    /**
     * Recarga las entidades. Este método es el que se encarga realmente de
     * realizar las llamadas a la base de datos.
     */
    protected void loadEntities() {

        this.log.debug("Recargando entidades en el controlador {}", this);

        Where<T> baseWhere = this.getBaseWhere();

        this.getPagingHelper().udpateCount(
                this.getBaseLogic().getCount(baseWhere));

        ISearchParam sp = this.getPagingHelper().getISearchparam();
        this.configureSearchParam(sp);

        this.entities = this.getBaseLogic().getAll(baseWhere, sp);
    }

    /**
     * Retorna un {@link Where} configurado como base, notar que si
     * {@link #getExample()} es <code>null</code> (cuando no hay búsqueda
     * avanzada), retorna lo mismo que {@link #getSimpleFilters()}, por ello,
     * siempre es recomendable reescribir estos dos métodos cuando se quiere
     * modificar lo que se muestra.
     * 
     * @return {@link Where} configurado para mostrar
     */
    @Override
    public Where<T> getBaseWhere() {

        Where<T> baseWhere;
        if (this.isSearch()) {
            baseWhere = new Where<T>();
            baseWhere.setExample(this.getExample());
        } else {
            baseWhere = this.getSimpleFilters();
        }
        return baseWhere;
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
    @Override
    public void configureSearchParam(ISearchParam sp) {

        for (String s : DEFAULT_SORT_COLUMNS) {
            if (s == null) {
                continue;
            }
            try {
                this.getBaseLogic().getDao().getClassOfT().getDeclaredField(s);
                sp.addOrder(s, true);
                return;
            } catch (SecurityException e) {
                log.trace(COLUMN_NOT_FOUND_IN_ENTITY_MESSAGE, s, e);
            } catch (NoSuchFieldException e) {
                log.trace(COLUMN_NOT_FOUND_IN_ENTITY_MESSAGE, s, e);
            }
        }
        throw new KarakuRuntimeException(
                "Tabla '"
                        + this.getBaseLogic().getDao().getTableName()
                        + "' no posee las columnas por defecto para ordenar "
                        + Arrays.toString(DEFAULT_SORT_COLUMNS)
                        + " por favor, reescriba el método configureSearchparam en el controlador: "
                        + this.getClass().getSimpleName());

    }

    /**
     * Retorna la entidad que esta siendo utilizada como ejemplo por al búsqueda
     * avanzada.
     * 
     * @return Entidad utilziada como ejemplo
     */
    @Override
    public T getExample() {

        return this.example;
    }

    /**
     * Retorna la opción actualmente seleccionada en la vista (filtro simple).
     * 
     * @return cadena que representa el texto actualmente seleccionado en el
     *         combo box.
     */
    public String getFilterOption() {

        return this.filterOption;
    }

    /**
     * Retorna el valor actual en el filtro simple
     * 
     * @return String ingresado por el usuario
     */
    public String getFilterValue() {

        return this.filterValue;
    }

    /**
     * Retorna una cadena internacionalizada dada la llave.
     * 
     * @param code
     *            clave del archivo de internacionalización
     * @param params
     *            parámetros.
     * @return cadena internacionalizada
     */
    public String getMessage(final String code, final Object ... params) {

        return i18nHelper.getString(code, params);
    }

    /**
     * Cada formulario tiene {@literal <}h:message /> para mostrar los posibles
     * errores, ponerle estáticamente un id es fácil pero hay un solo
     * controlador genérico, por lo que el id debe ser único en ese caso. Si en
     * una vista se quiere incluir varios formularios, surge el problema de
     * duplicidad de componentes (dos o más h:message con el mismo id).
     * <p>
     * Por lo tanto, se genera un id que tiene como prefijo el nombre de la
     * entidad seguido de "faces_message". Ej: Usuario_faces_message
     * </p>
     * 
     * @return cadena con el nombre del controlador, mas _faces_message
     **/
    @Override
    public String getMessageIdName() {

        if (this.messageIdName == null) {
            this.messageIdName = this.getBaseLogic().getDao().getClassOfT()
                    .getSimpleName()
                    + "_faces_message";
        }
        return this.messageIdName;
    }

    @Override
    public Mode getMode() {

        return this.mode;
    }

    /**
     * Se crea un nuevo {@link PagingHelper} y se le asigna un
     * {@link ChangeListener} que recarga la lista de entidades cada vez que hay
     * un cambio
     * 
     * @return {@link PagingHelper} que se encarga de la paginación
     */
    @Override
    public PagingHelper getPagingHelper() {

        if (this.pagingHelper == null) {
            this.pagingHelper = new PagingHelper(this.getRowsForPage());
            this.pagingHelper.setChangeListener(new ChangeListener() {

                @Override
                public void onChange(PagingHelper thizz, int previousPage,
                        int currentPage) {

                    KarakuBaseController.this.reloadEntities();
                }
            });
        }
        return this.pagingHelper;
    }

    /**
     * Cantidad de registros mostrados por este controlador en la vista.
     * 
     * @return {@value #ROWS_FOR_PAGE}
     */
    public int getRowsForPage() {

        return ROWS_FOR_PAGE;
    }

    /**
     * Esta lista del tipo SelectItem es necesaria para los combobox hechos con
     * este objeto (para la lista de filtros).
     * 
     * @return {@link List} de {@link SelectItem} que representan los criterios
     *         por los cuales se puede buscar en este controller.
     */
    @Override
    public List<SelectItem> getSearchSelectItemsList() {

        return SelectHelper.getSelectItems(this.getBaseSearchItems());

    }

    /**
     * XXX Workaround para registrar a este controlador como el principal en un
     * momento dado.
     * 
     * @return ""
     */
    public String getUsarController() {

        this.reloadEntities();
        // XXX Setea este controllador, como el controlador actual para el
        // breadcrum
        this.breadcrumController.setActualController(this);
        return "";
    }

    @Override
    public String goDelete() {

        return ABM_XHTML;
    }

    @Override
    public String goEdit() {

        return ABM_XHTML;
    }

    @Override
    public String goList() {

        return LIST_XHTML;
    }

    @Override
    public String goNew() {

        return this.goEdit();
    }

    @Override
    public String goView() {

        return ABM_XHTML;
    }

    @Override
    public boolean isCreate() {

        return this.mode == Mode.NEW;
    }

    @Override
    public boolean isDelete() {

        return this.mode.equals(Mode.DELETE);
    }

    @Override
    public boolean isEdit() {

        return this.mode == Mode.EDIT;
    }

    /**
     * Define si un atributo es o no editable en el estado actual del
     * controlador.
     * 
     * @param campo
     *            field a verificar
     * @return <code>true</code> si puede ser editable, <code>false</code> si
     *         debe mostrarse deshabilitado.
     */
    public boolean isEditable(String campo) {

        this.log.trace("Verificando visibilidad de: {}", campo);
        if (this.mode == null) {
            this.mode = Mode.VIEW;
        }

        if (this.mode == Mode.VIEW || this.mode == Mode.DELETE) {
            return false;
        }
        return true;
    }

    /**
     * Define si actualmente se esta en una búsqueda simple.
     * 
     * @return <code>true</code> si los filtros son adecuados y
     *         <code>false</code> en caso contrario
     */
    protected boolean isSimpleSearch() {

        return StringUtils.isValid(getFilterOption(), getFilterValue())
                && !isSearch();
    }

    @Override
    public boolean isSearch() {

        return this.mode == Mode.SEARCH;
    }

    @Override
    public boolean isView() {

        return this.mode == Mode.VIEW;
    }

    @Override
    public String postCreate() {

        this.mode = Mode.LIST;
        return this.goList();
    }

    @Override
    public String postDelete() {

        this.mode = Mode.LIST;
        return this.goList();
    }

    @Override
    public String postEdit() {

        this.mode = Mode.LIST;
        return this.goList();
    }

    @Override
    public void postSearch() {

        this.log.info("post search llamado");
        this.mode = Mode.VIEW;

    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT_CREATE)
    public String preCreate() {

        this.mode = Mode.NEW;
        this.setBean(this.getBaseLogic().getNewInstance());
        this.log.info("preCreate llamado");
        return this.goNew();
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT_DELETE)
    public String preDelete() {

        this.log.info("Pre Delete llamado");
        this.mode = Mode.DELETE;
        return this.goDelete();
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT_EDIT)
    public String preEdit() {

        this.mode = Mode.EDIT;
        this.log.info("Pre edit llamado");
        return this.goEdit();
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT)
    public String preList() {

        this.setBean(null);
        this.mode = Mode.LIST;
        return this.goList();
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT)
    public void preSearch() {

        this.log.info("pre search llamado");
        this.mode = Mode.SEARCH;
        this.setBean(this.getExample());
    }

    @Override
    @HasRole(KarakuSecurity.DEFAULT)
    public String preView() {

        this.mode = Mode.VIEW;
        this.log.info("pre View llamado");
        return this.goView();
    }

    @Override
    public void setBean(T bean) {

        this.bean = bean;
    }

    @Override
    public void setExample(T example) {

        this.example = example;
    }

    @Override
    public void setFilterOption(String filterOption) {

        this.filterOption = filterOption;
    }

    @Override
    public void setFilterValue(String filterValue) {

        if (StringUtils.isValid(filterValue)) {
            this.filterValue = filterValue.toUpperCase();
            return;
        }
        this.filterValue = null;
    }

    /**
     * Modifica el estado actual del controlador.
     * 
     * @param mode
     *            nuevo estado
     */
    public void setMode(Mode mode) {

        this.mode = mode;
    }

    @Override
    public String getDefaultPermission() {

        return "SIGH";
    }

    @Override
    public String getDeletePermission() {

        return this.getEditPermission();
    }

    @Override
    public String getCreatePermission() {

        return this.getEditPermission();
    }

    @Override
    public String getEditPermission() {

        return this.getDefaultPermission();
    }

    /**
     * Retorna el mensaje que debe ser desplegado en el botón de cancelar.
     * 
     * @return cadena a mostrar
     */
    public String getCancelText() {

        if (this.getMode().equals(Mode.VIEW)) {
            return this.getMessage("BASE_FORM_CANCEL_VIEW");
        }
        return this.getMessage("BASE_FORM_CANCEL");

    }

    @Override
    public String getHeaderText() {

        String header;
        switch (this.getMode()) {
            case EDIT:
                header = getHeaderTextEdit();
                break;
            case VIEW:
                header = getHeaderTextView();
                break;
            case DELETE:
                header = getHeaderTextDelete();
                break;
            case NEW:
                header = getHeaderTextNew();
                break;
            default:
                header = "BREADCRUM_UNKNOWN";
                break;
        }
        String menuActual = getActualMenu();

        if (!StringUtils.isValid(menuActual)) {
            return getMessage(header);
        } else {
            if (StringUtils.isValid(header)) {
                header = getMessage(header);
            }
            return header + " " + menuActual;
        }

    }

    protected String getActualMenu() {

        Menu actual = this.currentPageHelper.getCurrentMenu();
        if (actual == null) {
            return "";
        } else {
            return actual.getName();

        }
    }

    /**
     * Retorna el key de internacionalización de la cadena a visualizar en el
     * panel del formulario al momento de crear una entidad .
     * 
     * @return "Creando"(key de internacionalización) ,luego dicha cadena será
     *         internacionalizada y concatenada con el nombre de la entidad en
     *         cuestion, supongamos que estamos creando un tipo de autorización,
     *         la cadena retornada será "Creando" y en {@link getHeaderText} se
     *         transformará a "Creando tipo de Autorización".
     * 
     * 
     */
    public String getHeaderTextNew() {

        return "BASE_FORM_NEW_HEADER";
    }

    /**
     * Retorna el key de internacionalización de la cadena a visualizar en el
     * panel del formulario al momento de crear una entidad .
     * 
     * @return "Editando" (key de internacionalización) ,luego dicha cadena será
     *         internacionalizada y concatenada con el nombre de la entidad en
     *         cuestion, supongamos que estamos editando un tipo de
     *         autorización, la cadena retornada será "Creando" y en
     *         {@link getHeaderText} se transformará a
     *         "Editando tipo de Autorización".
     * 
     * 
     */
    public String getHeaderTextEdit() {

        return "BASE_FORM_EDIT_HEADER";
    }

    /**
     * Retorna el key de internacionalización de la cadena a visualizar en el
     * panel del formulario al momento de crear una entidad .
     * 
     * @return "Eliminando" (key de internacionalización) ,luego dicha cadena
     *         será internacionalizada y concatenada con el nombre de la entidad
     *         en cuestion, supongamos que estamos eliminando un tipo de
     *         autorización, la cadena retornada será "Eliminando" y en
     *         {@link getHeaderText} se transformará a
     *         "Eliminando tipo de Autorización".
     * 
     * 
     */
    public String getHeaderTextDelete() {

        return "BASE_FORM_DELETE_HEADER";
    }

    /**
     * Retorna el key de internacionalización de la cadena a visualizar en el
     * panel del formulario al momento de visualizar una entidad .
     * 
     * @return "Visualizando" (key de internacionalización) ,luego dicha cadena
     *         será internacionalizada y concatenada con el nombre de la entidad
     *         en cuestion, supongamos que estamos visualizando un tipo de
     *         autorización, la cadena retornada será "Visualizando" y en
     *         {@link getHeaderText} se transformará a
     *         "Visualizando tipo de Autorización".
     * 
     * 
     */
    public String getHeaderTextView() {

        return "BASE_FORM_VIEW_HEADER";
    }

    @Override
    public void reloadEntities() {

        this.entities = null;
    }

    @Override
    public boolean isList() {

        return this.getMode().equals(Mode.LIST);
    }

}
