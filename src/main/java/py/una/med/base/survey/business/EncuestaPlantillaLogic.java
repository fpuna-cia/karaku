/*
 * @EncuestaPlantillaLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.EncuestaPlantilla;
import py.una.med.social.business.SocialBaseLogic;
import py.una.med.social.repo.IEncuestaPlantillaDAO;

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
public class EncuestaPlantillaLogic extends SocialBaseLogic<EncuestaPlantilla>
		implements IEncuestaPlantillaLogic {

	@Autowired
	private IEncuestaPlantillaDAO dao;

	@Override
	public ISIGHBaseDao<EncuestaPlantilla, Long> getDao() {

		return dao;
	}

}
