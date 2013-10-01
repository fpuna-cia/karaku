/*
 * @SIGHExceptionHandler.java 1.0 07/01/13
 */

package py.una.med.base.exception;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.util.Util;

/**
 * Esta clase provee comportamiento especializado a una instancia
 * ExceptionHandler existente.
 * 
 * @author Arturo Volpe
 * @author Uriel González
 * @version 1.1, 13/08/13
 * @since 1.0
 */
public class SIGHExceptionHandler extends ExceptionHandlerWrapper {

	private static final String ACCESS_DENIED_KEY = "karaku.exception.access_denied.page";
	private static final String ACCESS_DENIED_DEFAULT = "/faces/views/errors/accessDenied.xhtml";
	private static final String EXCEPTION_KEY = "karaku.exception.error.page";
	private static final String EXCEPTION_DEFAULT = "/faces/views/errors/error.xhtml";

	static final Logger LOG = LoggerFactory
			.getLogger(SIGHExceptionHandler.class);

	private final ExceptionHandler wrapped;

	public SIGHExceptionHandler(ExceptionHandler wrapped) {

		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {

		return this.wrapped;
	}

	@Override
	public void handle() {

		if (this.isDevelop()) {
			this.wrapped.handle();
			return;
		}
		// itera sobre todas las excepciones no controladas
		Iterator<ExceptionQueuedEvent> iterator = this.getUnhandledExceptionQueuedEvents()
				.iterator();
		while (iterator.hasNext()) {
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
					.getSource();

			// obtiene un throwable object
			Throwable t = context.getException();

			try {

				// iteramos para encontrar todas las causas del error
				while (t != null) {
					// manegamos la excepcion

					if (t instanceof org.springframework.security.access.AccessDeniedException) {
						// redirigimos al error view etc....
						this.accessDenied(t);
						return;
					}
					t = t.getCause();
				}
				// no se ha tratado aun ningun error
				this.error(context.getException());

			} catch (Exception e) {
				LOG.error("Se ha producido un error al manejar el error", e);
			} finally {
				// despues que la excepcion es controlada, la removemos de la
				// cola
				iterator.remove();
			}
		}
		// no dejamos al parent handler manejar el resto debido a que no
		// visualizaría la página de error para hacer esto invocar
		// getWrapped()#handle

	}

	private void accessDenied(Throwable t) throws IOException {

		this.printLog(t);
		this.redirectTo(PropertiesUtil.getCurrentFromJSF().get(ACCESS_DENIED_KEY,
				ACCESS_DENIED_DEFAULT));
	}

	private void error(Throwable t) throws IOException {

		this.printLog(t);
		this.redirectTo(PropertiesUtil.getCurrentFromJSF().get(EXCEPTION_KEY,
				EXCEPTION_DEFAULT));
	}

	private void redirectTo(String url) throws IOException {

		String path = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath();
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(path + url);
	}

	/**
	 * Imprime en el log de la aplicación el mensaje de error y el stack trace
	 * del mismo.
	 * 
	 * @param t
	 *            Excepción lanzada.
	 */
	private void printLog(Throwable t) {

		LOG.error(t.getMessage(), t);
	}

	private boolean isDevelop() {

		FacesContext context = FacesContext.getCurrentInstance();
		return Util.getSpringBeanByJSFContext(context, Util.class).isDevelop();
	}
}
