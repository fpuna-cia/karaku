/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.dynamic.wizard;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import org.richfaces.component.UIPopupPanel;
import org.richfaces.component.UITogglePanel;
import org.richfaces.component.behavior.ToggleControl;
import py.una.pol.karaku.dynamic.forms.Button;
import py.una.pol.karaku.dynamic.forms.Button.OnClickCallBack;
import py.una.pol.karaku.dynamic.forms.ButtonAction;
import py.una.pol.karaku.dynamic.forms.DynamicFormList;
import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.dynamic.tables.DataTable;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.ListHelper;
import py.una.pol.karaku.util.Util;

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
	private static final String FLOAT_RIGTH = "float:right";
	private static final String FLOAT_LEFT = "float:left";
	private final Step step;

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
		previous.setStyle(FLOAT_LEFT);
		previous.setImmediate(true);
		previous.addAjaxBehavior(ButtonAction.CLICK,
				KarakuComponentFactory.getToogleControl("@prev"));
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
		next.setStyle(FLOAT_RIGTH);

		ToggleControl control = KarakuComponentFactory.getToogleControl("@next");

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
	public StepBuilder addFinish(final OnClickCallBack callBack,
			final SimpleWizard wizard) {

		Button last = new Button();
		last.setText("KARAKU_WIZARD_FINISH");
		last.setStyle(FLOAT_RIGTH);
		last.setClickCallBack(new OnClickCallBack() {

			@Override
			public void onClick() {

				UITogglePanel utp = findComponentByID(wizard.getTogglePanelId());
				utp.setActiveItem(utp.getFirstItem().getName());
				callBack.onClick();
			}
		});

		step.getToolBar().addItem(last);
		return this;
	}

	/**
	 * Agrega los botones que son necesarios para un paso inicial (next)
	 *
	 * @see #addNext()
	 * @return this
	 */
	public StepBuilder addFirstButtons(SimpleWizard wizard) {

		addNext();
		addCancelButton(wizard);
		return this;
	}

	/**
	 * Agrega un botón que hace desaparecer al wizard
	 *
	 * @param wizard
	 * @return this
	 */
	public StepBuilder addCancelButton(final SimpleWizard wizard) {

		Button cancel = new Button();
		cancel.setText("KARAKU_WIZARD_CANCEL");
		cancel.setStyle(FLOAT_RIGTH);
		cancel.setClickCallBack(new OnClickCallBack() {

			@Override
			public void onClick() {

				UITogglePanel utp = findComponentByID(wizard.getTogglePanelId());
				utp.setActiveItem(utp.getFirstItem().getName());
				UIPopupPanel upp = findComponentByID(wizard.getPopupId());
				upp.setShow(false);

			}
		});
		step.getToolBar().addItem(cancel);
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
	public StepBuilder addLastButtons(OnClickCallBack callback,
			SimpleWizard wizard) {

		addFinish(callback, wizard);
		addPrevious();
		addCancelButton(wizard);
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

	/**
	 * Agrega un botón al estado actual
	 *
	 * @param boton
	 * @return this
	 */
	public StepBuilder addButon(Button boton) {

		boton.setStyle(FLOAT_RIGTH);
		step.getToolBar().addItem(boton);
		return this;
	}

	/**
	 * Agrega un botón ajax, que se ejecuta cuando se presiona click.
	 *
	 * @return this
	 */
	public StepBuilder addAjaxButton(String i18nKey,
			OnClickCallBack clickCallBack, boolean immediate) {

		return addAjaxButton(i18nKey, clickCallBack, immediate, true);
	}

	/**
	 * Agrega un botón ajax, que se ejecuta cuando se presiona click.
	 *
	 * @return this
	 */
	public StepBuilder addAjaxButton(String i18nKey,
			final OnClickCallBack clickCallBack, boolean immediate,
			boolean inLeft) {

		Button b = new Button();
		b.setText(i18nKey);
		b.setImmediate(immediate);
		b.setStyle(inLeft ? FLOAT_LEFT : FLOAT_RIGTH);

		AjaxBehavior ab = KarakuComponentFactory.getAjaxBehavior();
		ab.addAjaxBehaviorListener(new AjaxBehaviorListener() {

			@Override
			public void processAjaxBehavior(AjaxBehaviorEvent event) {

				clickCallBack.onClick();

			}
		});
		ab.setRender(ListHelper.getAsList("@form"));
		b.addAjaxBehavior(ButtonAction.ACTION, ab);
		step.getToolBar().addItem(b);
		return this;
	}

	@SuppressWarnings("unchecked")
	private <T extends UIComponent> T findComponentByID(String id) {

		ControllerHelper ch = Util.getSpringBeanByJSFContext(
				FacesContext.getCurrentInstance(), ControllerHelper.class);
		return (T) ch.findComponent(id);
	}
}
