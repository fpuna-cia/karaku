/*
 * @RequiredPhaseListener.java 1.0 Jul 24, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PhaseId;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.util.ELHelper;
import py.una.med.base.util.I18nHelper;

/**
 * 
 * Componente que se encarga de capturar la fase {@link PhaseId#RENDER_RESPONSE}
 * , y busca todos aquellos {@link UIInput} que sean requeridos, para agregarles
 * una clase (css) a fin de que luego mediante css puedan mostrarse alertas
 * visuales.
 * 
 * @see #preRenderView(ComponentSystemEvent)
 * @author Arturo Volpe Torres
 * @since 1.3.2
 * @version 1.0 25/07/2013
 */
@Component
public class RequiredPhaseListener implements Serializable {

	@Autowired
	I18nHelper helper;

	/**
	 * String que representa la clase de aquellas etiquetas de campos que son
	 * requeridos.
	 */
	public static final String REQUIRED_CLASS_LABEL = "required_label";
	/**
	 * String que representa la clase de aquellos campos que son requeridos.
	 */
	public static final String REQUIRED_CLASS_FIELD = "required_field";

	/**
	 * Cadena que representa el mensaje a ser mostrado cuando un {@link UIInput}
	 * requerido no fue ingreasdo.
	 */
	public static final String REQUIRED_MESSAGE = "javax.faces.component.UIInput.REQUIRED";
	/**
	 * 
	 */
	private static final long serialVersionUID = -5178965595775494541L;
	UIViewRoot _root;
	private UIOutput lastOutput;

	/**
	 * Se buscan aquellos {@link UIInput} que son requeridos, se define un campo
	 * requerido aquel que retorne en {@link UIInput#isRequired()}
	 * <code>true</code>, o que su field tenga la anotación {@link NotNull}. <br />
	 * Por cada campo encontrado se marca al {@link UIInput} como a su
	 * {@link UIOutput} precedente con las clases css
	 * {@link #REQUIRED_CLASS_FIELD} y {@link #REQUIRED_CLASS_LABEL}
	 * respectivamente.
	 * 
	 * @param componentSystemEvent
	 *            evento capturado
	 */
	public void preRenderView(ComponentSystemEvent componentSystemEvent) {

		_root = FacesContext.getCurrentInstance().getViewRoot();
		markLabels4Required(_root);
	}

	private void markLabels4Required(UIComponent parent) {

		// String marker = " *";
		for (UIComponent child : parent.getChildren()) {
			// if (child instanceof HtmlOutputLabel) {
			// HtmlOutputLabel label = (HtmlOutputLabel) child;
			// String targetId = label.getNamingContainer().getClientId()
			// + ":" + label.getFor();
			// UIComponent target = _root.findComponent(targetId);
			// if (target instanceof UIInput) {
			// UIInput input = (UIInput) target;
			// String labelText = label.getValue().toString();
			// if (input.isRequired() && !labelText.endsWith(marker)) {
			// label.setValue(labelText + marker);
			// }
			// if (!input.isRequired() && labelText.endsWith(marker)) {
			// label.setValue(labelText.substring(0,
			// labelText.length() - marker.length()));
			// }
			// }
			// }
			processUIComponent(child);
			markLabels4Required(child);
		}

		if (parent.getFacet(UIComponent.COMPOSITE_FACET_NAME) != null) {
			for (UIComponent child : parent.getFacet(
					UIComponent.COMPOSITE_FACET_NAME).getChildren()) {
				processUIComponent(child);
				markLabels4Required(child);
			}
		}
	}

	private void processUIComponent(UIComponent uiComponent) {

		if (uiComponent instanceof UIInput) {
			processUIInput((UIInput) uiComponent);
		}
		if (uiComponent instanceof UIOutput) {
			processUIOutput((UIOutput) uiComponent);
		}
	}

	/**
	 * @param uiComponent
	 */
	private void processUIOutput(UIOutput uiComponent) {

		lastOutput = uiComponent;
	}

	private void processUIInput(UIInput input) {

		if (input == null)
			return;

		boolean addClass = false;

		if (input.isRequired()) {
			addClass = true;
			input.setRequiredMessage(helper.getString(REQUIRED_MESSAGE));
		} else {
			if (input.getValueExpression("value") == null) {
				addClass = false;
			} else {
				String beanExpression = input.getValueExpression("value")
						.getExpressionString();
				if (!(beanExpression == null || "".equals(beanExpression))) {
					Field f = ELHelper.getFieldByExpression(beanExpression);
					if (f == null) {
						return;
					}
					if (f.getAnnotation(NotNull.class) != null) {
						if (lastOutput != null) {
							addClass = true;
						}
					}
				}
			}
		}
		if (addClass) {
			markOutputRequired(lastOutput);
			markInputRequired(input);
		}
	}

	/**
	 * @param input
	 */
	private void markInputRequired(UIInput input) {

		if (input instanceof HtmlInputText) {
			HtmlInputText hit = ((HtmlInputText) input);
			String style = hit.getStyleClass();
			hit.setStyleClass(appendRequiredClass(style, REQUIRED_CLASS_FIELD));
		}

	}

	private void markOutputRequired(UIOutput comp) {

		if (comp instanceof HtmlOutputText) {
			HtmlOutputText hot = ((HtmlOutputText) comp);
			String style = hot.getStyleClass();
			hot.setStyleClass(appendRequiredClass(style, REQUIRED_CLASS_LABEL));
		}
		if (comp instanceof HtmlOutputLabel) {
			HtmlOutputLabel hol = (HtmlOutputLabel) comp;
			hol.setStyleClass(appendRequiredClass(hol.getStyleClass(),
					REQUIRED_CLASS_LABEL));
		}
	}

	private String appendRequiredClass(String classes, String clase) {

		if (null == classes || "".equals(classes.trim())) {
			return clase;
		}
		return classes + " " + clase;
	}

}
