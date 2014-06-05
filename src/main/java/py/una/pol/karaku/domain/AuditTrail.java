/*
 * 
 */
package py.una.pol.karaku.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Tabla que representa la auditoria
 * 
 * @author Romina Fernandez, Arturo Volpe
 * @version 1.0
 * @since 1.0
 * 
 */
@Entity
@Table(name = "audit_trail")
@SequenceGenerator(name = "AUDIT_TRAIL_SEQ", sequenceName = "audit_trail_id_seq")
public class AuditTrail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "AUDIT_TRAIL_SEQ")
	private Long id;

	private String username;

	private String ip;

	@OneToMany(mappedBy = "header")
	private List<AuditTrailDetail> details;

	private String methodSignature;

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(String user) {

		this.username = user;
	}

	public String getIp() {

		return ip;
	}

	public void setIp(String ip) {

		this.ip = ip;
	}

	public void setMethodSignature(String methodSignature) {

		this.methodSignature = methodSignature;
	}

	public String getMethodSignature() {

		return methodSignature;
	}

	public List<AuditTrailDetail> getDetails() {

		return details;
	}

	public void setDetails(List<AuditTrailDetail> details) {

		this.details = details;
	}

}
