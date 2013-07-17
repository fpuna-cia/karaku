/*
 * @Encuesta 1.0 29/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.domain;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "encuesta")
@SequenceGenerator(name = "ENCUESTA_SEQ", sequenceName = "encuesta_id_seq")
public class Encuesta extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_SEQ")
	private Long id;

	@NotNull
	@Column(name = "fecha_realizacion")
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

		return fechaRealizacion;
	}

	public void setFechaRealizacion(Date fechaRealizacion) {

		this.fechaRealizacion = fechaRealizacion;
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

	public void addDetalle(EncuestaDetalle detalle) {

		if (this.detalles == null) {
			this.detalles = new ArrayList<EncuestaDetalle>();
		}
		this.detalles.add(detalle);
	}
}
