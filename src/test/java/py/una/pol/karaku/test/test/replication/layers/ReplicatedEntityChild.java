/*
 * @ReplicatedEntityChild.java 1.0 Nov 4, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.replication.layers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.envers.Audited;
import py.una.pol.karaku.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 * 
 */
@Audited
@Entity
public class ReplicatedEntityChild implements Shareable {

	/**
	 * Clave primaria igual al 80% de los casos implementados.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private ReplicatedEntity father;


	/**
	 * @return id
	 */
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
	 * @return father
	 */
	public ReplicatedEntity getFather() {

		return father;
	}

	/**
	 * @param father
	 *            father para setear
	 */
	public void setFather(ReplicatedEntity father) {

		this.father = father;
	}

	@Override
	public String getUri() {

		return id + "";
	}

	@Override
	public void inactivate() {

	}

	@Override
	public void activate() {

	}

	@Override
	public boolean isActive() {

		return false;
	}

}
