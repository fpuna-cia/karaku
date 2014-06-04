/*
 * @TestLogic.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.test.test.util.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.una.pol.karaku.business.KarakuBaseLogic;
import py.una.pol.karaku.repo.IKarakuBaseDao;

/**
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 *
 */
@Service
public class TestLogic extends KarakuBaseLogic<TestEntity, Long> implements
		ITestLogic {

	@Autowired
	private ITestDAO dao;

	@Override
	public IKarakuBaseDao<TestEntity, Long> getDao() {

		return dao;
	}

}
