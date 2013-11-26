/*
 * @Entity.java 1.0 Nov 4, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.replication.layers;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.envers.Audited;
import py.una.med.base.dao.entity.annotations.URI;
import py.una.med.base.dao.entity.annotations.URI.Type;
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;

/**
 * Entidad para los test de replicación.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 * 
 */
@Audited
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "description"),
		@UniqueConstraint(columnNames = "uri") })
public class ReplicatedEntity extends BaseEntity implements DTO, Shareable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101207945256545348L;

	/**
	 * Clave primaria igual al 80% de los casos implementados.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Clave única dentro del sistema
	 */

	@URI(baseUri = "test", type = Type.SEQUENCE, sequenceName = "tt1")
	private String uri;

	/**
	 * Atributo comun para realizar pruebas
	 */
	private String description;

	private boolean active;

	@OneToMany(mappedBy = "father")
	private Set<ReplicatedEntityChild> childs;

	/**
	 * @return id
	 */
	@Override
	public Long getId() {

		return id;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	@Override
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @return uri
	 */
	@Override
	public String getUri() {

		return uri;
	}

	/**
	 * @param uri
	 *            uri para setear
	 */
	public void setUri(String uri) {

		this.uri = uri;
	}

	/**
	 * @return description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * @param description
	 *            description para setear
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * @return active
	 */
	public Boolean getActive() {

		return active;
	}

	/**
	 * @param active
	 *            active para setear
	 */
	public void setActive(Boolean active) {

		this.active = active;
	}

	/**
	 * @return
	 */
	@Override
	public boolean isActive() {

		return active;
	}

	/**
	 * @return childs
	 */
	public Set<ReplicatedEntityChild> getChilds() {

		return childs;
	}

	/**
	 * @param childs
	 *            childs para setear
	 */
	public void setChilds(Set<ReplicatedEntityChild> childs) {

		this.childs = childs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate() {

		active = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {

		active = true;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ReplicatedEntity) {
			ReplicatedEntity oth = (ReplicatedEntity) obj;
			if ((uri == null) && (oth.uri == null)) {
				return true;
			}
			return uri.equals(oth.uri) && description.equals(oth.description);
		}
		return false;
	}

	@Override
	public String toString() {

		return uri;
	}
}
