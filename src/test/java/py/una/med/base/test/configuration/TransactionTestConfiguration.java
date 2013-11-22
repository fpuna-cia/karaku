/*
 * @TransactionBaseTestConfiguration.java 1.0 Aug 19, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.test.configuration;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.envers.Audited;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import py.una.med.base.configuration.KarakuPersistence;
import py.una.med.base.dao.entity.interceptors.BigDecimalInterceptor;
import py.una.med.base.dao.entity.interceptors.CaseSensitiveInterceptor;
import py.una.med.base.dao.entity.interceptors.InterceptorHandler;
import py.una.med.base.dao.entity.interceptors.TimeInterceptor;
import py.una.med.base.dao.entity.interceptors.UriInterceptor;
import py.una.med.base.dao.entity.watchers.WatcherHandler;
import py.una.med.base.dao.helper.AndExpressionHelper;
import py.una.med.base.dao.helper.BetweenExpressionHelper;
import py.una.med.base.dao.helper.EqualExpressionHelper;
import py.una.med.base.dao.helper.GeExpressionHelper;
import py.una.med.base.dao.helper.LeExpressionHelper;
import py.una.med.base.dao.helper.LikeExpressionHelper;
import py.una.med.base.dao.helper.NotExpressionHelper;
import py.una.med.base.dao.helper.NumberLikeExpressionHelper;
import py.una.med.base.dao.helper.OrExpressionHelper;
import py.una.med.base.dao.helper.RestrictionHelper;
import py.una.med.base.dao.util.MainInstanceHelper;
import py.una.med.base.dao.where.DateClauses;
import py.una.med.base.exception.KarakuPropertyNotFoundException;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Clases de persistencia para los test, sus anotaciones no se heredan.
 * <p>
 * Crea por defecto una base de datos H2, sin ningún dato, solamente las
 * entidades definidas en {@link #getBasePackageToScan()} o
 * {@link #getEntityClasses()}.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 *
 */
public class TransactionTestConfiguration extends BaseTestConfiguration {

	/**
	 * Cadena que representa el valor negativo, es decir, si esta cadena esta
	 * presente como valor de una propiedad, entonces será evaluada como
	 * <code>false</code>
	 */
	private static final String STRING_FALSE = "false";

	private static final String USE_EMBEDDED = "test.hibernate.use_embedded";

