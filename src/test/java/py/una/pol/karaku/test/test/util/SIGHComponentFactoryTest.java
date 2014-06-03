/*
 * @SIGHComponentFactoryTest.java 1.0 May 6, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.util;

import static org.junit.Assert.assertNotNull;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import org.junit.Test;
import org.richfaces.component.behavior.ToggleControl;
import py.una.pol.karaku.dynamic.forms.PickerField;
import py.una.pol.karaku.dynamic.forms.SIGHComponentFactory;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 6, 2014
 * 
 */
public class SIGHComponentFactoryTest {

	@Test
	public void test() {

		assertNotNull(SIGHComponentFactory.getHtmlInputText());
		assertNotNull(SIGHComponentFactory.getToogleControl(null, "test"));
		assertNotNull(SIGHComponentFactory.getToogleControl("id", "test"));
		MockedFacesContext mfc = new MockedFacesContext();
		assertNotNull(SIGHComponentFactory.getHtmlInputText());
		assertNotNull(SIGHComponentFactory.getToogleControl(null, "test"));
		assertNotNull(SIGHComponentFactory.getToogleControl("id", "test"));

		mfc.clear();
		new PickerField<String>().enable();

	}

	/**
	 * Version mockeada del faces context para que se puedan crear objetos desde
	 * los test.
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 May 6, 2014
	 * 
	 */
	class MockedFacesContext extends FacesContextWrapper {

		public MockedFacesContext() {

			setCurrentInstance(this);
		}

		@Override
		public FacesContext getWrapped() {

			return null;
		}

		public void clear() {

			setCurrentInstance(null);
		}

		@Override
		public Application getApplication() {

			return new ApplicationWrapper() {

				@Override
				public Application getWrapped() {

					return null;
				}

				@Override
				public UIComponent createComponent(FacesContext context,
						String componentType, String rendererType) {

					return new HtmlInputText();
				}

				@Override
				public Behavior createBehavior(String behaviorId)
						throws FacesException {

					return new ToggleControl();
				}
			};

		}
	}
}
