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
public class AuditTrailDetail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1292116113097030666L;

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
