package py.una.pol.karaku.test.base;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.test.cucumber.DatabasePopulatorCucumberExecutionListener;
import py.una.pol.karaku.test.cucumber.TransactionalTestCucumberExecutionListener;

/**
 * Base para los tests con Cucumber.
 * 
 * @author Rainer Reyes
 * @since 1.0
 * @version 1.0 24/10/2014
 * 
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({ TransactionalTestCucumberExecutionListener.class,
		DatabasePopulatorCucumberExecutionListener.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public abstract class BaseStepCucumber extends BaseTest {

}
