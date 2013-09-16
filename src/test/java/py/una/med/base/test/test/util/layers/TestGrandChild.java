/*
 * @TestEntity.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.util.layers;

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

	@ManyToOne
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
