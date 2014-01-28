/*
 * @EncuestaPlantillaPregunta 1.0 27/05/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.domain;

import java.io.Serializable;
import java.util.List;
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
@Table(name = "encuesta_plantilla_pregunta")
@SequenceGenerator(name = "ENCUESTA_PLANTILLA_PREGUNTA_SEQ", sequenceName = "encuesta_plantilla_pregunta_id_seq")
public class EncuestaPlantillaPregunta extends BaseEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENCUESTA_PLANTILLA_PREGUNTA_SEQ")
	private Long id;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "encuesta_plantilla_bloque_id")
	private EncuestaPlantillaBloque bloque;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "tipo_objeto_id")
	private TipoObjeto tipoObjeto;

	@NotNull
	@Size(max = 100, message = "{LENGTH_MAX}")
	private String descripcion;

	@NotNull
	private Integer orden;

	@NotNull
	@Size(min = 2, max = 2, message = "{LENGTH}")
	private String obligatoria;

	@Size(max = 50, message = "{LENGTH_MAX}")
	private String tag;

	@Column(name = "longitud_respuesta")
	private Integer longitudRespuesta;

	@NotNull
	@Size(min = 2, max = 2, message = "{LENGTH}")
	@Column(name = "editable")
	private String editable;

	@OneToMany(mappedBy = "pregunta")
	private List<OpcionRespuesta> opcionRespuesta;

	@Override
	public Long getId() {

		return id;
	}

	@Override
	public void setId(Long id) {

		this.id = id;
	}

	public EncuestaPlantillaBloque getBloque() {

		return bloque;
	}

	public void setBloque(EncuestaPlantillaBloque bloque) {

		this.bloque = bloque;
	}

	public TipoObjeto getTipoObjeto() {

		return tipoObjeto;
	}

	public void setTipoObjeto(TipoObjeto tipoObjeto) {

		this.tipoObjeto = tipoObjeto;
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

	public String getObligatoria() {

		return obligatoria;
	}

	public void setObligatoria(String obligatoria) {

		this.obligatoria = obligatoria;
	}

	public String getTag() {

		return tag;
	}

	public void setTag(String tag) {

		this.tag = tag;
	}

	public Integer getLongitudRespuesta() {

		return longitudRespuesta;
	}

	public void setLongitudRespuesta(Integer longitudRespuesta) {

		this.longitudRespuesta = longitudRespuesta;
	}

	public String getEditable() {

		return editable;
	}

	/**
	 * Define si una pregunta es editable.
	 * 
	 * <p>
	 * Ciertas preguntas no son editables, por ejemplo algunas que se cargan con
	 * datos de un servicio o aquellas que no pueden cambiar.
	 * </p>
	 * 
	 * @return <code>true</code> si es editable, <code>false</code> en caso
	 *         contrario.
	 */
	public boolean isEditable() {

		return "SI".equals(editable);
	}

	/**
	 * Define si una pregunta es obligatoria o no.
	 * 
	 * <p>
	 * Ciertas preguntas son obligatorias, por ejemplo el nombre en una encuesta
	 * no anonima..
	 * </p>
	 * 
	 * @return <code>true</code> si es obligatoria, <code>false</code> en caso
	 *         contrario.
	 */
	public boolean isObligatoria() {

		return "SI".equals(obligatoria);
	}

	public void setEditable(String editable) {

		this.editable = editable;
	}

	public List<OpcionRespuesta> getOpcionRespuesta() {

		return opcionRespuesta;
	}

	public void setOpcionRespuesta(List<OpcionRespuesta> opcionRespuesta) {

		this.opcionRespuesta = opcionRespuesta;
	}

	/**
	 * Verifica si el tipo de objeto de la pregunta es un Ckeck.
	 * 
	 * @return
	 */
	public boolean isCheck() {

		return "CHECK".equals(getTipoObjeto().getNombre());
	}

	/**
	 * Verifica si el tipo de objeto de la pregunta es un SelectOneRadio.
	 * 
	 * @return
	 */
	public boolean isRadio() {

		return "RADIO".equals(getTipoObjeto().getNombre());
	}

}
