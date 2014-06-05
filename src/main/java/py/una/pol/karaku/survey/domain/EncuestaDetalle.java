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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "encuesta_detalle")
@SequenceGenerator(name = "ENCUESTA_DETALLE_SEQ", sequenceName = "encuesta_detalle_id_seq")
public class EncuestaDetalle extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_DETALLE_SEQ")
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_id")
	private Encuesta encuesta;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_plantilla_pregunta_id")
	private EncuestaPlantillaPregunta pregunta;

	private String respuesta;

	@Column(name = "nro_fila")
	private Integer numeroFila;

	@OneToMany(mappedBy = "encuestaDetalle", cascade = CascadeType.ALL)
	private List<EncuestaDetalleOpcionRespuesta> opcionRespuesta;

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public Encuesta getEncuesta() {

		return encuesta;
	}

	public void setEncuesta(Encuesta encuesta) {

		this.encuesta = encuesta;
	}

	public EncuestaPlantillaPregunta getPregunta() {

		return pregunta;
	}

	public void addDetalleOpcionRespuesta(
			EncuestaDetalleOpcionRespuesta opcionRespuesta) {

		if (this.opcionRespuesta == null) {
			this.opcionRespuesta = new ArrayList<EncuestaDetalleOpcionRespuesta>();
		}
		this.opcionRespuesta.add(opcionRespuesta);
	}

	public void setPregunta(EncuestaPlantillaPregunta pregunta) {

		this.pregunta = pregunta;
	}

	public String getRespuesta() {

		return respuesta;
	}

	public void setRespuesta(String respuesta) {

		this.respuesta = respuesta;
	}

	public Integer getNumeroFila() {

		return numeroFila;
	}

	public void setNumeroFila(Integer numeroFila) {

		this.numeroFila = numeroFila;
	}

	public List<EncuestaDetalleOpcionRespuesta> getOpcionRespuesta() {

		return opcionRespuesta;
	}

	public void setOpcionRespuesta(
			List<EncuestaDetalleOpcionRespuesta> opcionRespuesta) {

		this.opcionRespuesta = opcionRespuesta;
	}

	/**
	 * Obtiene el numero de orden de la pregunta asociada.
	 * 
	 * @return
	 */
	public Integer getNumeroPregunta() {

		return getPregunta().getOrden();
	}

}
