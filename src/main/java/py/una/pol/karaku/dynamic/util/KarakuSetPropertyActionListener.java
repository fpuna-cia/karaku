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
package py.una.pol.karaku.dynamic.util;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import py.una.pol.karaku.util.ELHelper;

/**
 * Action Listener que se encarga de emular el comportamiento de
 * &lt;f:setPropertyActionListener&gt;, esto se debe a que no se puede crear un
 * componente de ese tipo, si se encuentra la implementacion de referencia
 * marcar esta como deprecada y utilizar esa
 * 
 * @author Arturo Volpe Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * @see <a
 *      href="http://www.java2s.com/Open-Source/Java/EJB-Server/resin-4.0.7/com/caucho/jsf/event/SetPropertyActionListener.java.htm">
 *      MyFaces SetPropertyActionListener implementation</a>
 * 
 * 
 */
public class KarakuSetPropertyActionListener implements ActionListener,
		StateHolder {

	private ValueExpression source;
	private ValueExpression target;
	private boolean isTransient;

	public KarakuSetPropertyActionListener() {

	}

	public KarakuSetPropertyActionListener(final ValueExpression source,
			final ValueExpression target) {

		this.source = source;
		this.target = target;
	}

	public KarakuSetPropertyActionListener(final String source,
			final String target) {

		this.source = ELHelper.INSTANCE.makeValueExpression(source,
				Object.class);
		this.target = ELHelper.INSTANCE.makeValueExpression(target,
				Object.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.event.ActionListener#processAction(javax.faces.event.ActionEvent
	 * )
	 */
	@Override
	public void processAction(final ActionEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();

		Object value = source.getValue(fc.getELContext());
		target.setValue(fc.getELContext(), value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext
	 * )
	 */
	@Override
	public Object saveState(final FacesContext context) {

		return new Object[] { source, target };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.
	 * FacesContext, java.lang.Object)
	 */
	@Override
	public void restoreState(final FacesContext context, final Object state) {

		Object[] o = (Object[]) state;
		source = (ValueExpression) o[0];
		target = (ValueExpression) o[1];

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.StateHolder#isTransient()
	 */
	@Override
	public boolean isTransient() {

		return isTransient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.StateHolder#setTransient(boolean)
	 */
	@Override
	public void setTransient(final boolean newTransientValue) {

		isTransient = newTransientValue;

	}

}
