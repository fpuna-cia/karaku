/*
 * @EncuestaDAO 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.repo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.repo.SIGHBaseDao;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.domain.EncuestaDetalle;
import py.una.pol.karaku.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Repository
public class EncuestaDAO extends SIGHBaseDao<Encuesta, Long> implements
		IEncuestaDAO {

	@Autowired
	private IEncuestaDetalleDAO detalleDao;

	@Autowired
	private IEncuestaDetalleOpcionRespuestaDAO opcionDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Nonnull
	public Encuesta update(Encuesta entity) {

		/*
		 * Borramos las respuestas que fueron descartadas.
		 */
		Where<EncuestaDetalle> where = Where.get();
		where.addClause(Clauses.eq("encuesta", entity));
		List<EncuestaDetalle> listaDB = detalleDao.getAll(where, null);

		for (EncuestaDetalle detalle : listaDB) {
			if (!entity.getDetalles().contains(detalle)) {
				detalleDao.remove(detalle);
			}
		}

		/*
		 * Borramos las opciones que fueron deseleccionadas o descartadas.
		 */
		// Creamos la lista de opciones actual para posteriormente comparar con
		// las opciones de la base de datos.
		List<EncuestaDetalleOpcionRespuesta> allOption = new ArrayList<EncuestaDetalleOpcionRespuesta>();
		for (EncuestaDetalle detalle : entity.getDetalles()) {
			if (ListHelper.hasElements(detalle.getOpcionRespuesta())) {
				allOption.addAll(detalle.getOpcionRespuesta());
			}
		}

		@SuppressWarnings("unchecked")
		List<EncuestaDetalleOpcionRespuesta> optionDB = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from EncuestaDetalleOpcionRespuesta opcion where opcion.encuestaDetalle.encuesta = :en")
				.setParameter("en", entity).list();

		for (EncuestaDetalleOpcionRespuesta opcion : optionDB) {
			if (!allOption.contains(opcion)) {
				opcionDao.remove(opcion);
			}
		}

		return super.update(entity);
	}

}
