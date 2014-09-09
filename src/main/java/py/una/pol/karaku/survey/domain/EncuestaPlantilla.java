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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.util.ValidationConstants;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @author Arsenio Ferreira
 * @since 1.0
 * @version 1.1 25/06/2014
 * 
 */
@Entity
@Audited
@Table(name = "encuesta_plantilla")
@SequenceGenerator(name = "ENCUESTA_PLANTILLA_SEQ", sequenceName = "encuesta_plantilla_id_seq")
public class EncuestaPlantilla extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_PLANTILLA_SEQ")
	private Long id;

	@NotNull
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@NotNull
	@Size(max = 50, message = "{LENGTH_MAX}")
	@Column(name = "usuario_id")
	private String usuario;

	@NotNull
	@Size(min = 2, max = 2, message = "{LENGTH}")
	private String activo;

	@NotNull
	@Size(max = 30)
	@Column(name = "key")
	private String key;

	@NotNull
	@Size(max = 100, message = "{LENGTH_MAX}")
	@Pattern(regexp = ValidationConstants.WORDS_SPE, message = "{FORMAT_CAPITALIZED}")
	private String descripcion;

	@Override
	public Long getId() {

		return id;
	}

	@Override
	public void setId(Long id) {

		this.id = id;
	}

	public Date getFechaCreacion() {

		if (fechaCreacion != null) {
			return new Date(fechaCreacion.getTime());
		}
		return null;
	}

	public void setFechaCreacion(Date fechaCreacion) {

		if (fechaCreacion != null) {
			this.fechaCreacion = new Date(fechaCreacion.getTime());
		}
		this.fechaCreacion = null;
	}

	public String getUsuario() {

		return usuario;
	}

	public void setUsuario(String usuario) {

		this.usuario = usuario;
	}

	public String getActivo() {

		return activo;
	}

	public void setActivo(String activo) {

		this.activo = activo;
	}

	/**
	 * @return key
	 */
	public String getKey() {

		return key;
	}

	/**
	 * @param key
	 *            key para setear
	 */
	public void setKey(String key) {

		this.key = key;
	}

	public String getDescripcion() {

		return descripcion;
	}

	public void setDescripcion(String descripcion) {

		this.descripcion = descripcion;
	}

}
