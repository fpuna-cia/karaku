/*
 * @EncuestaLogicTest.java 1.0 26/05/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.survey;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.survey.business.EncuestaLogic;
import py.una.pol.karaku.survey.business.IEncuestaLogic;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaDetalle;
import py.una.pol.karaku.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.pol.karaku.survey.repo.EncuestaDAO;
import py.una.pol.karaku.survey.repo.EncuestaDetalleDAO;
import py.una.pol.karaku.survey.repo.EncuestaDetalleOpcionRespuestaDAO;
import py.una.pol.karaku.survey.repo.IEncuestaDAO;
import py.una.pol.karaku.survey.repo.IEncuestaDetalleDAO;
import py.una.pol.karaku.survey.repo.IEncuestaDetalleOpcionRespuestaDAO;
import py.una.pol.karaku.survey.repo.IOpcionRespuestaDAO;
import py.una.pol.karaku.survey.repo.OpcionRespuestaDAO;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.TestUtils;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 26/05/2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class EncuestaDaoTest extends BaseTestWithDatabase {

	@Configuration
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(Encuesta.class);
		}

		@Bean
		IEncuestaLogic encuestaLogic() {

			return new EncuestaLogic();
		}

		@Bean
		IEncuestaDAO encuestaDao() {

			return new EncuestaDAO();
		}

		@Bean
		IEncuestaDetalleDAO encuestaDetalleDao() {

			return new EncuestaDetalleDAO();
		}

		@Bean
		IEncuestaDetalleOpcionRespuestaDAO opcionDao() {

			return new EncuestaDetalleOpcionRespuestaDAO();
		}

		@Bean
		IOpcionRespuestaDAO opcionRespuestaDAO() {

			return new OpcionRespuestaDAO();
		}
	}

	@Autowired
	private IEncuestaDAO dao;
	@Autowired
	private IEncuestaDetalleDAO detalleDao;

	@Autowired
	private IOpcionRespuestaDAO opcionRespuestaDao;

	@Test
	public void testUpdate() throws Exception {

		Encuesta encuesta = dao.getById(500000L);
		encuesta.setDetalles(getDetalles());

		Encuesta update = dao.update(encuesta);

		assertEquals(6, update.getDetalles().size());
	}

	private List<EncuestaDetalle> getDetalles() {

		List<EncuestaDetalle> detalles = new ArrayList<EncuestaDetalle>();
		detalles.add(detalleDao.getById(500000L));
		// No agregamos el segundo detalle, para verificar si el mismo el
		// borrado de la base de datos al hacer update.

		/*
		 * Cambiamos la respuesta de la pregunta 3 (Sexo de la persona)
		 */
		EncuestaDetalle detalleSexo = detalleDao.getById(500002L);

		List<EncuestaDetalleOpcionRespuesta> opciones = new ArrayList<EncuestaDetalleOpcionRespuesta>();
		EncuestaDetalleOpcionRespuesta opcion = new EncuestaDetalleOpcionRespuesta();
		opcion.setOpcionRespuesta(opcionRespuestaDao.getById(500001L));
		opcion.setEncuestaDetalle(detalleSexo);
		opciones.add(opcion);

		detalleSexo.setOpcionRespuesta(opciones);
		detalles.add(detalleSexo);
		detalles.add(detalleDao.getById(500003L));
		detalles.add(detalleDao.getById(500004L));
		detalles.add(detalleDao.getById(500005L));
		detalles.add(detalleDao.getById(500006L));
		return detalles;
	}
}
