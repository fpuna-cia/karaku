/*
 * @TipoObjetoLogic 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.SIGHBaseLogic;
import py.una.pol.karaku.repo.ISIGHBaseDao;
import py.una.pol.karaku.survey.domain.TipoObjeto;
import py.una.pol.karaku.survey.repo.ITipoObjetoDAO;

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
public class TipoObjetoLogic extends SIGHBaseLogic<TipoObjeto, Long> implements
		ITipoObjetoLogic {

	@Autowired
	private ITipoObjetoDAO dao;

	@Override
	public ISIGHBaseDao<TipoObjeto, Long> getDao() {

		return dao;
	}

}
