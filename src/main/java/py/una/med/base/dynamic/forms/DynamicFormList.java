/*
 * @DynamicFormList.java 1.0 Feb 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.LinkedList;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class DynamicFormList extends LinkedList<Field> {

	private static final long serialVersionUID = 6014962602509720398L;

	public void enable() {
		for (Field f : this) {
			f.enable();
		}
	}

	public void disable() {
		for (Field f : this)
			f.disable();
	}
}
