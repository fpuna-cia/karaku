/*
 * @TestEntity.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.util.layers;

import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import py.una.med.base.domain.BaseEntity;

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
