package py.una.pol.karaku.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.log.Log;

/**
 * Componente de PrimeFaces para realizar LazyLoad de la base de datos e
 * implementa Ordenación y Filtro por columna de las entidades.
 * 
 * 
 * @author Saúl Zalimben
 * @since 1.0
 * @version 1.0 15/01/2015
 */
@Component
public class LazyModel<T, K extends Serializable> extends LazyDataModel<T> {

    protected IKarakuBaseLogic<T, K> logic;

    @Log
    protected Logger log;

    private static final long serialVersionUID = 1L;

    public LazyModel() {

        // Constructor
    }

    /*
     * Setea el logic que será utilizado según la entidad que se desea listar
     * aplicando el LazyModel.
     */
    public void setLogic(IKarakuBaseLogic<T, K> logic) {

        this.logic = logic;
    }

    /**
     * Método que realiza el proceso de LazyLoad desde la base de datos donde
     * recibe los parametros que filtran el query.
     * 
     * @return Lista de entidades filtradas por la ordenación y filtro
     *         correspondiente
     * 
     * @see LazyDataModel<T>
     */
    @Override
    public List<T> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, Object> filters) {

        Where<T> where = getMoreWheres();
        ISearchParam params = new SearchParam();

        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String filterProperty = entry.getKey();
                Object filterValue = filters.get(filterProperty);
                if (filterValue instanceof Number) {
                    where.addClause(Clauses.numberLike(filterProperty,
                            String.valueOf(filterValue)));
                } else {
                    where.addClause(Clauses.iLike(filterProperty, filterValue));
                }
            }
        }

        if (sortField != null) {
            params.addOrder(sortField, SortOrder.ASCENDING.equals(sortOrder));
        }

        params.setOffset(first);
        params.setLimit(pageSize);

        List<T> entities = logic.getAll(where, params);

        this.setRowCount(logic.getCount(where).intValue());
        this.setPageSize(pageSize);

        return entities;
    }

    /***
     * Este método debe ser sobrescrito por los controladores que requieran
     * filtros especiales en las grillas. Debe retornar un {link <Where>} que
     * contenga todos los filtros requeridos.
     * 
     * @return {link <Where>}
     */
    public Where<T> getMoreWheres() {

        return new Where<T>();
    }

}
