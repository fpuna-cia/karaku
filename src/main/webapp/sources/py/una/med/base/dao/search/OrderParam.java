/**
 * @OrdenParam 1.0 23/10/2012
 */
package py.una.med.base.dao.search;

/**
 * Clase que representa una ordenacion de una consulta
 * 
 * @author Arturo Volpe
 * @version 1.0, 23/10/2012
 * @since 1.0
 */
public class OrderParam {

	private boolean asc;

	private String columnName;

	/**
	 * Crea una nueva instancia, para el nombre de la columna (tipicamente el
	 * nombre del atributo de la entidad), y un orden definido por el boolean
	 * asc
	 * 
	 * @param asc
	 * @param columnName
	 */
	public OrderParam(boolean asc, String columnName) {

		super();
		this.asc = asc;
		this.columnName = columnName;
	}

	/**
	 * Consulta si es ascendente
	 * 
	 * @return
	 */
	public boolean isAsc() {

		return asc;
	}

	/**
	 * Indica que la columna se ordenara ascendentemnete, false para ordenar
	 * descendentemente
	 * 
	 * @param asc
	 */
	public void setAsc(boolean asc) {

		this.asc = asc;
	}

	/**
	 * Nombre de la columna a ser ordenada, tipicamente el nombre del atributo
	 * de la entidad
	 * 
	 * @return
	 */
	public String getColumnName() {

		return columnName;
	}

	/**
	 * Indica que columna sera ordenada por este ordenParam
	 * 
	 * @param columnName
	 */
	public void setColumnName(String columnName) {

		this.columnName = columnName;
	}
}
