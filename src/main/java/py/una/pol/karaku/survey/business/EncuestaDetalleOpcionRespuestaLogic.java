/*
 * @EncuestaDetalleOpcionRespuestaLogic 1.0 29/05/13. Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.SIGHBaseLogic;
import py.una.pol.karaku.repo.ISIGHBaseDao;
import py.una.pol.karaku.survey.domain.EncuestaDetalleOpcionRespuesta;
import py.una.pol.karaku.survey.repo.IEncuestaDetalleOpcionRespuestaDAO;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Service
@Transactional
public class EncuestaDetalleOpcionRespuestaLogic extends
		SIGHBaseLogic<EncuestaDetalleOpcionRespuesta, Long> implements
		IEncuestaDetalleOpcionRespuestaLogic {

	@Autowired
	private IEncuestaDetalleOpcionRespuestaDAO dao;

	@Override
	public ISIGHBaseDao<EncuestaDetalleOpcionRespuesta, Long> getDao() {

		return dao;
	}

}
