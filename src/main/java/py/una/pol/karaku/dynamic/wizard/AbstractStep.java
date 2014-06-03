/*
 * @AbstractStep.java 1.0 May 29, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.wizard;

import py.una.med.base.dynamic.forms.Field;
import py.una.med.base.dynamic.forms.ToolBar;

/**
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public abstract class AbstractStep extends Field implements Step {

	private ToolBar toolBar;
	private String description;

	@Override
	public ToolBar getToolBar() {

		if (toolBar == null) {
			toolBar = new ToolBar();
		}
		return toolBar;
	}

	/**
	 * @param toolBar
	 *            toolBar para setear
	 */
	public void setToolBar(ToolBar toolBar) {

		this.toolBar = toolBar;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public void initialize() {

		// ninguna acción por defecto
	}
}
