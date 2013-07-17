/*
 * @EncuestaLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.Encuesta;
import py.una.med.social.business.SocialBaseLogic;
import py.una.med.social.repo.IEncuestaDAO;

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
public class EncuestaLogic extends SocialBaseLogic<Encuesta> implements
		IEncuestaLogic {

	@Autowired
	private IEncuestaDAO dao;

	@Override
	public ISIGHBaseDao<Encuesta, Long> getDao() {

		return dao;
	}

}
