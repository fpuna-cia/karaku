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
/**
 * Test para los servicios de replicaciÃ³n.
 * 
 * La organizaciÃ³n es como sigue:
 * 
 * <p>
 * <ol>
 * <li>Layers: se encuentran los objetos no mockeados que sera utilizados para
 * los test, como una entidad, dao y lÃ³gica.</li>
 * <li>Watchers: test que se encargan de detectar cambios y almacenar la
 * informaciÃ³n necesaria para que los proveedores puedan generar informacion.</li>
 * <li>Provider: se encuentran los test que se encargan de proveer datos</li>
 * <li>Consumer: se encuentran los test que consumen y se encargan de actualizar
 * tablas locales.</li>
 * 
 * </ol>
 * </p>
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 * 
 */
package py.una.pol.karaku.test.test.replication;