package py.una.med.base.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Detalle de una auditoria
 * 
 * @author Romina Fernandez, Arturo Volpe
 * @version 1.0
 * @since 1.0
 * 
 */
@Entity
public class AuditTrailDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private AuditTrail header;

	private Serializable value;

	private String expression;

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public AuditTrail getHeader() {

		return header;
	}

	public void setHeader(AuditTrail header) {

		this.header = header;
	}

	public Serializable getValue() {

		return value;
	}

	public void setValue(Serializable value) {

		this.value = value;
	}

	public String getExpression() {

		return expression;
	}

	public void setExpression(String expression) {

		this.expression = expression;
	}

}
