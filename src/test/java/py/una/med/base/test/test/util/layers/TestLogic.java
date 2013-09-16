/*
 * @TestLogic.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.util.layers;

import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.business.SIGHBaseLogic;
import py.una.med.base.repo.ISIGHBaseDao;

/**
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 *
 */
public class TestLogic extends SIGHBaseLogic<TestEntity, Long> implements
		ITestLogic {

	@Autowired
	private ITestDAO dao;

	@Override
	public ISIGHBaseDao<TestEntity, Long> getDao() {

		return dao;
	}

}
