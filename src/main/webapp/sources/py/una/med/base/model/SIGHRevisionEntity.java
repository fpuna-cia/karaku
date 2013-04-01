package py.una.med.base.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import py.una.med.base.audit.SIGHRevisionListener;
/**
 * 
 * 
 * @author Romina Fernandez, Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
@Entity
@RevisionEntity(SIGHRevisionListener.class)
public class SIGHRevisionEntity implements Serializable {

	private static final long serialVersionUID = 1;

	@Id
	@GeneratedValue
	@RevisionNumber
	private Long id;

	@RevisionTimestamp
	@Column(name = "sello_tiempo")
	private long timestamp;

	private String username;

	private String ip;

	/**
	 * 
	 * @return Id principal de la entidad
	 */
	public Long getId() {

		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * 
	 * @return Fecha en la cual se realizo la revision
	 */
	public long getTimestamp() {

		return timestamp;
	}

	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp) {

		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @return actor principal del hecho
	 */
	public String getUsername() {

		return username;
	}

	/**
	 * 
	 * @param username
	 */
	public void setUsername(String username) {

		this.username = username;
	}

	/**
	 * 
	 * @return direccion desde la cual se realizo el hecho
	 */
	public String getIp() {

		return ip;
	}

	/**
	 * 
	 * @param ip
	 */
	public void setIp(String ip) {

		this.ip = ip;
	}
}
