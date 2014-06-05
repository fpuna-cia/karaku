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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Table(name = "opcion_respuesta")
@SequenceGenerator(name = "OPCION_RESPUESTA_SEQ", sequenceName = "opcion_respuesta_id_seq")
public class OpcionRespuesta extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1994591208621195887L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPCION_RESPUESTA_SEQ")
	private Long id;

	@NotNull
	@Size(max = 50, message = "{LENGTH_MAX}")
	private String descripcion;

	@NotNull
	private Integer orden;

	@NotNull
	@Size(min = 2, max = 2, message = "{LENGTH}")
	private String completar;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_plantilla_pregunta_id")
	private EncuestaPlantillaPregunta pregunta;

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getDescripcion() {

		return descripcion;
	}

	public void setDescripcion(String descripcion) {

		this.descripcion = descripcion;
	}

	public Integer getOrden() {

		return orden;
	}

	public void setOrden(Integer orden) {

		this.orden = orden;
	}

	public String getCompletar() {

		return completar;
	}

	public void setCompletar(String completar) {

		this.completar = completar;
	}

	public EncuestaPlantillaPregunta getPregunta() {

		return pregunta;
	}

	public void setPregunta(EncuestaPlantillaPregunta pregunta) {

		this.pregunta = pregunta;
	}

	/**
	 * Retorna true si la opcion permite un texto asociado.
	 * 
	 * @return
	 */
	public boolean isCompletar() {

		return "SI".equals(getCompletar());
	}

}
