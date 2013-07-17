/*
 * @TipoObjetoLogic 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.TipoObjeto;
import py.una.med.social.business.SocialBaseLogic;
import py.una.med.social.repo.ITipoObjetoDAO;

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
public class TipoObjetoLogic extends SocialBaseLogic<TipoObjeto> implements
		ITipoObjetoLogic {

	@Autowired
	private ITipoObjetoDAO dao;

	@Override
	public ISIGHBaseDao<TipoObjeto, Long> getDao() {

		return dao;
	}

}
