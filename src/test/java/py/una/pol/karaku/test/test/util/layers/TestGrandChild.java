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
package py.una.pol.karaku.test.test.util.layers;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * Clase que representa a un nieto en la relacion:
 * <p>
 * <code>
 * {@link TestEntity} -> {@link TestChild}-> <b>{@link TestGrandChild}</b>
 * </code>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Entity
@Table(name = "test_grand_child")
public class TestGrandChild extends BaseTestEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2841607668100128822L;
	@ManyToOne(optional = true)
	private TestChild father;

	/**
	 * @return father
	 */
	public TestChild getFather() {

		return father;
	}

	/**
	 * @param father
	 *            father para setear
	 */
	public void setFather(TestChild father) {

		this.father = father;
	}
}
