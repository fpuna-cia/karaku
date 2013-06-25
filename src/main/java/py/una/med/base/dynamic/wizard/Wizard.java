/*
 * @Wizard.java 1.0 May 29, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.wizard;

import java.util.List;

/**
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public interface Wizard {

	/**
	 * Longitud máxima del titulo del wizard
	 */
	int TITLE_MAX_LENGTH = 100;

	/**
	 * Lista de pasos que componen este wizard
	 * 
	 * @return {@link List} de pasos
	 */
	List<Step> getSteps();

	/**
	 * Reinicia todos los pasos dados por {@link #getSteps()}
	 */
	void initialize();

	/**
	 * Se mueve al siguiente paso, si {@link #validateEachStep()} retorna
	 * <code>true</code>, este método valida el contenido del <b>paso
	 * actual</b>, si falla se mantiene en el paso actual, en caso contrario
	 * avanza.
	 * 
	 * @return El {@link Step} actual si la validación falla, o el siguiente
	 *         {@link Step} si no hay problemas de validación.
	 */
	Step next();

	/**
	 * Vuelve al paso anterior, sin modificar el contenido del paso actual, esto
	 * es, si el paso actual sufrio alguna modificación, estas persisten.
	 * 
	 * @return {@link Step} anterior
	 */
	Step previous();

	/**
	 * Bandera que representa si cada paso debe ser validado.
	 * 
	 * @return <code>true</code> si cada paso (AJAX) debe ser validado,
	 *         <code>false</code> si no se requiere la validación
	 */
	boolean validateEachStep();

	/**
	 * Retorna el numero del paso actual, con este resultado se puede invocar a
	 * {@link List#get(int)} sobre el resultado de {@link #getSteps()} se
	 * obtiene el paso actual.
	 * 
	 * @return entero positivo entre 0 y size({@link #getSteps()})
	 */
	int getCurrentStepNumber();

	/**
	 * Retorna el titulo del wizard. Esta cadena debe resumir la función del
	 * wizard, y no debe ser muy extensa. Su longitud va de 0 a
	 * {@value #TITLE_MAX_LENGTH}
	 * 
	 * @return Cadena que explica el funcionamiento del wizard.
	 */
	String getTitle();

	/**
	 * Retorna el ID del panel, este id es importante pues se utiliza en la
	 * definición de los botones, y además es el identificador único de todo
	 * aquello que forma parte del wizard.
	 * 
	 * @return
	 */
	String getTogglePanelId();
}
