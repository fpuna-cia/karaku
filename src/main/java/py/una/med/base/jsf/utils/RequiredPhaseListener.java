/*
 * @RequiredPhaseListener.java 1.0 Jul 24, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PhaseId;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

	private HashMap<String, Field> hashedFields;

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

		hashedFields = new HashMap<String, Field>(10);
		lastOutput = null;
		markLabels4Required(FacesContext.getCurrentInstance().getViewRoot());

	}

	private void markLabels4Required(UIComponent parent) {

		// String marker = " *";
		Iterator<UIComponent> kids = parent.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent child = kids.next();
			processUIComponent(child);
			markLabels4Required(child);
		}

	}

	private void processUIComponent(UIComponent uiComponent) {

		if (uiComponent instanceof UIInput) {
			processInput((UIInput) uiComponent);
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

	private void processInput(UIInput input) {

		if (input instanceof UIInput) {
			processUIInput(input);
		}
		if (input instanceof HtmlInputText) {
			processHtmlInput((HtmlInputText) input);
		}
	}

	public void processUIInput(UIInput input) {

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
				Field f = getField(input);
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
		if (addClass) {
			markOutputRequired(lastOutput);
			markInputRequired(input);
		}
	}

	private void processHtmlInput(HtmlInputText htmlInputText) {

		int max = htmlInputText.getMaxlength();
		if (max <= 0) {
			Field f = getField(htmlInputText);
			if (f != null) {
				Size s = f.getAnnotation(Size.class);
				if (s != null) {
					max = s.max();
				}
			}
		}
		htmlInputText.setMaxlength(max);

		if (getField(htmlInputText) != null) {
			// TODO ver cuando ya tiene
			Field f = getField(htmlInputText);
			Pattern p = f.getAnnotation(Pattern.class);
			if (p != null && p.regexp() != null) {
				htmlInputText.setOnkeypress(getFunction(p.regexp()));
			}
		}
	}

	/**
	 * @param regexp
	 * @return
	 */
	private String getFunction(String regexp) {

		return "var theEvent = window.event; "
				+ "var key = theEvent.keyCode || theEvent.which; "
				+ "key = String.fromCharCode( key ); " + "var regex = /^"
				+ regexp + "$/;" + "if( !regex.test(key) ) {"
				+ "	theEvent.returnValue = false; "
				+ "	if(theEvent.preventDefault) "
				+ "		theEvent.preventDefault(); " + "}";
	}

	private Field getField(UIComponent component) {

		if (hashedFields.containsKey(component.getId())) {
			return hashedFields.get(component.getId());
		}

		Field toRet;
		if (component.getValueExpression("value") == null) {
			toRet = null;
		} else {
			String beanExpression = component.getValueExpression("value")
					.getExpressionString();
			if (!(beanExpression == null || "".equals(beanExpression))) {
				toRet = ELHelper.getFieldByExpression(beanExpression);
			} else {
				toRet = null;
			}
		}
		hashedFields.put(component.getId(), toRet);
		return toRet;
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