package py.una.med.base.exception;

/**
 * 
 * @author Uriel González
 * @since 1.0
 * @version 1.0 May 07, 2013
 * 
 */
public class NotDisplayNameException extends KarakuRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -589396111044225598L;

	public NotDisplayNameException() {

		super("The field dont hava a DisplayName");
	}
}
