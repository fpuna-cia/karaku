/*
 * @XHTMLStep.java 1.0 Jun 6, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.wizard;

/**
 * Clase que sirve para reutilizar componentes escritos en Facelets (XHTML) y
 * añadirle las funcionalidades que tiene un {@link Step} convencional (toolbar
 * y navegación). <br>
 * Un posible ejemplo de lo que este {@link Step} es capaz de reutilizar son los
 * fields de los casos de uso (tanto simples como avanzados). <br>
 * <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 * "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 * 
 * <pre>
 * {@literal
 * <ui:composition xmlns="http://www.w3.org/1999/xhtml"
 * 	xmlns:h="http://java.sun.com/jsf/html"
 * 	xmlns:ui="http://java.sun.com/jsf/facelets"
 * 	xmlns:f="http://java.sun.com/jsf/core" xmlns:a4j="http://richfaces.org/a4j"
 * 	xmlns:rich="http://richfaces.org/rich"
 * 	xmlns:sigh="http://java.sun.com/jsf/composite/sigh">
 * 
 * 	... content ... 
 * </ui:composition>
 * }
 * </pre>
 * 
 * <br>
 * Las consideraciones adicionales que se deben tener son:
 * <ol>
 * <li>Automáticamente son encapsuladas en un formulario (el formulario del
 * wizard)</li>
 * <li>Automáticamente son encapsuladas en un datagrid de 6 columnas</li>
 * </ol>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 6, 2013
 * 
 */
public class FaceletsStep extends AbstractStep {

	private boolean aliased;
	private final String path;
	private String alias;
	private String value;

	/**
	 * Crea un nuevo paso que contendrá el facelets que se encuentre en el path
	 * pasado como parámetro.
	 * 
	 * @param xhtmlPath
	 *            ubicación del archivo, debe ser un path absoluto, el formato
	 *            debe ser absoluto, p.e.: '/views/SYSTEM/USE_CASE/fields.xhtml'
	 * 
	 */
	public FaceletsStep(String xhtmlPath) {

		path = xhtmlPath;
	}

	/**
	 * Retorna la ubicación del archivo facelets utilizado por este paso.
	 * 
	 * @return path ubicación del facelet que define este step
	 */
	public String getPath() {

		return path;
	}

	@Override
	public boolean disable() {

		getToolBar().disable();
		return false;
	}

	@Override
	public boolean enable() {

		getToolBar().enable();
		return false;
	}

	public void setAlias(String varName, String expression) {

		aliased = true;
		alias = varName;
		value = expression;
	}

	/**
	 * @return aliased
	 */
	public boolean isAliased() {

		return aliased;
	}

	/**
	 * @return alias
	 */
	public String getAlias() {

		return alias;
	}

	/**
	 * @return value
	 */
	public String getValue() {

		return value;
	}
}
