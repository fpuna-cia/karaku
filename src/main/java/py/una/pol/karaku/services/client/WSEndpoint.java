/*
 * @WSEndpoint.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.services.client;

import javax.persistence.Column;
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
import py.una.pol.karaku.dao.annotations.CaseSensitive;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.model.DisplayName;
import py.una.pol.karaku.util.ValidationConstants;

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

	private static final long serialVersionUID = 5879654731910374623L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WS_ENDPOINT_SEQ")
	private Long id;

	@NotNull
	@CaseSensitive
	@DisplayName(key = "{KARAKU_ENDPOINT_URL}")
	@Pattern(regexp = ValidationConstants.URL)
	private String url;

	@NotNull
	@CaseSensitive
	@DisplayName(key = "{KARAKU_ENDPOINT_KEY}")
	@Pattern(regexp = ValidationConstants.ALPHANUMERIC)
	private String key;

	@NotNull
	@DisplayName(key = "{KARAKU_ENDPOINT_PASSWORD}")
	@Pattern(regexp = ValidationConstants.ALPHANUMERIC)
	@CaseSensitive
	private String password;

	@NotNull
	@Column(name = "username")
	@DisplayName(key = "{KARAKU_ENDPOINT_USERNAME}")
	@Pattern(regexp = ValidationConstants.ALPHANUMERIC)
	@CaseSensitive
	private String user;

	@NotNull
	@Column(name = "internal_tag")
	@Size(max = 100)
	@DisplayName(key = "{KARAKU_ENDPOINT_USERNAME}")
	@Pattern(regexp = ValidationConstants.ALPHANUMERIC_SPE)
	private String internalTag;

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

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getUser() {

		return user;
	}

	public void setUser(String user) {

		this.user = user;
	}

	public String getInternalTag() {

		return internalTag;
	}

	public void setInternalTag(String internalTag) {

		this.internalTag = internalTag;
	}

}
