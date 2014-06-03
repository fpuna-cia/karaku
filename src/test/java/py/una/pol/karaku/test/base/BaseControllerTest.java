/*
 * @ExampleTest.java 1.0 Aug 19, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.base;

import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.test.util.TestControllerHelper;
import py.una.med.base.test.util.TestI18nHelper;
import py.una.med.base.util.I18nHelper;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 11, 2014
 * 
 */
public abstract class BaseControllerTest extends BaseTest {

	@Autowired
	private TestControllerHelper helper;

	@Autowired
	private TestI18nHelper i18nHelper;

	/**
	 * Retorna el {@link TestControllerHelper}.
	 * 
	 * <p>
	 * Es útil para verificar los mensajes, se puede obtener el último mensaje
	 * generado con {@link TestControllerHelper#getLastMessage()}
	 * </p>
	 * 
	 * @return helper nunca <code>null</code>
	 * @see TestControllerHelper
	 */
	public TestControllerHelper getHelper() {

		return helper;
	}

	/**
	 * Retorna el {@link TestI18nHelper}.
	 * 
	 * <p>
	 * Es útil para agregar cadenas a internacionalizar.
	 * </p>
	 * 
	 * @return i18nHelper {@link I18nHelper}, nunca <code>null</code>.
	 * @see TestI18nHelper
	 */
	public TestI18nHelper getI18nHelper() {

		return i18nHelper;
	}
}
