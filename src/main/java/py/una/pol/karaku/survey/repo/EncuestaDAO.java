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
package py.una.pol.karaku.survey.repo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.repo.KarakuBaseDao;
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
public class EncuestaDAO extends KarakuBaseDao<Encuesta, Long> implements
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
