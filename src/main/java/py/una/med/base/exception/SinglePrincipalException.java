/*
 * @SinglePrincipalException.java 1.0 Mar 20, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.exception;


/**
 * 
 * @author Diego Acuña 
 * @since 1.0
 * @version 1.0 Abr 18, 2013
 * 
 */
public class SinglePrincipalException extends RuntimeException {
	
	private static final long serialVersionUID = -154211713138448380L;

	public SinglePrincipalException(String key) {

		super(key);
	}

}
