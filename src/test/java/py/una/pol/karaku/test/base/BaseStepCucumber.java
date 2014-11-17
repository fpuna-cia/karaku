package py.una.pol.karaku.test.base;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.configuration.KarakuBaseConfiguration;
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

	@Bean
	public CustomScopeConfigurer configurer() {

		CustomScopeConfigurer toRet = new CustomScopeConfigurer();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KarakuBaseConfiguration.SCOPE_CONVERSATION,
				new SimpleThreadScope());
		map.put(KarakuBaseConfiguration.SCOPE_CONVERSATION_MANUAL,
				new SimpleThreadScope());
		toRet.setScopes(map);
		return toRet;
	}

}
