/*
 * @LdapStressTest.java 1.0 Oct 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.security;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * Test de stress para probar el rendimiento de diferentes servidores LDAP
 * utilizados.
 *
 * <p>
 * Se debe agregar tantos servidores como se desee (en el método
 * {@link #befor()} y configurar las variables globales {@link #threads} y
 * {@link #timesPerThread}.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 25, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Ignore(value = "Ignorado por que es un test de stress.")
public class LdapStressTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	int threads = 1;
	int timesPerThread = 1;

	class LdapServer {

		String url;
		String user;
		String pass;

		public List<Thread> threads = new ArrayList<Thread>();

		public LdapServer(String ip, String user, String pass) {

			this.url = ip;
			this.user = user;
			this.pass = pass;
		}

		long begin;
		Long duration;
		private InitialDirContext context;

		public void doWithThreads(final int times, int numberofThreads,
				boolean verbose) throws InterruptedException {

			begin = System.currentTimeMillis();
			duration = 0L;
			for (int i = 0; i < numberofThreads; i++) {
				Thread thread = new Thread() {

					@Override
					public void run() {

						for (int i = 0; i < times; i++) {
							try {
								duration += doIt(times, false);
							} catch (NamingException e) {
								e.getMessage();
							}
						}
					}
				};
				thread.start();
				threads.add(thread);
			}

			while (threads.size() > 0) {
				Thread t = threads.get(0);
				t.join();
				threads.remove(t);
			}
			if (verbose) {
				log.info("{}\t\t{}\t{}\t{}\t{}",
						new Object[] {
								url,
								numberofThreads + "",
								numberofThreads * times,
								duration.doubleValue()
										/ (numberofThreads * times), duration });
			}
		}

		public long doIt(int times, boolean verbose) throws NamingException {

			long begin = System.currentTimeMillis();
			getAll(getInitialDirContext(), pass);
			long duration = System.currentTimeMillis() - begin;
			if (verbose) {
				log.info("Time: {}", duration);
			}
			return duration;
		}

		private void getAll(InitialDirContext ctx, String uid) {

			try {
				Attributes matchAttrs = new BasicAttributes(true);
				matchAttrs.put(new BasicAttribute("member", user));

				NamingEnumeration<SearchResult> answer = ctx.search(
						"ou=permissions", matchAttrs, new String[] { "cn" });

				while (answer.hasMore()) {
					SearchResult searchResult = answer.next();
					Attributes attributes = searchResult.getAttributes();
					attributes.get("cn");
				}

			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		private InitialDirContext getInitialDirContext() throws NamingException {

			if (context == null) {
				Hashtable<Object, String> env = new Hashtable<Object, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"com.sun.jndi.ldap.LdapCtxFactory");
				env.put(Context.PROVIDER_URL, url.trim()
						+ "/dc=med,dc=una,dc=py");
				env.put(Context.SECURITY_PRINCIPAL, user);
				env.put(Context.SECURITY_CREDENTIALS, pass);
				context = new InitialDirContext(env);
			}
			return context;

		}
	}

	private ArrayList<LdapServer> servers;

	private Logger log = LoggerFactory.getLogger(LdapStressTest.class);

	@Before
	public void befor() {

		servers = new ArrayList<LdapServer>();

		servers.add(new LdapServer("ldap://test-cia01.pol.una.py",
				"uid=root,ou=users,dc=med,dc=una,dc=py", "pass"));
		servers.add(new LdapServer("ldap://presec.med.una.py",
				"uid=root,ou=users,dc=med,dc=una,dc=py", "")); // Poner el
																// password
		servers.add(new LdapServer("ldap://sec.med.una.py\t",
				"uid=root,ou=users,dc=med,dc=una,dc=py", "")); // Poner el
																// password
		servers.add(new LdapServer("ldap://devel-cia01.pol.una.py",
				"uid=root,ou=users,dc=med,dc=una,dc=py", "pass"));
	}

	@Test
	public void doIt() throws InterruptedException {

		log.info("Server\t\t\t\t\tThreads\tCount\tAverage\tTotal");
		for (LdapServer ls : servers) {
			ls.doWithThreads(timesPerThread, threads, true);
		}
	}

}
