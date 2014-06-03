package py.una.pol.karaku.exception;

import java.util.List;
import py.una.pol.karaku.util.ListHelper;
import py.una.pol.karaku.util.UniqueHelper.UniqueRestrintion;

/**
 * Excepción que encapsula una {@link UniqueRestrintion} para poder recuperar la
 * Información relevante de la excepción.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 19, 2013
 * 
 */
public class UniqueConstraintException extends Exception {

	private static final long serialVersionUID = 4209534069171968008L;
	private final List<String> fields;
	private final String uniqueConstraintName;
	private static final String UNIQUECONSTRAINTVIOLATED = "Unique constraint violated";

	/**
	 * Construye una excepción a partir de los datos valiosos del
	 * {@link UniqueRestrintion}
	 * 
	 * @param item
	 */
	public UniqueConstraintException(final UniqueRestrintion item) {

		super(UNIQUECONSTRAINTVIOLATED + item.getUniqueConstraintName());
		fields = item.getFields();
		uniqueConstraintName = item.getUniqueConstraintName();
	}

	public UniqueConstraintException(final String restriction,
			final String ... uniqueFields) {

		super(UNIQUECONSTRAINTVIOLATED + restriction);
		this.fields = ListHelper.getAsList(uniqueFields);
		uniqueConstraintName = restriction;

	}

	/**
	 * Retorna la lista de fields afectados por esta ejecución
	 * 
	 * @return fields
	 */
	public List<String> getFields() {

		return fields;
	}

	/**
	 * Retorna el nombre del constraint que impido la ejecución.
	 * 
	 * @return uniqueConstraintName
	 */
	public String getUniqueConstraintName() {

		return uniqueConstraintName;
	}
}
