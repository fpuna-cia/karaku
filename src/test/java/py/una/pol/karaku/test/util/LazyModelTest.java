package py.una.pol.karaku.test.util;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import py.una.pol.karaku.survey.business.EncuestaLogic;
import py.una.pol.karaku.survey.business.EncuestaPlantillaLogic;
import py.una.pol.karaku.survey.business.IEncuestaPlantillaLogic;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaPlantilla;
import py.una.pol.karaku.survey.repo.EncuestaDAO;
import py.una.pol.karaku.survey.repo.EncuestaDetalleDAO;
import py.una.pol.karaku.survey.repo.EncuestaDetalleOpcionRespuestaDAO;
import py.una.pol.karaku.survey.repo.EncuestaPlantillaDAO;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.ControllerTestConfiguration;
import py.una.pol.karaku.test.configuration.TestBeanCreator;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.util.LazyModel;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LazyModelTest extends BaseTestWithDatabase {

	@Configuration
	static class ContextConfiguration extends ControllerTestConfiguration {

		@Bean
		public LazyModel<EncuestaPlantilla, Long> lazyModel() {
			return new LazyModel<EncuestaPlantilla, Long>();

		}
	}

	@Configuration
	static class ContextConfiguration2 extends TransactionTestConfiguration {

		@Bean
		public TestBeanCreator beanCreator() {

			return new TestBeanCreator(TestUtils.getAsClassArray(
					EncuestaLogic.class, EncuestaDetalleDAO.class,
					EncuestaPlantillaLogic.class,
					EncuestaPlantillaDAO.class,
					EncuestaDAO.class, EncuestaDetalleOpcionRespuestaDAO.class));
		}

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(Encuesta.class,
					EncuestaPlantilla.class);
		}
	}

	@Autowired
	private IEncuestaPlantillaLogic logic;

	@Autowired
	private TestControllerHelper controllerHelper;

	@Autowired
	private LazyModel<EncuestaPlantilla, Long> lazyModel;

	@Test
	public void getLoad() {

		int pageSize = 10;
		int first = 0;
		Map<String, Object> filters = new HashMap<String, Object>();

		String sortField = "id";
		Long value = 1L;
		String nameField = "id";

		filters.put(nameField, value);

		lazyModel.setLogic(logic);

		List<EncuestaPlantilla> Encuestaes = lazyModel.load(first, pageSize, sortField,
				SortOrder.ASCENDING, filters);

		assertNotNull(Encuestaes);
		
		sortField = "usuario";
		String valor = "";
		nameField = "usuario";

		filters.put(nameField, valor);
		
		List<EncuestaPlantilla> Encuestas = lazyModel.load(first, pageSize, sortField,
				SortOrder.ASCENDING, filters);

		assertNotNull(Encuestas);
		
	}

}