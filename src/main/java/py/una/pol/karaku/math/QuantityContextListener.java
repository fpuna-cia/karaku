/*
 * @QuantityContextListener.java 1.0 Nov 20, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.math;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context Listener que se encarga de setear las propiedades de entorno
 * necesarias para el correcto funcionamiento de la entidad Quantity.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 20, 2013
 * 
 */
public class QuantityContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		// no hacer nada al destruir el contexto
	}

}
