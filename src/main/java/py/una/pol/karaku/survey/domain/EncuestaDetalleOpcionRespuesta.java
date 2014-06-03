/*
 * @EncuestaDetalleOpcionRespuesta 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.domain;

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
