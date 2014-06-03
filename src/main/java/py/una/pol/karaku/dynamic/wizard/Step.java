/*
 * @Step.java 1.0 May 29, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dynamic.wizard;

import py.una.pol.karaku.dynamic.forms.ToolBar;

/**
 * Se definen las funciones básicas que debe cumplir cada paso del wizard.
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public interface Step {

	/**
	 * Inicializa el step a su estado inicial, puede ser usado tanto para
	 * empezar el {@link Wizard} o para reiniciarlo
	 */
	void initialize();

	/**
	 * Retorna el toolbar del paso actual
	 * 
	 * @return {@link ToolBar} vacío o con el estado actual (nunca null).
	 */
	ToolBar getToolBar();

	/**
	 * Explicación de las acciones que se llevan en este paso, si se retorna
	 * null o cadena vacia, la parte de descripcion es omitida
	 * 
	 * @return descripcion
	 */
	String getDescription();
	void setDescription(String description);
}
