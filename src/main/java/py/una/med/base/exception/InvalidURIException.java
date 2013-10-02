package py.una.med.base.exception;

/**
 * 
 * Excepción lanzada cuando se usa una URI invalida.
 * 
 * @author Uriel González
 * 
 */
public class InvalidURIException extends Exception {

	private static final long serialVersionUID = 1L;

	private String field;

	public InvalidURIException(String field) {

		super("Invalid URI in " + field);
		this.field = field;
	}

	public String getField() {

		return this.field;
	}

}
