/*
 * @TestConfiguration.java 1.0 Aug 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.configuration;

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
import py.una.med.base.util.DateProvider;
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

	@Bean
	TestExportReport exportReport() {

		return new TestExportReport();
	}

	@Bean
	public DynamicUtils dynamicUtils() {

		return new DynamicUtils();
	}

	@Bean
	UniqueHelper object() {

		return new UniqueHelper();
	}

	@Bean
	public BreadcrumbController breadCrumbController() {

		return null;
	}

	@Bean
	public ICurrentpageHelper currentpageHelper() {

		return null;
	}

	@Bean
	public SIGHBaseReportSimple baseReportSimple() {

		return null;
	}

	@Bean
	public SearchHelper searchHelper() {

		return null;
	}

	@Bean
	public AuthorityController authorityController() {

		return null;
	}

	@Bean
	public DateProvider dateProvider() {

		return new DateProvider();
	}

}
