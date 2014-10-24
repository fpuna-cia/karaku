package py.una.pol.karaku.test.cucumber;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Clase base que ejecuta los tests de Cucumber con JUnit.
 * 
 * @author Romina Fernandez
 * @author Rainer Reyes
 * @since 1.0
 * @version 1.0 24/10/2014
 * 
 */
@RunWith(Cucumber.class)
@CucumberOptions(format = { "pretty", "html:target/cucumber" })
public class RunCukesTestBase {

}
