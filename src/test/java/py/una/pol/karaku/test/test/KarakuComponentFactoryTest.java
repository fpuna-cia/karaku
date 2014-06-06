/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.test.test;

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
import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.dynamic.forms.PickerField;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 6, 2014
 * 
 */
public class KarakuComponentFactoryTest {

	@Test
	public void test() {

		assertNotNull(KarakuComponentFactory.getHtmlInputText());
		assertNotNull(KarakuComponentFactory.getToogleControl(null, "test"));
		assertNotNull(KarakuComponentFactory.getToogleControl("id", "test"));
		MockedFacesContext mfc = new MockedFacesContext();
		assertNotNull(KarakuComponentFactory.getHtmlInputText());
		assertNotNull(KarakuComponentFactory.getToogleControl(null, "test"));
		assertNotNull(KarakuComponentFactory.getToogleControl("id", "test"));

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
