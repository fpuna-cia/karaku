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
package py.una.pol.karaku.test.configuration;

import javax.el.ValueExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import py.una.pol.karaku.breadcrumb.BreadcrumbController;
import py.una.pol.karaku.business.reports.KarakuBaseReportSimple;
import py.una.pol.karaku.dao.search.SearchHelper;
import py.una.pol.karaku.dao.where.DateClauses;
import py.una.pol.karaku.jsf.utils.ICurrentpageHelper;
import py.una.pol.karaku.reports.DynamicUtils;
import py.una.pol.karaku.security.AuthorityController;
import py.una.pol.karaku.test.util.TestControllerHelper;
import py.una.pol.karaku.test.util.TestExportReport;
import py.una.pol.karaku.util.ELHelper;
import py.una.pol.karaku.util.UniqueHelper;

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
	 * Esto permite la creaciÃ³n de pickers en test
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
	KarakuBaseReportSimple baseReportSimple() {

		return null;
	}

	@Bean
	SearchHelper searchHelper() {

		return new SearchHelper();
	}

	@Bean
	AuthorityController authorityController() {

		return null;
	}

	@Bean
	public DateClauses dc() {

		return new DateClauses();
	}

}
