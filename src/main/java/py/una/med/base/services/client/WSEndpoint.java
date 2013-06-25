/*
 * @WSEndpoint.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.envers.Audited;
import py.una.med.base.dao.annotations.CaseSensitive;
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.model.DisplayName;
import py.una.med.base.util.ValidationConstants;

/**
 * Entidad que representa los endpoints a los que se accede,
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 11, 2013
 * 
 */
@Audited
@Entity
@Table(name = "ws_end_point")
@SequenceGenerator(name = "WS_ENDPOINT_SEQ", sequenceName = "ws_end_point_id_seq")
public class WSEndpoint extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WS_ENDPOINT_SEQ")
	private Long id;

	@Basic(optional = false)
	@DisplayName(key = "{KARAKU_ENDPOINT_URL}")
	@NotNull
	@CaseSensitive
	@Pattern(regexp = ValidationConstants.URL)
	private String url;

	@Basic(optional = false)
	@NotNull
	@CaseSensitive
	@DisplayName(key = "{KARAKU_ENDPOINT_KEY}")
	@Pattern(regexp = ValidationConstants.ALPHANUMERIC)
	private String key;

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
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @return url
	 */
	public String getUrl() {

		return url;
	}

	/**
	 * @param url
	 *            url para setear
	 */
	public void setUrl(String url) {

		this.url = url;
	}

	/**
	 * @return key
	 */
	public String getKey() {

		return key;
	}

	/**
	 * @param key
	 *            key para setear
	 */
	public void setKey(String key) {

		this.key = key;
	}

}
