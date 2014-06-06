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

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entidad que representa a un hijo en la relación:
 * <p>
 * <code>
 * <b>{@link TestEntity}</b> -> {@link TestChild}-> {@link TestGrandChild}
 * </code>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Entity
@Table(name = "test")
public class TestEntity extends BaseTestEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5218054809166947011L;

	@NotNull
	@Min(0L)
	@Max(500000L)
	private BigDecimal costo;

	@OneToOne(mappedBy = "father")
	private TestChild testChild;

	/**
	 * @return testChild
	 */
	public TestChild getTestChild() {

		return testChild;
	}

	/**
	 * @param testChild
	 *            testChild para setear
	 */
	public void setTestChild(TestChild testChild) {

		this.testChild = testChild;
	}

	/**
	 * @return costo
	 */
	public BigDecimal getCosto() {

		return costo;
	}

	/**
	 * @param costo
	 *            costo para setear
	 */
	public void setCosto(BigDecimal costo) {

		this.costo = costo;
	}
}
