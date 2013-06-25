/*
 * @StepFactory.java 1.0 Jun 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.wizard;

import javax.el.MethodExpression;
import javax.faces.event.MethodExpressionActionListener;
import org.richfaces.component.behavior.ComponentControlBehavior;
import org.richfaces.component.behavior.ToggleControl;
import py.una.med.base.dynamic.forms.Button;
import py.una.med.base.dynamic.forms.ButtonAction;
import py.una.med.base.dynamic.forms.DynamicFormList;
import py.una.med.base.dynamic.forms.SIGHComponentFactory;
import py.una.med.base.dynamic.tables.DataTable;
import py.una.med.base.util.ELHelper;
import py.una.med.base.util.I18nHelper;

/**
 * Fabrica de objetos del tipo {@link Step}, esta es una clase de conveniencia
 * para ocultar detalles verbosos como la creación de toolbars
 * 
 * <br />
 * Tipos Soportados
 * <ol>
 * <li>
 * <b>'form'</b>: {@link DynamicFormStep}</li>
 * <li><b>'table'</b>: {@link DataTableStep}</li>
 * </ol>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 5, 2013
 * 
 */
public class StepBuilder {

	/**
	 * 
	 */
	private static final String FLOAT = "float:right";
	private final Step step;
	private final ELHelper elHelper = ELHelper.INSTANCE;

	private StepBuilder(Step stepToBuild) {

		step = stepToBuild;
	}

	public static StepBuilder getDataGridBuilder(DataTable table) {

		return new StepBuilder(new DataTableStep(table));
	}

	public static StepBuilder getDynamicFormBuilder(
			DynamicFormList dynamicFormList) {

		return new StepBuilder(new DynamicFormStep(dynamicFormList));

	}

	public static FaceletsStepBuilder getFaceletsStepBuilder(String path) {

		return new FaceletsStepBuilder(new FaceletsStep(path));
	}

	public static StepBuilder getEmptyStep() {

		return new StepBuilder(new DynamicFormStep(null));
	}

	/**
	 * Agrega un botón que tiene el funcionamiento de volver atrás, este botón
	 * retrocede inmediatamente.
	 * 
	 * @return this
	 */
	public StepBuilder addPrevious() {

		Button previous = new Button();
		previous.setStyle(FLOAT);
		previous.setImmediate(true);
		previous.addAjaxBehavior(ButtonAction.CLICK,
				SIGHComponentFactory.getToogleControl("@prev"));
		previous.setText("KARAKU_WIZARD_PREVIOUS");
		step.getToolBar().addItem(previous);
		return this;
	}

	/**
	 * Agrega un botón al toolbar, este botón tiene el funcionamiento de
	 * siguiente, esto es al presionar avanza un paso en el {@link Wizard}, como
	 * es un botón ajax esta sujeto a las validaciones.
	 * 
	 * @return this
	 */
	public StepBuilder addNext() {

		Button next = new Button();
		next.setStyle(FLOAT);

		ToggleControl control = SIGHComponentFactory.getToogleControl("@next");

		next.addAjaxBehavior(ButtonAction.CLICK, control);
		next.setText("KARAKU_WIZARD_NEXT");

		step.getToolBar().addItem(next);
		return this;
	}

	/**
	 * Agrega un boton de finalización, que invoca a un método.
	 * 
	 * @param expression
	 *            método que retorna {@link Void} y recibe {@link Void}, esto
	 *            es, no retorna ni recibe nada.
	 * @return this
	 */
	public StepBuilder addFinish(String expression, SimpleWizard wizard) {

		Button last = new Button();
		MethodExpression callback = elHelper.makeMethodExpression(expression,
				void.class, void.class);
		last.addActionListener(new MethodExpressionActionListener(callback));
		last.setText("KARAKU_WIZARD_FINISH");
		last.setStyle(FLOAT);

		ComponentControlBehavior ccb = SIGHComponentFactory
				.getComponentControl(wizard.getPopupId(), "hide");
		last.addAjaxBehavior(ButtonAction.CLICK, ccb);
		step.getToolBar().addItem(last);
		return this;
	}

	/**
	 * Agrega los botones que son necesarios para un paso inicial (next)
	 * 
	 * @see #addNext()
	 * @return this
	 */
	public StepBuilder addFirstButtons() {

		addNext();
		return this;
	}

	/**
	 * Agregar los botones que son necesarios para un paso intermedio (previous,
	 * next)
	 * 
	 * @return this
	 * @see #addPrevious()
	 * @see #addNext()
	 */
	public StepBuilder addMiddleButtons() {

		addNext();
		addPrevious();
		return this;
	}

	public StepBuilder setDescription(String key) {

		step.setDescription(I18nHelper.getMessage(key));
		return this;
	}

	/**
	 * Dada una expresión genera los botones que son comunes a los últimos pasos
	 * de un wizard (previous, finish)
	 * 
	 * @param expression
	 * @see #addFinish(String)
	 * @see #addPrevious()
	 * @return this
	 */
	public StepBuilder addLastButtons(String expression, SimpleWizard wizard) {

		addPrevious();
		addFinish(expression, wizard);
		return this;
	}

	/**
	 * @return step
	 */
	protected Step getStep() {

		return step;
	}

	/**
	 * Retorna el paso que se esta construyendo actualmente
	 * 
	 * @return {@link Step} construido
	 */
	public Step build() {

		return step;
	}

	public static class FaceletsStepBuilder extends StepBuilder {

		public FaceletsStepBuilder(FaceletsStep step) {

			super(step);
		}

		public FaceletsStepBuilder addAlias(String varName, String expression) {

			((FaceletsStep) getStep()).setAlias(varName, expression);
			return this;
		}
	}
}
