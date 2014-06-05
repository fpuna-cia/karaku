/*
 * 
 */
package py.una.pol.karaku.exception;

/**
 * 
 * @author Uriel González
 * @since 1.0
 * @version 1.0 May 06, 2013
 * 
 */
public class SimpleFilterNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SimpleFilterNotFoundException() {

		super("Llamado a simpleFilter sin filtro simple!");
	}

}
