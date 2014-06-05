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
