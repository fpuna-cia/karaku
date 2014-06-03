package py.una.pol.karaku.exception;

/**
 * Excepción lanzada cuando se produce una violación de restricción de not null.
 * 
 * @author Uriel González
 * 
 */
public class NotNullConstraintException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String column;

	public NotNullConstraintException(String column) {

		super("Not null constraint violated. Field " + column);
		this.column = column;

	}

	public String getColumn() {

		return column;
	}

}
