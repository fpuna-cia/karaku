package py.una.med.base.exception;

/**
 * Describe una excepcion que ocurre durante el procesamiento de solicitudes
 * HTTP
 * 
 * @author Uriel Gonzalez
 * 
 */
public class HTTPException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String code;
	private final String shortDesciption;

	/**
	 * 
	 * @param code
	 *            Codigos de estatus especificados en el RFC 2616
	 */
	public HTTPException(String code, String shortDescription) {

		this.code = code;
		this.shortDesciption = shortDescription;
	}

	public HTTPException(String code, String shortDescription, Throwable cause) {

		super(cause);
		this.code = code;
		this.shortDesciption = shortDescription;
	}

	/**
	 * Recupera el codigo de estado de HTTP
	 * 
	 * @return codigo de estado
	 */
	public String getCode() {

		return this.code;
	}

	public String getShortDescription() {

		return this.shortDesciption;
	}

}
