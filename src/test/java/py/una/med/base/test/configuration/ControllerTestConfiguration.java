/*
 * @TestConfiguration.java 1.0 Aug 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.configuration;

import javax.el.ValueExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import py.una.med.base.breadcrumb.BreadcrumbController;
import py.una.med.base.business.reports.SIGHBaseReportSimple;
import py.una.med.base.dao.search.SearchHelper;
import py.una.med.base.jsf.utils.ICurrentpageHelper;
import py.una.med.base.reports.DynamicUtils;
import py.una.med.base.security.AuthorityController;
import py.una.med.base.test.util.TestControllerHelper;
import py.una.med.base.test.util.TestExportReport;
import py.una.med.base.util.ELHelper;
import py.una.med.base.util.UniqueHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * 
 */
@Configuration
@Profile(ControllerTestConfiguration.TEST_PROFILE)
public class ControllerTestConfiguration extends BaseTestConfiguration {

	@Bean
	TestControllerHelper controllerHelper() {

		return new TestControllerHelper();
	}

	/**
	 * Retorna un {@link ELHelper} dummy.
	 * 
	 * <p>
	 * Esto permite la creación de pickers en test
	 * </p>
	 * 
	 * @return dummy {@link ELHelper}
	 */
	@Bean
	ELHelper elHelper() {

		return new ELHelper() {

			@Override
			public ValueExpression makeValueExpression(String expression,
					Class<?> type) {

				return null;
			}
		};
	}

	@Bean
	TestExportReport exportReport() {

		return new TestExportReport();
	}

	@Bean
	DynamicUtils dynamicUtils() {

		return new DynamicUtils();
	}

	@Bean
	UniqueHelper uniqueHelper() {

		return new UniqueHelper();
	}

	@Bean
	BreadcrumbController breadCrumbController() {

		return null;
	}

	@Bean
	ICurrentpageHelper currentpageHelper() {

		return null;
	}

	@Bean
	SIGHBaseReportSimple baseReportSimple() {

		return null;
	}

	@Bean
	SearchHelper searchHelper() {

		return null;
	}

	@Bean
	AuthorityController authorityController() {

		return null;
	}

}
