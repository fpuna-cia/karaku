/**
 * @SIGHConfiguration 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.configuration;

import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.exception.KarakuPropertyNotFoundException;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Clase de configuración de la persistencia
 * 
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.0
 * 
 @HasRole("test") public String getTemp() { return helper.
 */
@Configuration
@EnableTransactionManagement()
public class KarakuPersistence {

	/**
	 * Cadena que representa el valor positivo, es decir, si esta cadena esta
	 * presente como valor de una propiedad, entonces será evaluada como
	 * <code>true</code>
	 */
	private static final String STRING_TRUE = "true";

	/**
	 * Cadena que representa el valor negativo, es decir, si esta cadena esta
	 * presente como valor de una propiedad, entonces será evaluada como
	 * <code>false</code>
	 */
	private static final String STRING_FALSE = "false";

	/**
	 * Llave donde se almacena el driver a ser cargado
	 */
	private static final String DRIVER_PROPS = "karaku.jpa.driverName";

	private static final Logger log = LoggerFactory
			.getLogger(KarakuPersistence.class);

	private PropertiesUtil properties;

	private boolean enabled;
	private boolean liquibase;

	/**
	 * Crea un datasource con los valores definidos en el karaku.properties.
	 * 
	 * @return dataSource creada o null si no se necesita un datasource
	 */
	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource dataSource = null;
		if (enabled) {
			dataSource = new DriverManagerDataSource();
			dataSource.setUrl(properties.getProperty("database.url"));
			dataSource.setUsername(properties.getProperty("database.user"));
			dataSource.setPassword(properties.getProperty("database.password"));
		}

		return dataSource;
	}

	@Autowired
	public void setPropertiesUtil(PropertiesUtil propertiesUtil) {

		this.properties = propertiesUtil;
		checkState();
	}

	/**
	 * Verifica si esta activado JPA y si no es asi lo desabilita
	 */
	private void checkState() {

		if (properties.get("karaku.jpa.enabled", STRING_TRUE).trim()
				.equals(STRING_FALSE)) {
			log.info("Karaku JPA support is disabled");
			enabled = false;
			log.info("Karaku Liquibase support is disabled");
			liquibase = false;
			return;
		} else {
			enabled = true;

		}
		if (properties.get("karaku.liquibase.enabled", STRING_TRUE).trim()
				.equals(STRING_FALSE)) {
			log.info("Karaku JPA support is disabled");
			liquibase = false;
		} else {
			liquibase = true;
		}
	}

	/**
	 * Este método carga el driver en el classpath, no debería ser necesario
	 * según la especificación, pero normalmente es definido.
	 */
	public void loadDriver() {

		try {
			Class.forName(properties.getProperty(DRIVER_PROPS));
		} catch (ClassNotFoundException cnfe) {
			throw new KarakuRuntimeException(String.format(
					"No se puede cargar driver %s.", DRIVER_PROPS), cnfe);
		}
	}

	@SuppressWarnings("unchecked")
	@Bean
	public Object liquibase() {

		if (!liquibase) {
			return null;
		}
		@SuppressWarnings("rawtypes")
		Class clazz = null;
		try {
			clazz = Class
					.forName("liquibase.integration.spring.SpringLiquibase");
		} catch (ClassNotFoundException e) {
			throw new KarakuRuntimeException(
					"Can not find the Liquibase base class in the classpath, please, check your pom",
					e);
		}
		Object o;
		try {
			o = clazz.newInstance();
			clazz.getMethod("setDataSource", DataSource.class).invoke(o,
					dataSource());
			clazz.getMethod("setChangeLog", String.class).invoke(o,
					properties.getProperty("liquibase.changelog-file"));
			return o;
		} catch (ReflectiveOperationException e) {
			throw new KarakuRuntimeException(
					"Wrong version of liquibase, please, check your pom", e);
		}

	}

	@Bean
	@DependsOn("liquibase")
	public LocalSessionFactoryBean sessionFactory() {

		if (!enabled) {
			return null;
		}
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setPackagesToScan(properties.getProperty("base-package-hibernate"));
		bean.setDataSource(dataSource());
		Properties props = new Properties();
		try {
			props.put("hibernate.dialect", properties.get("hibernate.dialect"));
			props.put("hibernate.hbm2ddl.auto",
					properties.get("hibernate.hbm2ddl.auto", "validate"));
			props.put("hibernate.show_sql",
					properties.get("hibernate.show_sql", STRING_FALSE));
			props.put("hibernate.format_sql",
					properties.get("hibernate.format_sql", STRING_FALSE));
		} catch (KarakuPropertyNotFoundException kpnfe) {
			throw new KarakuRuntimeException(
					"Please check the properties file", kpnfe);
		}
		bean.setHibernateProperties(props);
		return bean;

	}

	@Bean
	public HibernateTransactionManager transactionManager() {

		if (enabled) {
			return new HibernateTransactionManager(sessionFactory().getObject());
		} else {
			return null;
		}
	}
}
