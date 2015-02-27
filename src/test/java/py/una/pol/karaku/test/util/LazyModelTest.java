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

import py.una.med.asistencial.domain.Cama;
import py.una.med.asistencial.domain.persona.Persona;
import py.una.med.configuracion.business.IUniversidadLogic;
import py.una.med.configuracion.business.UniversidadLogic;
import py.una.med.configuracion.dao.IUniversidadDao;
import py.una.med.configuracion.dao.UniversidadDao;
import py.una.med.configuracion.domain.Universidad;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.ControllerTestConfiguration;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.transaction.SQLFiles;
import py.una.pol.karaku.util.LazyModel;

@SQLFiles
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LazyModelTest extends BaseTestWithDatabase {

	@Configuration
	static class ContextConfiguration extends ControllerTestConfiguration {
		
		@Bean
		public LazyModel<Universidad, Long> lazyModel() {
			return new LazyModel<Universidad, Long>();
				 
		} 
	}
	
	@Configuration
	static class ContextConfiguration2 extends TransactionTestConfiguration {
		
		
		@Bean
		public IUniversidadLogic UniversidadLogic() {
			
			return new UniversidadLogic();
		}
		
		@Bean
		public IUniversidadDao UniversidadDao() {
			
			return new UniversidadDao();
		}
		
		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(
					Cama.class,
					Persona.class,
					Universidad.class);
		}
	}

	@Autowired
	private IUniversidadLogic logic;

	@Autowired
	private TestControllerHelper controllerHelper;

	@Autowired
	private LazyModel<Universidad, Long> lazyModel;
	
	@Test
	public void getLoad() {

		int pageSize = 10;
		int first = 0;
		Map<String, Object> filters = new HashMap<String, Object>();
		String sortField = "sigla";
		String value = "UNA";
		String nameField = "sigla";
		
		filters.put(nameField, value);
		
		lazyModel.setLogic(logic);
		
		List<Universidad> universidades = lazyModel.load(first, pageSize, sortField, SortOrder.ASCENDING, filters);
		
		assertNotNull(universidades);
	}
	
	
}