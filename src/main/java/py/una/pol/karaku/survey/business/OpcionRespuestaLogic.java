/*
 * @OpcionRespuestaLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.KarakuBaseLogic;
import py.una.pol.karaku.repo.IKarakuBaseDao;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.survey.repo.IOpcionRespuestaDAO;

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
public class OpcionRespuestaLogic extends KarakuBaseLogic<OpcionRespuesta, Long>
		implements IOpcionRespuestaLogic {

	@Autowired
	private IOpcionRespuestaDAO dao;

	@Override
	public IKarakuBaseDao<OpcionRespuesta, Long> getDao() {

		return dao;
	}

}
