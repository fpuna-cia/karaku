/*
 * @SimpleStep.java 1.0 Jun 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.wizard;

import py.una.med.base.dynamic.forms.DynamicFormList;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.wizard.Step#initialize()
	 */
	@Override
	public void initialize() {

	}

	/**
	 * @return dynamicForm
	 */
	public DynamicFormList getDynamicForm() {

		return dynamicForm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#disable()
	 */
	@Override
	public boolean disable() {

		getDynamicForm().disable();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		getDynamicForm().disable();
		return true;
	}
}
