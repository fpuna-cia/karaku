/*
 * @DataGridStep.java 1.0 Jun 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dynamic.wizard;

import py.una.pol.karaku.dynamic.tables.DataTable;

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

	@Override
	public boolean disable() {

		return false;
	}

	public DataTable getBind() {

		return bind;
	}

	@Override
	public boolean enable() {

		return false;
	}

}
