/*
 * @KarakuJSFExceptionHandlerFactory.java 1.0 07/01/13
 */
package py.una.pol.karaku.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Esta clase es una factoria que crea (si necesita) y retorna una nueva
 * instancia ExceptionHandler.
 * 
 * @author Uriel González
 * @version 1.0, 07/01/13
 * @since 1.0
 */
public class KarakuJSFExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	public KarakuJSFExceptionHandlerFactory(ExceptionHandlerFactory parent) {

		this.parent = parent;
	}

	// creamos nuestro propio ExceptionHandler
	@Override
	public ExceptionHandler getExceptionHandler() {

		ExceptionHandler result = new KarakuJSFExceptionHandler(
				parent.getExceptionHandler());
		return result;
	}
}
