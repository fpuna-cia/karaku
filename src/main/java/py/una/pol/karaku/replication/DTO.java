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
package py.una.pol.karaku.replication;

/**
 * Determina un objeto válido para la replicación.
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 * 
 */
public interface DTO {

	/**
	 * Retorna una cadena que identifica de manera única a este objeto.
	 * 
	 * <p>
	 * Las técnicas de generación de URI dependen de la entidad, pero se agrupan
	 * en tres mecanismos:
	 * <ul>
	 * <li>Por columna única: una columna única en una entidad puede servir como
	 * uri de la misma, anexando antes la URI única del sistema. Por ejemplo,
	 * una URI generada de esta forma se compone de:
	 * 
	 * <pre>
	 * med.una.py / karaku / entity / UNIQUE_COLUMN
	 * </pre>
	 * 
	 * </li>
	 * <li>Por una secuencia: se utiliza una secuencia especial para generar
	 * números únicos para una entidad.
	 * 
	 * <pre>
	 * med.una.py / karaku / entity / SEQUENCE
	 * </pre>
	 * 
	 * </li>
	 * 
	 * </ul>
	 * </p>
	 * 
	 * @return cadena única
	 * @see py.una.pol.karaku.dao.entity.annotations.URI
	 */
	String getUri();

	/**
	 * Verifica si la entidad esta o no activa.
	 * 
	 * <p>
	 * Una entidad borrada lógicamente no debería ser utilizada para ningúna
	 * operación.
	 * </p>
	 * 
	 * @return <code>true</code> si esta activa, <code>false</code> si se la ha
	 *         borrado lógicamente.
	 */
	boolean isActive();
}
