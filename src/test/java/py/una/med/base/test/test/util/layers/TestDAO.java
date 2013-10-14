/*
 * @TestDAO.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.util.layers;

import org.springframework.stereotype.Repository;
import py.una.med.base.repo.SIGHBaseDao;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Repository
public class TestDAO extends SIGHBaseDao<TestEntity, Long> implements ITestDAO {

}
