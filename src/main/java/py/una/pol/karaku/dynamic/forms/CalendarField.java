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
