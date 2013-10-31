/*
 * @ExampleTest.java 1.0 Aug 19, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.base;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.util.transaction.DatabasePopulatorExecutionListener;
import py.una.med.base.test.util.transaction.SQLFiles;

/**
 * Base clase para Test que utilicen una base de datos.
 * <p>
 * Los test que desean tener un script de inserción de datos para pruebas deben
 * heredad de esta clase. Automáticamente todos los test son isolados, es decir,
 * utilizan este proceso:
 * <ol>
 * <li>Se inicia una transacción</li>
 * <li>Invocación de {@link #populateTable()} que crea los datos de prueba</li>
 * <li>Se invoca a los métodos que están marcados con {@literal @}{@link Before}
 * </li>
 * <li>Se invoca al método</li>
 * <li>Se hace un rollback de la transacción</li>
 * </ol>
 * </p>
 * <p>
 * Las anotaciones {@link Transactional} y {@link TransactionConfiguration} se
 * heredan, asi que no hace falta declararlas en cada subclase. Estas
 * anotaciones indican que todos los {@literal @}{@link Test} serán ejecutados
 * como una transacción independiente, si se desea que un test no realice
 * rollback, se puede utilizar la anotación {@literal @}{@link Rollback} pasando
 * como {@link Rollback#value()} <code>false</code>
 * </p>
 *
 * <p>
 * Una clase que desee hacer test deberá ser como sigue:
 *
 * <pre>
 * {@literal @}{@link RunWith}(SpringJUnit4ClassRunner.class)
 * {@literal @}{@link ContextConfiguration}(loader = AnnotationConfigContextLoader.class)
 * public class Test extends BaseTestWithDatabase {
 *
 * 	{@literal @}{@link Configuration}
 * 	{@literal @}{@link EnableTransactionManagement}()
 * 	static class ContextConfiguration extends {@link TransactionTestConfiguration} {
 *
 * 		{@liberal @}{@link Bean}
 * 		public MyBean mybean() {
 * 			return new MyBean();
 * 		}
 *
 * 	}
 *
 * 	{@literal @}{@link Autowired}	// Podemos inyectar el bean que creamos más arriba
 * 	MyBean mybean;
 *
 * 	{@literal @}{@link Test}
 * 	public void testAddInvalidData () {
 * 		...
 * 	}
 * }
 * </pre>
 *
 * </p>
 * <p>
 * Se utiliza una configuración independiente por cada
 * {@link BaseTestWithDatabase} para intentar que sean lo mas independientes
 * unos de otros. En términos de rendimiento, no hay demasiado detrimento, pues
 * la mayoría de los componentes son cacheados en memoria.
 * </p>
 * <p>
 * Al utilizar una configuración que herede de
 * {@link TransactionTestConfiguration} se disponibilizan en el contexto (via
 * {@link Autowired}) la mayoría de los {@link Bean} que forman parte de Karaku
 * para la persistencia, incluyendo toda la infrastructura de {@link BaseDAO}.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * @see TransactionTestConfiguration
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({ TransactionalTestExecutionListener.class,
	DatabasePopulatorExecutionListener.class })
@SQLFiles
public abstract class BaseTestWithDatabase extends BaseTest {

}
