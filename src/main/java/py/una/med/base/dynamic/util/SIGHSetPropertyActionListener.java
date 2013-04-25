/*
 * @SIGHSetPropertyActionListener.java 1.0 Mar 13, 2013 Sistema Integral de
 * Gestión Hospitalaria
 */
package py.una.med.base.dynamic.util;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import py.una.med.base.util.ELHelper;

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
public class SIGHSetPropertyActionListener implements ActionListener,
		StateHolder {

	private ValueExpression source;
	private ValueExpression target;
	private boolean isTransient;

	public SIGHSetPropertyActionListener() {

	}

	public SIGHSetPropertyActionListener(final ValueExpression source,
			final ValueExpression target) {

		this.source = source;
		this.target = target;
	}

	public SIGHSetPropertyActionListener(final String source,
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
	public void processAction(final ActionEvent event)
			throws AbortProcessingException {

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
