/*
 * @TipoBloqueLogic 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.TipoBloque;
import py.una.med.base.survey.repo.ITipoBloqueDAO;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 27/05/2013
 * 
 */
@Service
@Transactional
public class TipoBloqueLogic extends SIGHBaseLogic<TipoBloque, Long> implements
		ITipoBloqueLogic {

	@Autowired
	private ITipoBloqueDAO dao;

	@Override
	public ISIGHBaseDao<TipoBloque, Long> getDao() {

		return dao;
	}

}
