/*
 * @TipoObjeto 1.0 27/05/13. Sistema Integral de Gestion Hospitalaria.
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
@Table(name = "tipo_objeto")
@SequenceGenerator(name = "TIPO_OBJETO_SEQ", sequenceName = "tipo_objeto_id_seq")
public class TipoObjeto extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_OBJETO_SEQ")
	private Long id;

	@NotNull
	@Pattern(regexp = ValidationConstants.WORD, message = "{ONLY_STRING}")
	@Size(max = 50, message = "{LENGTH_MAX}")
	@DisplayName(key = "{TIPO_OBJETO_NOMBRE}")
	@Unique(value = "uq_tipo_objeto_nombre")
	private String nombre;

	@Override
	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getNombre() {

		return nombre;
	}

	public void setNombre(String nombre) {

		this.nombre = nombre;
	}

}
