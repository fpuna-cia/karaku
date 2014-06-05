/*
 * @KarakuServiceTest.java 1.0 Oct 24, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.security.KarakuUser;
import py.una.pol.karaku.security.KarakuUserService;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 24, 2013
 * 
 */
@Ignore("Para que se pueda ejecutar sin la existencia de un LDAP")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class KarakuServiceTest extends BaseTest {

	@Configuration
	@EnableTransactionManagement()
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		KarakuUserService sIGHUserService() {

			return new KarakuUserService();
		}
	}

	@Log
	private Logger log;

	@Autowired
	private KarakuUserService userService;

	@Test
	public void testAuthenticate() throws Exception {

		assertTrue("No se puede iniciar sesión al LDAP",
				userService.checkAuthenthicationByUID("root", "pass"));
		assertFalse("Acepta usuario aleatorio",
				userService.checkAuthenthication("-564-54-456-3-4567-5456",
						"pass"));
	}

	@Test(expected = InsufficientAuthenticationException.class)
	public void testName() throws Exception {

		userService.loadUserByUsername("tincho2");
	}

	@Test
	public void testLoadAuthorities() {

		KarakuUser user = (KarakuUser) userService
				.loadUserByUsername("root");
		assertFalse("root No tiene permisos?", user.getAuthorities().isEmpty());
	}

}
