/*
 * @SurveyHelperTest.java 1.0 02/07/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.survey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.reports.KarakuReportBlock;
import py.una.pol.karaku.reports.KarakuReportBlockField;
import py.una.pol.karaku.reports.KarakuReportBlockGrid;
import py.una.pol.karaku.survey.business.EncuestaPlantillaPreguntaLogic;
import py.una.pol.karaku.survey.business.IEncuestaPlantillaPreguntaLogic;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.repo.EncuestaDAO;
import py.una.pol.karaku.survey.repo.EncuestaDetalleDAO;
import py.una.pol.karaku.survey.repo.EncuestaDetalleOpcionRespuestaDAO;
import py.una.pol.karaku.survey.repo.EncuestaPlantillaBloqueDAO;
import py.una.pol.karaku.survey.repo.EncuestaPlantillaPreguntaDAO;
import py.una.pol.karaku.survey.repo.IEncuestaDAO;
import py.una.pol.karaku.survey.repo.IEncuestaDetalleDAO;
import py.una.pol.karaku.survey.repo.IEncuestaDetalleOpcionRespuestaDAO;
import py.una.pol.karaku.survey.repo.IEncuestaPlantillaBloqueDAO;
import py.una.pol.karaku.survey.repo.IEncuestaPlantillaPreguntaDAO;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.util.SurveyHelper;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 02/07/2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SurveyHelperTest extends BaseTestWithDatabase {

	@Configuration
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(Encuesta.class);
		}

		@Bean
		public SurveyHelper surveyHelper() {

			return new SurveyHelper();
		}

		@Bean
		public IEncuestaPlantillaBloqueDAO encuestaPlantillaBloqueDAO() {

			return new EncuestaPlantillaBloqueDAO();
		}

		@Bean
		public IEncuestaPlantillaPreguntaLogic encuestaPlantillaPreguntaLogic() {

			return new EncuestaPlantillaPreguntaLogic();
		}

		@Bean
		public IEncuestaPlantillaPreguntaDAO encuestaPlantillaPreguntaDAO() {

			return new EncuestaPlantillaPreguntaDAO();
		}

		@Bean
		public IEncuestaDetalleDAO encuestaDetalleDAO() {

			return new EncuestaDetalleDAO();
		}

		@Bean
		public IEncuestaDetalleOpcionRespuestaDAO encuestaDetalleOpcionRespuestaDAO() {

			return new EncuestaDetalleOpcionRespuestaDAO();
		}

		@Bean
		public IEncuestaDAO encuestaDao() {

			return new EncuestaDAO();
		}
	}

	@Autowired
	private SurveyHelper helper;

	@Autowired
	private IEncuestaDAO encuestaDao;

	@Test
	public void testbuildReport() throws Exception {

		List<KarakuReportBlock> bloques = helper.buildReport(encuestaDao
				.getById(500000L));

		assertEquals(2, bloques.size());
		assertTrue(bloques.get(0) instanceof KarakuReportBlockField);
		assertTrue(bloques.get(1) instanceof KarakuReportBlockGrid);
	}

}
