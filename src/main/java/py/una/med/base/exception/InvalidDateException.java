package py.una.med.base.exception;

/**
 * Excepción lanzada cuando se usa una fecha invalida.
 *
 * @author Uriel González
 *
 */
public class InvalidDateException extends Exception {

	private static final long serialVersionUID = 1L;

	private String field;

	public InvalidDateException(String field) {

		super("Invalid date in " + field);
		this.field = field;
	}

	public InvalidDateException(String field, Throwable cause) {

		super(field, cause);
		this.field = field;
	}

	public String getField() {

		return this.field;
	}

}
