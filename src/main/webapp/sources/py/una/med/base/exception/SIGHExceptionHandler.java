/*
 * @SIGHExceptionHandler.java 1.0 07/01/13
 */

package py.una.med.base.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase provee comportamiento especializado a una instancia
 * ExceptionHandler existente.
 * 
 * @author Uriel González
 * @version 1.0, 07/01/13
 * @since 1.0
 */
public class SIGHExceptionHandler extends ExceptionHandlerWrapper {

	static final Logger logger = LoggerFactory
			.getLogger(SIGHExceptionHandler.class);

	private ExceptionHandler wrapped;

	public SIGHExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() throws FacesException {
		// itera sobre todas las excepciones no controladas
		Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents()
				.iterator();
		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
					.getSource();

			// obtiene un throwable object
			Throwable t = context.getException();

			try {
				String path = FacesContext.getCurrentInstance()
						.getExternalContext().getRequestContextPath();
				boolean error = true;
				// iteramos para encontrar todas las causas del error
				while (t != null) {
					// manegamos la excepcion

					if (t instanceof org.springframework.security.access.AccessDeniedException) {
						// redirigimos al error view etc....
						printLog(t);
						FacesContext
								.getCurrentInstance()
								.getExternalContext()
								.redirect(
										path
												+ "/faces/views/errors/accessDenied.xhtml");
						error = false;
					}
					t = t.getCause();
				}
				if (error) {
					// no se ha tratado aun ningun error
					printLog(context.getException());
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect(path + "/faces/views/errors/error.xhtml");
				}

			} catch (Exception e) {
				logger.error("Se ha producido un error al manejar el error");
			} finally {
				// despues que la excepcion es controlada, la removemos de la
				// cola
				iterator.remove();
			}
		}
		// no dejamos al parent handler manejar el resto debido a que no
		// visualizaría la pagina de error
		// getWrapped().handle();

	}

	/**
	 * Imprime en el log de la aplicación el mensaje de error y el stack trace
	 * del mismo.
	 * 
	 * @param t
	 *            Excepción lanzada.
	 */
	private void printLog(Throwable t) {
		// obtiene el stackTrace
		StackTraceElement stackTrace[] = t.getStackTrace();

		String temp = t.getMessage() + "" + ". Stack Trace: \n\t\t\t\t";
		for (StackTraceElement stackTraceElement : stackTrace) {
			temp += stackTraceElement.toString() + "\n\t\t\t\t";
		}
		logger.error(temp);
	}

}