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
package py.una.pol.karaku.survey.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import py.una.pol.karaku.domain.BaseEntity;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 29/05/2013
 * 
 */
@Entity
@Audited
@Table(name = "encuesta_detalle_opcion_respuesta")
@SequenceGenerator(name = "ENCUESTA_DETALLE_OPCION_RESPUESTA_SEQ", sequenceName = "encuesta_detalle_opcion_respuesta_id_seq")
public class EncuestaDetalleOpcionRespuesta extends BaseEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_DETALLE_OPCION_RESPUESTA_SEQ")
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_detalle_id")
	private EncuestaDetalle encuestaDetalle;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "opcion_respuesta_id")
	private OpcionRespuesta opcionRespuesta;

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public EncuestaDetalle getEncuestaDetalle() {

		return encuestaDetalle;
	}

	public void setEncuestaDetalle(EncuestaDetalle encuestaDetalle) {

		this.encuestaDetalle = encuestaDetalle;
	}

	public OpcionRespuesta getOpcionRespuesta() {

		return opcionRespuesta;
	}

	public void setOpcionRespuesta(OpcionRespuesta opcionRespuesta) {

		this.opcionRespuesta = opcionRespuesta;
	}

}
