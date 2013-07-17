/*
 * @IDynamicSurveyBlock.java 1.0 04/06/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.components;

import java.util.List;

/**
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 04/06/2013
 * 
 */
public interface IDynamicSurveyBlock {

	/**
	 * @return Lista de strings que representan los label mostrados al usuario.
	 */
	List<String> getLabels();

	String getType();

	/**
	 * @return
	 */
	String getId();
}
