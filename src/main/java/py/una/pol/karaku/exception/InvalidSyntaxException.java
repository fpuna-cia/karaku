package py.una.med.base.exception;

/**
 * Excepción lanzada cuando se produce un error de sintaxis.
 * 
 * @author Uriel González
 * 
 */
public class InvalidSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Construye un InvalidSyntaxException con el mensaje de detalle
	 * especificado.
	 * 
	 * @param message
	 *            el mensaje de detalle
	 */
	public InvalidSyntaxException(String message) {

		super(message);
	}

}
