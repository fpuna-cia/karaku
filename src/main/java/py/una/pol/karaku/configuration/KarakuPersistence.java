/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.configuration;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.dao.entity.types.QuantityCustomType;
import py.una.pol.karaku.exception.KarakuPropertyNotFoundException;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.math.Quantity;

/**
 * Clase de configuración de la persistencia
 * 
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.0
 * 
 */
@Configuration
@EnableTransactionManagement()
public class KarakuPersistence {

	/**
	 * Define si la persistencia esta activa.
	 */
	public static final String KARAKU_JPA_ENABLED = "karaku.jpa.enabled";

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

	private static final Logger LOG = LoggerFactory
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
		if (this.enabled) {
			dataSource = new DriverManagerDataSource();
			dataSource.setUrl(this.properties.get("database.url"));
			dataSource.setUsername(this.properties.get("database.user"));
			dataSource.setPassword(this.properties.get("database.password"));
		}

		return dataSource;
	}

	@Autowired
	public void setPropertiesUtil(PropertiesUtil propertiesUtil) {

		this.properties = propertiesUtil;
		this.checkState();
	}

	/**
	 * Verifica si esta activado JPA y si no es así lo deshabilita.
	 */
	private void checkState() {

		if (this.properties.getBoolean(KARAKU_JPA_ENABLED, true)) {
			this.enabled = true;
		} else {
			LOG.info("Karaku JPA support is disabled");
			this.enabled = false;
			LOG.info("Karaku Liquibase support is disabled");
			this.liquibase = false;
			return;
		}
		if (this.properties.getBoolean("karaku.liquibase.enabled", true)) {
			this.liquibase = true;
		} else {
			LOG.info("Karaku Liquibase support is disabled");
			this.liquibase = false;
		}
	}

	/**
	 * Este método carga el driver en el classpath, no debería ser necesario
	 * según la especificación, pero normalmente es definido.
	 */
	public void loadDriver() {

		try {
			Class.forName(this.properties.get(DRIVER_PROPS));
		} catch (ClassNotFoundException cnfe) {
			throw new KarakuRuntimeException(String.format(
					"No se puede cargar driver %s.", DRIVER_PROPS), cnfe);
		}
	}

	@SuppressWarnings("unchecked")
	@Bean
	public Object liquibase() {

		if (!this.liquibase) {
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
					this.dataSource());
			clazz.getMethod("setChangeLog", String.class).invoke(o,
					this.properties.get("liquibase.changelog-file"));
			return o;
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					"Wrong version of liquibase, please, check your pom", e);
		}

	}

	@Bean
	@DependsOn("liquibase")
	public LocalSessionFactoryBean sessionFactory() {

		if (!this.enabled) {
			return null;
		}
		LocalSessionFactoryBean bean = new KarakuLocalSessionFactoryBean();
		bean.setPackagesToScan(this.properties.get("base-package-hibernate")
				.split("\\s+"));
		bean.setDataSource(this.dataSource());

		Properties props = new Properties();
		try {
			props.put("hibernate.dialect",
					this.properties.get("hibernate.dialect"));
			props.put("hibernate.temp.use_jdbc_metadata_defaults", STRING_FALSE);
			props.put("hibernate.hbm2ddl.auto",
					this.properties.get("hibernate.hbm2ddl.auto", "validate"));
			props.put("hibernate.show_sql",
					this.properties.get("hibernate.show_sql", STRING_FALSE));
			props.put("hibernate.format_sql",
					this.properties.get("hibernate.format_sql", STRING_FALSE));
		} catch (KarakuPropertyNotFoundException kpnfe) {
			throw new KarakuRuntimeException(
					"Please check the properties file", kpnfe);
		}
		bean.setHibernateProperties(props);

		return bean;

	}

	@Bean
	public HibernateTransactionManager transactionManager() {

		if (this.enabled) {
			return new HibernateTransactionManager(this.sessionFactory()
					.getObject());
		}
		return null;
	}

	/**
	 * SessionFactory que se encarga de registrar los tipos de Karaku.
	 * 
	 * <p>
	 * Intercepta la creación del session factory y entre la creación de
	 * configuración y la función que efectivamente crea la
	 * {@link SessionFactory} registra los tipos personalizados.
	 * </p>
	 * 
	 * @author Arturo Volpe
	 * @since 2.2.8
	 * @version 1.0 Oct 15, 2013
	 * 
	 */
	public static class KarakuLocalSessionFactoryBean extends
			LocalSessionFactoryBean {

		@Override
		protected SessionFactory buildSessionFactory(
				LocalSessionFactoryBuilder sfb) {

			getConfiguration().registerTypeOverride(
					QuantityCustomType.INSTANCE,
					new String[] { Quantity.class.getName() });
			return super.buildSessionFactory(sfb);
		}
	}
}
