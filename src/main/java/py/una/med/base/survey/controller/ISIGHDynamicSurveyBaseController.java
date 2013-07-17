/*
 * @ISIGHDynamicSurveyBaseController.java 1.0 03/06/13. Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.survey.controller;

import java.util.List;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.survey.components.DynamicSurveyBlock;
import py.una.med.base.survey.domain.Encuesta;
import py.una.med.base.survey.domain.EncuestaPlantilla;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 03/06/2013
 * 
 */
public interface ISIGHDynamicSurveyBaseController {

	/**
	 * @return
	 */
	List<DynamicSurveyBlock> getBlocks();

	/**
	 * @param block
	 */
	void setBlocks(List<DynamicSurveyBlock> block);

	/**
	 * @param bean
	 */
	void setBean(Encuesta bean);

	/**
	 * @return
	 */
	Encuesta getBean();

	/**
	 * @return
	 */
	ISIGHBaseLogic<Encuesta, Long> getBaseLogic();

	/**
	 * Crea una nueva encuesta y redirecciona a la vista principal (a la que
	 * llamo a esta escuesta)
	 * 
	 * @return
	 */
	String doCreate();

	/**
	 * @return
	 */
	void preCreate(EncuestaPlantilla template, Encuesta survey, boolean clone);

	/**
	 * @return
	 */
	String doCancel();

	/**
	 * @return
	 */
	boolean isNew();

	/**
	 * @return
	 */
	boolean isView();

	/**
	 * @return
	 */
	boolean isDelete();

	/**
	 * @return
	 */
	boolean isEditable();

	/**
	 * @return
	 */
	String doDelete();

}
