/*
 * @MenuTest.java 1.0 Oct 17, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.menu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.log.Log;
import py.una.med.base.menu.client.AbstractMenuProvider;
import py.una.med.base.menu.client.MenuHelper;
import py.una.med.base.menu.schemas.Menu;
import py.una.med.base.menu.server.MenuServerLogic;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.test.util.TestI18nHelper;
import py.una.med.base.test.util.TestPropertiesUtil;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MenuTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		MenuServerLogic menuServerLogic() {

			return new MenuServerLogic();
		}

		@Bean
		MenuHelper menuHelper() {

			return new MenuHelper();
		}

		@Bean
		TestMenuProvider testMenuHelper() {

			return new TestMenuProvider();
		}
	}

	@Log
	private Logger log;

	/**
 *
 */
	@Autowired
	MenuServerLogic menuServerLogic;

	@Autowired
	TestI18nHelper i18nHelper;

	@Autowired
	TestPropertiesUtil propertiesUtil;

	@Autowired
	MenuHelper mh;

	@Autowired
	TestMenuProvider testMenuProvider;

	private Menu m1;

	private Menu m12;

	private Menu m11;

	private Menu m122;

	private Menu m121;

	@Before
	public void addEntriesI18() {

		i18nHelper.addString("1", "UNO");
		i18nHelper.addString("1.1", "UNO.UNO");
		i18nHelper.addString("1.2", "UNO.DOS");
		i18nHelper.addString("1.2.1", "UNO.DOS.UNO");
		i18nHelper.addString("1.2.2", "UNO.DOS.DOS");
		i18nHelper.addString("TEST_STRING", "test");

	}

	@Before
	public void initialize() {

		m1 = m(null, "1", 1, null);
		m12 = m(m1, "1.2", 2, null);
		m11 = m(m1, "1.1", 1, "/views/caso3/list.xhtml");
		m121 = m(m12, "1.2.1", 1, "/views/caso4/report.xhtml");
		m122 = m(m12, "1.2.2", 2, "/views/caso5/list.xhtml");
	}

	@Before
	public void setProperties() {

		propertiesUtil.put("application.appUrlPlaceHolder", "base");
	}

	/**
	 * Si este test falla, verificar el test {@link #converTest()} que convierte
	 * un formato viejo al nuevo.
	 */
	@Test
	public void getMenuTest() {

		Menu m = menuServerLogic.getCurrentSystemMenu().get(0);
		assertNotNull(m.getItems());
		assertNotNull(m.getName());
		assertFalse(m.getOrder() == 0);
	}

	@Test
	public void buildMenuTest() {

		menuServerLogic.configMenu(m1);

		assertEquals("No ordena (1er level)", Arrays.asList(m11, m12),
				m1.getItems());

		assertEquals("No ordena (2do level)", Arrays.asList(m121, m122),
				m12.getItems());
		mh.createAlias();
	}

	@Test
	public void internationalizationTest() {

		menuServerLogic.configMenu(m1);
		assertEquals("UNO", m1.getName());
		assertEquals("UNO.UNO", m11.getName());
		assertEquals("UNO.DOS", m12.getName());
		assertEquals("UNO.DOS.DOS", m122.getName());
	}

	@Test
	public void testMenuHelperFather() {

		Menu root = menuServerLogic.configMenu(m1);
		testMenuProvider.setRoot(root);
		mh.createAlias();

		assertEquals(m1, mh.getFather(m11));
		assertEquals(m12, mh.getFather(m122));
		assertEquals(null, mh.getFather(m1));
		assertEquals(m1, mh.getFather(m12));

	}

	@Test
	public void testMenuHelperGetByUri() {

		Menu root = menuServerLogic.configMenu(m1);
		testMenuProvider.setRoot(root);
		mh.createAlias();

		assertEquals(m11, mh.getMenuByUrl("/views/caso3/abm.xhtml"));
		assertEquals(m11, mh.getMenuByUrl("/views/caso3/list.xhtml"));
		assertEquals(m121, mh.getMenuByUrl("/views/caso4/report.xhtml"));
		assertEquals(m122, mh.getMenuByUrl("/views/caso5/abm.xhtml"));
		assertEquals(m122, mh.getMenuByUrl("/views/caso5/list.xhtml"));

	}

	// /////////////////////////////
	// MÉTODO Y CLASES UTILITARIAS//
	// /////////////////////////////

	public Menu m(Menu father, String id, int order, String uri) {

		Menu toRet = new Menu();
		toRet.setId(id);
		toRet.setOrder(order);
		toRet.setName(id);
		toRet.setUrl(uri);
		if (father != null) {
			father.getItems().add(toRet);
		}
		return toRet;
	}

	static class TestMenuProvider extends AbstractMenuProvider {

		List<Menu> roots;

		public void setRoot(Menu root) {

			this.roots = new ArrayList<Menu>();
			this.roots.add(root);
			notifyMenuRebuild(this.roots);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<Menu> getMenu() {

			return roots;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<Menu> getLocalMenu() {

			return roots;
		}

	}

}
