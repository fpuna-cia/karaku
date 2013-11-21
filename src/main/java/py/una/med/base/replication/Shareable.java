/*
 * @Shareable.java 1.0 Nov 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.replication;

/**
 * Determina si una entidad puede o no ser replicada a varios sistemas.
 *
 * <p>
 * Una entidad de este tipo tiene las siguientes propiedades:
 *
 * <ol>
 * <li>No es posible eliminarla, el DAO automáticamente la inactiva, véase
 * {@link #inactivate()}</li>
 * <li>No es posible realizar operaciones CRUB, si no se encuentra en el sistema
 * correcto, es decir, si no soy el dueño de una tabla, entonces no puedo operar
 * sobre entidades de ese tipo.</li>
 * </ol>
 * <p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 *
 */
public interface Shareable {

	/**
	 * Cadena que se recomienda utilizar para una entidad que esta activa.
	 *
	 * <p>
	 * El uso de esta constante es recomendada pero no obligatoria.
	 * </p>
	 * <p>
	 * Ejemplo:
	 *
	 * <pre>
	 * 	public boolean isActive() {
	 * 		return active == Shareable.YES;
	 * 	}
	 * 	...
	 * 	public boolean activate() {
	 * 		this.active = Shareable.YES;
	 * 	}
	 * </pre>
	 * <p>
	 */
	static String YES = "SI";

	/**
	 * Cadena que se recomienda utilizar para una entidad que ha sido eliminada
	 * lógicamente.
	 *
	 * <p>
	 * El uso de esta constante es recomendada pero no obligatoria.
	 * </p>
	 * <p>
	 * Ejemplo:
	 *
	 * <pre>
	 * 	public boolean isActive() {
	 * 		return active != Shareable.NO;
	 * 	}
	 * 	...
	 * 	public boolean inactivate() {
	 * 		this.active = Shareable.NO;
	 * 	}
	 * </pre>
	 * <p>
	 */
	static String NO = "NO";

	/**
	 * Retorna una cadena que identifica de manera única a este objeto.
	 *
	 * <p>
	 * Las técnicas de generación de URI dependen de la entidad, pero se agrupan
	 * en tres mecanismos:
	 * <ul>
	 * <li>Por columna única: una columna única en una entidad puede servir como
	 * uri de la misma, anexando antes la URI única del sistema. Por ejemplo,
	 * una URI generada de esta forma se compone de:
	 *
	 * <pre>
	 * med.una.py / sigh / entity / UNIQUE_COLUMN
	 * </pre>
	 *
	 * </li>
	 * <li>Por una secuencia: se utiliza una secuencia especial para generar
	 * números únicos para una entidad.
	 *
	 * <pre>
	 * med.una.py / sigh / entity / SEQUENCE
	 * </pre>
	 *
	 * </li>
	 *
	 * </ul>
	 * </p>
	 *
	 * @return cadena única
	 * @see py.una.med.base.dao.entity.annotations.URI
	 */
	String getUri();

	/**
	 * Realiza un borrado lógico de la entidad.
	 *
	 * @see #activate()
	 */
	void inactivate();

	/**
	 * Realiza el proceso inverso al borrado lógico.
	 *
	 */
	void activate();

	/**
	 * Verifica si la entidad esta o no activa.
	 *
	 * <p>
	 * Una entidad borrada lógicamente no debería ser utilizada para ningúna
	 * operación.
	 * </p>
	 *
	 * @return <code>true</code> si esta activa, <code>false</code> si se la ha
	 *         borrado lógicamente.
	 */
	boolean isActive();
}
