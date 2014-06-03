/*
 * @NoSearchParamException.java 1.0 Abr 26, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.exception;

/**
 * 
 * @author Diego Acuña
 * @since 1.0
 * @version 1.0 Abr 26, 2013
 * 
 */
public class NoSearchParamException extends RuntimeException {
	
	private static final long serialVersionUID = -154211713138448380L;

	public NoSearchParamException (String key) {

		super(key);
	}
	
}
