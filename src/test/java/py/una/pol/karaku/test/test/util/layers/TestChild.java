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

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Clase que representa a un hijo en la relaciÃ³n:
 * <p>
 * <code>
 * {@link TestEntity} -> <b>{@link TestChild}</b>-> {@link TestGrandChild}
 * </code>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Entity
@Table(name = "test_child")
public class TestChild extends BaseTestEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8799394149696934250L;

	@OneToOne
	private TestEntity father;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "father")
	private List<TestGrandChild> grandChilds;

	/**
	 * @return father
	 */
	public TestEntity getFather() {

		return this.father;
	}

	/**
	 * @param father
	 *            father para setear
	 */
	public void setFather(TestEntity father) {

		this.father = father;
	}

	/**
	 * @return grandChilds
	 */
	public List<TestGrandChild> getGrandChilds() {

		return this.grandChilds;
	}

	/**
	 * @param grandChilds
	 *            grandChilds para setear
	 */
	public void setGrandChilds(List<TestGrandChild> grandChilds) {

		this.grandChilds = grandChilds;
	}
}
