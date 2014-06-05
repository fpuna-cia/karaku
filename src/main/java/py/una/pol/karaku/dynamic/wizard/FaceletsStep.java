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
