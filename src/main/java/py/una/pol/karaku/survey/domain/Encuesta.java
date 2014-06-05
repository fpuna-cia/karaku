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
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import py.una.pol.karaku.dao.entity.annotations.Time;
import py.una.pol.karaku.dao.entity.annotations.Time.Type;
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
@Table(name = "encuesta")
@SequenceGenerator(name = "ENCUESTA_SEQ", sequenceName = "encuesta_id_seq")
public class Encuesta extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_SEQ")
	private Long id;

	@NotNull
	@Column(name = "fecha_realizacion")
	@Time(type = Type.DATETIME)
	private Date fechaRealizacion;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_plantilla_id")
	private EncuestaPlantilla plantilla;

	@OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<EncuestaDetalle> detalles;

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public Date getFechaRealizacion() {

		if (fechaRealizacion != null) {
			return new Date(fechaRealizacion.getTime());
		}
		return null;
	}

	public void setFechaRealizacion(Date fechaRealizacion) {

		if (fechaRealizacion != null) {
			this.fechaRealizacion = new Date(fechaRealizacion.getTime());
		}
	}

	public EncuestaPlantilla getPlantilla() {

		return plantilla;
	}

	public void setPlantilla(EncuestaPlantilla plantilla) {

		this.plantilla = plantilla;
	}

	/**
	 * @return detalles
	 */
	public List<EncuestaDetalle> getDetalles() {

		return detalles;
	}

	/**
	 * @param detalles
	 *            detalles para setear
	 */
	public void setDetalles(List<EncuestaDetalle> detalles) {

		this.detalles = detalles;
	}
}
