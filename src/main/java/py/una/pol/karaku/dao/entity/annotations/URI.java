/*
 * @URI.java 1.0 Nov 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.entity.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.pol.karaku.dao.annotations.CaseSensitive;

/**
 * Define un atributo como una posible URI.
 * 
 * <p>
 * Una URI es un identificador único dentro del universo de un objeto, por lo
 * tanto, es una clave candidata en su tabla, debe ser una cadena.
 * </p>
 * 
 * <p>
 * Un atributo marcado con esta anotación es, además {@link CaseSensitive}, pues
 * las uri son case sensitive.
 * </p>
 * <p>
 * Las técnicas de generación de URI dependen de la entidad, pero se agrupan en
 * dos mecanismos:
 * <ul>
 * <li>Por atributo única ({@link Type#FIELD}): un atributo único en una entidad
 * puede servir como uri de la misma, anexando antes la URI única del sistema.
 * Por ejemplo, una URI generada de esta forma se compone de:
 * 
 * <pre>
 * med.una.py / karaku / entity / UNIQUE_COLUMN
 * 
 * y se define:
 * 
 * {@literal @}URI(field="unique", type=FIELD, baseUri="med.una.py/karaku/entity")
 * String uri;
 * </pre>
 * 
 * </li>
 * <li>Por una secuencia ({@link Type#SEQUENCE}): se utiliza una secuencia
 * especial para generar números únicos para una entidad.
 * 
 * <pre>
 * med.una.py / karaku / entity / SEQUENCE
 * 
 * {@literal @}URI(type=SEQUENCE, sequenceName="sequence_1", baseUri="med.una.py/karaku/entity")
 * String uri;
 * </pre>
 * 
 * </li>
 * 
 * </ul>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FieldAnnotation
@CaseSensitive
public @interface URI {

	/**
	 * Atributo único.
	 * 
	 * <p>
	 * El nombre de un atributo único, debe ser un atributo de la entidad o de
	 * alguna de sus superclases.
	 * </p>
	 * 
	 * <p>
	 * Esta propiedad solo es tomada en cuenta si {@link #type()} es
	 * {@link Type#FIELD}
	 * </p>
	 * 
	 * @return Nombre del atributo
	 */
	String field() default "";

	/**
	 * Nombre de la secuencia.
	 * 
	 * <p>
	 * El nombre de una secuencia definida en la base de datos.
	 * </p>
	 * 
	 * <p>
	 * Esta propiedad solo es tomada en cuenta si {@link #type()} es
	 * {@link Type#SEQUENCE}
	 * </p>
	 * 
	 * @return nombre de la secuencia
	 */
	String sequenceName() default "";

	/**
	 * Cadena base para las URI, esta cadena se antepondra a la URI generada.
	 * 
	 * @return cadena, no debe estar vacía ni debe ser nula.
	 */
	String baseUri();

	/**
	 * Tipo de la secuencia.
	 * 
	 * @see Type
	 * @return tipo de la secuencia, no nulo.
	 */
	Type type();

	/**
	 * Tipos de secuencia.
	 * 
	 * <p>
	 * Los tipos soportados son por secuencia, y por atributo.
	 * </p>
	 * 
	 * @author Arturo Volpe
	 * @since 2.2.8
	 * @version 1.0 Nov 6, 2013
	 * 
	 */
	enum Type {
		/**
		 * Define si se creará el URI de acuerdo a una secuencia.
		 */
		SEQUENCE,
		/**
		 * Define si se creará el URI de acuerdo a un atributo.
		 */
		FIELD
	}
}
