/*
 * @TestEntity.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.util.layers;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Clase que representa a un hijo en la relación:
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

	@OneToOne
	private TestEntity father;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "father")
	private List<TestGrandChild> grandChilds;

	/**
	 * @return father
	 */
	public TestEntity getFather() {

		return father;
	}

	/**
	 * @param father father para setear
	 */
	public void setFather(TestEntity father) {

		this.father = father;
	}

	/**
	 * @return grandChilds
	 */
	public List<TestGrandChild> getGrandChilds() {

		return grandChilds;
	}

	/**
	 * @param grandChilds grandChilds para setear
	 */
	public void setGrandChilds(List<TestGrandChild> grandChilds) {

		this.grandChilds = grandChilds;
	}
}
