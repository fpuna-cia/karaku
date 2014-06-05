/*
 * @CalendarField.java 1.0 Mar 15, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.pol.karaku.dynamic.forms;

import javax.el.ValueExpression;
import org.richfaces.component.UICalendar;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 15, 2013
 * 
 */
public class CalendarField extends LabelField {

	private UICalendar bind;

	/**
	 * 
	 */
	public CalendarField() {

		bind = KarakuComponentFactory.getCalendar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.Field#getType()
	 */
	@Override
	public String getType() {

		return "py.una.pol.karaku.dynamic.forms.CalendarField";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.Field#disable()
	 */
	@Override
	public boolean disable() {

		getBind().setDisabled(true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		getBind().setDisabled(false);
		return true;
	}

	/**
	 * @return bind
	 */
	public UICalendar getBind() {

		return bind;
	}

	/**
	 * @param bind
	 *            bind para setear
	 */
	public void setBind(final UICalendar bind) {

		this.bind = bind;
	}

	/**
	 * @return
	 * @see org.richfaces.component.UICalendar#getDatePattern()
	 */
	public String getDatePattern() {

		return bind.getDatePattern();
	}

	/**
	 * @param datePattern
	 * @see org.richfaces.component.UICalendar#setDatePattern(java.lang.String)
	 */
	public void setDatePattern(final String datePattern) {

		bind.setDatePattern(datePattern);
	}

	public void setValue(final ValueExpression expression) {

		bind.setValueExpression("value", expression);
	}

}
