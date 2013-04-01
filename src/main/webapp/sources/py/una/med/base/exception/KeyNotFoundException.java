/*
 * @KeyNotFoundException.java 1.0 Mar 20, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.exception;


/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 20, 2013
 * 
 */
public class KeyNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -154211713138448380L;

	public KeyNotFoundException(String key) {

		super(String.format("String not found in the current bundles: %s", key));
	}
}