	/**
	 * Crea un datasource para una base de datos embebida
	 *
	 * @return dataSource creada o null si no se necesita un datasource
	 * @throws IOException
	 *             si no se puede crear la base de datos
	 */
	@Bean
	public DataSource dataSource() throws IOException {

		DataSource ds;
		if (properties.getBoolean(USE_EMBEDDED, true)) {
			EmbeddedDatabaseBuilder edb = new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2);
			ds = edb.build();

		} else {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setUrl(properties.get("database.url"));
			dataSource.setUsername(properties.get("database.user"));
			dataSource.setPassword(properties.get("database.password"));
			ds = dataSource;

		}
		return ds;

	}

	/**
	 * Retorna un {@link SessionFactory} para los test, utilizando el
	 * {@link DataSource} definido por {@link #dataSource()}.
	 *
	 * @return {@link SessionFactory}
	 * @throws IOException
	 *             si no puede leer el datasource
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory() throws IOException {

		LocalSessionFactoryBean bean = new KarakuPersistence.KarakuLocalSessionFactoryBean();
		Class<?>[] annonClasses = getEntityClasses();
		if (annonClasses == null) {
			bean.setPackagesToScan(this.getBasePackageToScan());
		} else {
			bean.setAnnotatedClasses(annonClasses);
		}

		bean.setDataSource(this.dataSource());
		Properties props = new Properties();
		try {
			if (properties.getBoolean(USE_EMBEDDED, true)) {
				props.put("hibernate.dialect", properties.get(
						"test.hibernate.dialect",
						"org.hibernate.dialect.H2Dialect"));
				props.put("hibernate.hbm2ddl.auto", "create-drop");
			} else {
				props.put("hibernate.dialect",
						properties.get("hibernate.dialect"));
				props.put("hibernate.hbm2ddl.auto",
						properties.get("hibernate.hbm2ddl.auto", "validate"));
			}
			props.put("hibernate.show_sql",
					properties.get("hibernate.show_sql", STRING_FALSE));
			props.put("hibernate.format_sql",
					properties.get("hibernate.format_sql", STRING_FALSE));
			props.put("hibernate.listeners.envers.autoRegister",
					getWithEnvers());
		} catch (KarakuPropertyNotFoundException kpnfe) {
			throw new KarakuRuntimeException(
					"Please check the properties file", kpnfe);
		}
		bean.setHibernateProperties(props);
		return bean;

	}

	/**
	 * Debe estar activado envers?.
	 *
	 * @return <code>true</code> si se desea que se autoregistren las entidades
	 *         con {@link Audited}, <code>false</code> en caso contrario.
	 */
	protected boolean getWithEnvers() {

		return false;
	}

	/**
	 * Retorna la lista de paquetes que serán exploradas por esta
	 * configuración.
	 * <p>
	 * Por defecto utiliza la propiedad <code>base-package-hibernate</code> del
	 * archivo de propiedades.
	 * </p>
	 *
	 * @return lista de paquetes, si retorna <code>null</code>, el método
	 *         {@link #getEntityClasses()} debe retornar algo.
	 */
	public String[] getBasePackageToScan() {

		return properties.get("base-package-hibernate").split("\\s+");
	}

	/**
	 * Retorna la lista de paquetes que serán exploradas por esta
	 * configuración. Si este método no retorna <code>null</code>, entonces el
	 * método {@link #getBasePackageToScan()} es omitido.
	 * <p>
	 * Por defecto retorna null.
	 * </p>
	 *
	 *
	 * @return lista de entidades, si retorna <code>null</code>, el método
	 *         {@link #getBasePackageToScan()} debe retornar algo.
	 */
	public Class<?>[] getEntityClasses() {

		return null;
	}

	@Bean
	HibernateTransactionManager transactionManager() throws IOException {

		return new HibernateTransactionManager(this.sessionFactory()
				.getObject());
	}

	@Bean
	RestrictionHelper restrictionHelper() {

		return new RestrictionHelper();
	}

	@Bean
	LikeExpressionHelper likeExpressionHelper() {

		return new LikeExpressionHelper();
	}

	@Bean
	NumberLikeExpressionHelper numberLikeExpressionHelper() {

		return new NumberLikeExpressionHelper();
	}

	@Bean
	OrExpressionHelper orExpressionHelper() {

		return new OrExpressionHelper();
	}

	@Bean
	NotExpressionHelper notExpressionHelper() {

		return new NotExpressionHelper();
	}

	@Bean
	AndExpressionHelper andExpressionHelper() {

		return new AndExpressionHelper();
	}

	@Bean
	LeExpressionHelper leExpressionHelper() {

		return new LeExpressionHelper();
	}

	@Bean
	GeExpressionHelper geExpressionHelper() {

		return new GeExpressionHelper();
	}

	@Bean
	EqualExpressionHelper equalExpressionHelper() {

		return new EqualExpressionHelper();
	}

	@Bean
	BetweenExpressionHelper betweenExpressionHelper() {

		return new BetweenExpressionHelper();
	}

	@Bean
	DateClauses dateClauses() {

		return new DateClauses();
	}

	@Bean
	InterceptorHandler interceptorHandler() {

		return new InterceptorHandler();
	}

	@Bean
	TimeInterceptor timeInterceptor() {

		return new TimeInterceptor();
	}

	@Bean
	BigDecimalInterceptor bigDecimalInterceptor() {

		return new BigDecimalInterceptor();
	}

	@Bean
	CaseSensitiveInterceptor caseSensitiveInterceptor() {

		return new CaseSensitiveInterceptor();
	}

	@Bean
	MainInstanceHelper mainInstanceHelper() {

		return new MainInstanceHelper();
	}

	@Bean
	UriInterceptor uriInterceptor() {

		return new UriInterceptor();
	}

	@Bean
	WatcherHandler watcherHandler() {

		return new WatcherHandler();
	}

}
