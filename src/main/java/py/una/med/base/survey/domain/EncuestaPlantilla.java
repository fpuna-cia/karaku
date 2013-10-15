/*
 * @EncuestaPlantilla 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.domain;

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

	@Override
	public Long getId() {

		return id;
	}

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

}
