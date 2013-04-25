package py.una.med.base.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Detalle de una auditoria
 * 
 * @author Romina Fernandez, Arturo Volpe
 * @version 1.0
 * @since 1.0
 * 
 */
@Entity
@Table(name = "audit_trail_detail")
@SequenceGenerator(name = "AUDIT_TRAIL_DETAIL_SEQ", sequenceName = "audit_trail_detail_id_seq")
public class AuditTrailDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "AUDIT_TRAIL_DETAIL_SEQ")
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
