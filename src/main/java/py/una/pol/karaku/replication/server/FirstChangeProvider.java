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
package py.una.pol.karaku.replication.server;

import java.util.Collection;
import py.una.pol.karaku.replication.Shareable;

/**
 * Define un proveedor de cambios para la primera vez que se sincronizan.
 * 
 * 
 * <p>
 * El problema de la primera existe cuando una entidad necesita ser insertada en
 * un orden, los problemas que llevan a esto pueden ser:
 * <ol>
 * <li>Un elemento necesita que otro del mismo tipo este presente, esto ocurre
 * cuando se utilizan árboles (una tabla se referencia a sí misma).</li>
 * <li>Existe un orden por el cual se deban cargar y no existe otro mecanismo de
 * definir el mismo.</li>
 * </ol>
 * 
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
public interface FirstChangeProvider<T extends Shareable> {

	Integer MAX_PRIORITY = Integer.MAX_VALUE;

	Collection<? extends T> getChanges(Class<? extends T> clazz);

	Class<T> getSupportedClass();

	Integer getPriority();

}
