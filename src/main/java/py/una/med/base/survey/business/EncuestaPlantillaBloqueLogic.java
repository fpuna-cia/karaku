/*
 * @EncuestaPlantillaBloqueLogic 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;
import py.una.med.base.survey.domain.EncuestaPlantilla;
import py.una.med.base.survey.domain.EncuestaPlantillaBloque;
import py.una.med.base.survey.repo.IEncuestaPlantillaBloqueDAO;

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
public class EncuestaPlantillaBloqueLogic extends
		SIGHBaseLogic<EncuestaPlantillaBloque, Long> implements
		IEncuestaPlantillaBloqueLogic {

	@Autowired
	private IEncuestaPlantillaBloqueDAO dao;

	@Override
	public ISIGHBaseDao<EncuestaPlantillaBloque, Long> getDao() {

		return dao;
	}

	@Override
	public List<EncuestaPlantillaBloque> getBlocksByTemplate(
			EncuestaPlantilla template) {

		return dao.getBlocksByTemplate(template);
	}

}
