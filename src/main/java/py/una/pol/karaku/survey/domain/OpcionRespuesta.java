/*
 * @OpcionRespuesta 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.domain;

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
import py.una.med.base.domain.BaseEntity;

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
