/*
 * @TipoBloque 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.survey.domain;

import java.io.Serializable;
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
import py.una.pol.karaku.model.DisplayName;
import py.una.pol.karaku.model.Unique;
import py.una.pol.karaku.util.ValidationConstants;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 27/05/2013
 * 
 */
@Entity
@Audited
@Table(name = "tipo_bloque")
@SequenceGenerator(name = "TIPO_BLOQUE_SEQ", sequenceName = "tipo_bloque_id_seq")
public class TipoBloque extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_BLOQUE_SEQ")
	private Long id;

	@NotNull
	@Pattern(regexp = ValidationConstants.WORDS, message = "{FORMAT_CAPITALIZED}")
	@Size(max = 15, message = "{LENGTH_MAX}")
	@DisplayName(key = "{TIPO_BLOQUE_DESCRIPCION}")
	@Unique(value = "uq_tipo_bloque_descripcion")
	private String descripcion;

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

}
