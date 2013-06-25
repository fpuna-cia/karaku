/*
 * @DataGridStep.java 1.0 Jun 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.wizard;

import py.una.med.base.dynamic.tables.DataTable;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 5, 2013
 * 
 */
public class DataTableStep extends AbstractStep implements Step {

	private final DataTable bind;

	public DataTableStep(DataTable table) {

		this.bind = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.wizard.Step#initialize()
	 */
	@Override
	public void initialize() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#disable()
	 */
	@Override
	public boolean disable() {

		return false;
	}

	public DataTable getBind() {

		return bind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		return false;
	}

}
