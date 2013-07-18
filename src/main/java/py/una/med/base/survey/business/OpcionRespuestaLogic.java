/*
 * @OpcionRespuestaLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.OpcionRespuesta;
import py.una.med.base.survey.repo.IOpcionRespuestaDAO;

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
public class OpcionRespuestaLogic extends SIGHBaseLogic<OpcionRespuesta, Long>
		implements IOpcionRespuestaLogic {

	@Autowired
	private IOpcionRespuestaDAO dao;

	@Override
	public ISIGHBaseDao<OpcionRespuesta, Long> getDao() {

		return dao;
	}

}
