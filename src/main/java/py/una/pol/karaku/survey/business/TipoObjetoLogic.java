/*
 * @TipoObjetoLogic 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.KarakuBaseLogic;
import py.una.pol.karaku.repo.IKarakuBaseDao;
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
public class TipoObjetoLogic extends KarakuBaseLogic<TipoObjeto, Long> implements
		ITipoObjetoLogic {

	@Autowired
	private ITipoObjetoDAO dao;

	@Override
	public IKarakuBaseDao<TipoObjeto, Long> getDao() {

		return dao;
	}

}
