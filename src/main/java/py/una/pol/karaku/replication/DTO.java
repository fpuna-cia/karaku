/*
 * @Shareable.java 1.0 Nov 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.replication;

/**
 * Determina un objeto válido para la replicación.
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 * 
 */
public interface DTO {

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
	 * med.una.py / karaku / entity / UNIQUE_COLUMN
	 * </pre>
	 * 
	 * </li>
	 * <li>Por una secuencia: se utiliza una secuencia especial para generar
	 * números únicos para una entidad.
	 * 
	 * <pre>
	 * med.una.py / karaku / entity / SEQUENCE
	 * </pre>
	 * 
	 * </li>
	 * 
	 * </ul>
	 * </p>
	 * 
	 * @return cadena única
	 * @see py.una.pol.karaku.dao.entity.annotations.URI
	 */
	String getUri();

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
