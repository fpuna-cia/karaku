/*
 * @SimpleStep.java 1.0 Jun 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dynamic.wizard;

import py.una.pol.karaku.dynamic.forms.DynamicFormList;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 5, 2013
 * 
 */
public class DynamicFormStep extends AbstractStep implements Step {

	private final DynamicFormList dynamicForm;

	public DynamicFormStep(DynamicFormList form) {

		dynamicForm = form;
	}

	/**
	 * @return dynamicForm
	 */
	public DynamicFormList getDynamicForm() {

		return dynamicForm;
	}

	@Override
	public boolean disable() {

		getDynamicForm().disable();
		return true;
	}

	@Override
	public boolean enable() {

		getDynamicForm().disable();
		return true;
	}
}
