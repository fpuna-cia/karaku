/*
 * @WebService.java 1.0 Oct 18, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.services.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Endpoint
public @interface WebServiceDefinition {

	String[] xsds();
}
