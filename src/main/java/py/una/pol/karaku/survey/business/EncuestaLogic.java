/*
 * @EncuestaLogic 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.KarakuBaseLogic;
import py.una.pol.karaku.repo.IKarakuBaseDao;
import py.una.pol.karaku.survey.domain.Encuesta;
import py.una.pol.karaku.survey.repo.IEncuestaDAO;

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
public class EncuestaLogic extends KarakuBaseLogic<Encuesta, Long> implements
		IEncuestaLogic {

	@Autowired
	private IEncuestaDAO dao;

	@Override
	public IKarakuBaseDao<Encuesta, Long> getDao() {

		return dao;
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(Encuesta entity) {

		getDao().remove(entity.getId());
	}
}
