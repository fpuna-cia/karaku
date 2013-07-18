/*
 * @EncuestaDetalleDAO 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.repo;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.repo.SIGHBaseDao;
import py.una.med.base.survey.domain.Encuesta;
import py.una.med.base.survey.domain.EncuestaDetalle;
import py.una.med.base.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.domain.OpcionRespuesta;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Repository
public class EncuestaDetalleDAO extends SIGHBaseDao<EncuestaDetalle, Long> implements
		IEncuestaDetalleDAO {

	@Autowired
	private IEncuestaDetalleOpcionRespuestaDAO encuestaDetalleOpcionRespuestaDAO;

	@Override
	public List<?> getRespuestas(Encuesta encuesta,
			EncuestaPlantillaBloque block) {

		String select = "select ed.pregunta.orden, ed.numeroFila, ed.respuesta, ed.id";
		String from = " from EncuestaDetalle ed ";
		String where = " where ed.encuesta=:encuesta"
				+ " and ed.pregunta.bloque=:bloque";
		String order = " order by ed.numeroFila, ed.pregunta.orden";
		Query query = getSession().createQuery(
				select.concat(from).concat(where).concat(order));
		query.setParameter("encuesta", encuesta);
		query.setParameter("bloque", block);
		return query.list();
	}

	@Override
	public List<OpcionRespuesta> getRespuestasSelected(long encuestaDetalle) {

		EncuestaDetalleOpcionRespuesta example = new EncuestaDetalleOpcionRespuesta();
		example.setEncuestaDetalle(getById(encuestaDetalle));
		Where<EncuestaDetalleOpcionRespuesta> where = new Where<EncuestaDetalleOpcionRespuesta>();
		where.setExample(example);
		List<OpcionRespuesta> list = new ArrayList<OpcionRespuesta>();
		for (EncuestaDetalleOpcionRespuesta detalle : encuestaDetalleOpcionRespuestaDAO
				.getAll(where, null)) {
			list.add(detalle.getOpcionRespuesta());
		}
		return list;
	}

}
