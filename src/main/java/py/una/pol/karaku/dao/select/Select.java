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
package py.una.pol.karaku.dao.select;

import java.util.HashSet;
import java.util.Set;
import py.una.pol.karaku.util.StringUtils;

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
