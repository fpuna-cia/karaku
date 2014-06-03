/**
 * @OrdenParam 1.0 23/10/2012
 */
package py.una.pol.karaku.dao.search;

import javax.annotation.Nonnull;

/**
 * Clase que representa una ordenacion de una consulta
 * 
 * @author Arturo Volpe
 * @version 1.0, 23/10/2012
 * @since 1.0
 */
public class OrderParam {

	private boolean asc;

	@Nonnull
	private String columnName;

	/**
	 * Crea una nueva instancia, para el nombre de la columna (tipicamente el
	 * nombre del atributo de la entidad), y un orden definido por el boolean
	 * asc
	 * 
	 * @param asc
	 * @param columnName
	 */
	public OrderParam(boolean asc, @Nonnull String columnName) {

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
	@Nonnull
	public String getColumnName() {

		return columnName;
	}

	/**
	 * Indica que columna sera ordenada por este ordenParam
	 * 
	 * @param columnName
	 */
	public void setColumnName(@Nonnull String columnName) {

		this.columnName = columnName;
	}
}
