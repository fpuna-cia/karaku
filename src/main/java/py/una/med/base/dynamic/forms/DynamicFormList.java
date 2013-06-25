/*
 * @DynamicFormList.java 1.0 Feb 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * Formulario dinámico base, es un wrapper de {@link List}, para mantener una
 * lista de {@link Field}
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class DynamicFormList {

	private List<Field> fields;

	public DynamicFormList() {

		fields = new ArrayList<Field>();
	}

	public void enable() {

		for (Field f : fields) {
			f.enable();
		}
	}

	public void disable() {

		for (Field f : fields) {
			f.disable();
		}

	}

	public void add(Field ... field) {

		for (Field f : field) {
			fields.add(f);
		}
	}

	/**
	 * @return fields
	 */
	public List<Field> getFields() {

		return fields;
	}

	/**
	 * @param fields
	 *            fields para setear
	 */
	public void setFields(List<Field> fields) {

		this.fields = fields;
	}
}
