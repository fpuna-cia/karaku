/*
 * @EncuestaPlantillaBloque 1.0 29/05/13. Sistema Integral de Gestion
 * Hospitalaria
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
@Table(name = "encuesta_plantilla_bloque")
@SequenceGenerator(name = "ENCUESTA_PLANTILLA_BLOQUE_SEQ", sequenceName = "encuesta_plantilla_bloque_id_seq")
public class EncuestaPlantillaBloque extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_PLANTILLA_BLOQUE_SEQ")
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_plantilla_id")
	private EncuestaPlantilla encuestaPlantilla;

	@NotNull
	@Size(max = 50, message = "{LENGTH_MAX}")
	private String titulo;

	@NotNull
	private Integer orden;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "tipo_bloque_id")
	private TipoBloque tipoBloque;

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public EncuestaPlantilla getEncuestaPlantilla() {

		return encuestaPlantilla;
	}

	public void setEncuestaPlantilla(EncuestaPlantilla encuestaPlantilla) {

		this.encuestaPlantilla = encuestaPlantilla;
	}

	public String getTitulo() {

		return titulo;
	}

	public void setTitulo(String titulo) {

		this.titulo = titulo;
	}

	public Integer getOrden() {

		return orden;
	}

	public void setOrden(Integer orden) {

		this.orden = orden;
	}

	public TipoBloque getTipoBloque() {

		return tipoBloque;
	}

	public void setTipoBloque(TipoBloque tipoBloque) {

		this.tipoBloque = tipoBloque;
	}

}
