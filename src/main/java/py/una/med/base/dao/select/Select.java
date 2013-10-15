/*
 * @Select.java 1.0 Oct 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.select;

import java.util.HashSet;
import java.util.Set;
import py.una.med.base.util.StringUtils;

/**
 * Parte Select de una consulta, tícamente una proyección.
 *
 * <p>
 * Define una lista de columnas a ser seleccionadas mediante el atributo
 * {@link #getAttributes()}, y se pueden añadir más columnas con el método
 * {@link #addAttribute(String)}.
 * </p>
 *
 * <p>
 * <b>Formato de columnas</b>: se utiliza la misma convención que en todo el
 * DAO, es decir, si se encuentra un punto en la cadena que representa a la
 * columna, entonces se asume que es un atributo del atributo anterior (pais en
 * ciudad.pais, es un atributo de ciudad, que a su ves es un atributo de la
 * clase root).
 * </p>
 *
 * <h3>Ejemplos</h3>
 * <p>
 *
 * <pre>
 * Select.columns(&quot;id&quot;, &quot;ciudad.id&quot;)
 * </pre>
 *
 * Provocará que la consulta solamente traiga el atributo #id de la clase root,
 * además creará un join automático con la tabla ciudad, y seleccionara
 * solamente el atributo id de la misma.
 *
 * </p>
 *
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 13, 2013
 *
 */
public final class Select {

	private Set<String> columns;

	private Select() {

	}

	/**
	 * Crea un {@link Select} con una lista de atributos a proyectar.
	 *
	 * @param attributes
	 *            lista de atributos
	 * @return {@link Select} con las columnas a proyectar.
	 */
	public static Select build(String ... attributes) {

		return new Select().addAttributes(attributes);
	}

	/**
	 * Agrega un atributo más para proyectar.
	 *
	 * <p>
	 * No se agregan atributos repetidas.
	 * </p>
	 *
	 * @param attribute
	 *            con el formato de (ATRIBUTO.?)*
	 */
	public Select addAttribute(String attribute) {

		if (StringUtils.isValid(attribute)) {
			getAttributes().add(attribute);
		}
		return this;
	}

	/***
	 * Agrega más atributos a proyectar.
	 *
	 * <p>
	 * No se agregan atributos repetidas.
	 * </p>
	 *
	 * @param attributes
	 *            atributos con el formato de (ATRIBUTO.?)*
	 */
	public Select addAttributes(String ... attributes) {

		if (attributes != null) {
			for (String s : attributes) {
				addAttribute(s);
			}
		}
		return this;
	}

	/**
	 * Colección de atributos a proyectar.
	 *
	 *
	 * @return atributos a ser proyectados.
	 */
	public Set<String> getAttributes() {

		if (columns == null) {
			columns = new HashSet<String>();
		}

		return columns;
	}
}
