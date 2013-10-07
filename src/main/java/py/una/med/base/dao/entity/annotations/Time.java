/*
 * @Time.java 1.0 Oct 1, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.entity.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.med.base.util.FormatProvider;

/**
 * Define el formato de una fecha.
 * 
 * <p>
 * Esta anotación sirve para que el DAO automáticamente elimine los atributos de
 * un {@link java.util.Date} que no se desean persistir.
 * <p>
 * 
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FieldAnnotation
public @interface Time {

	/**
	 * Define el tipo de este atributo, por defecto es {@link Type#DATE}.
	 * 
	 * @return {@link Type} del atributo.
	 */
	Type type() default Type.DATE;

	/**
	 * Tipos de fechas soportados.
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Oct 7, 2013
	 * 
	 */
	static enum Type {
		/**
		 * Fecha sin horas ni minutos.
		 * <p>
		 * {@link FormatProvider#DATE_FORMAT} es el formato por defecto de este
		 * tipo de atributos
		 * </p>
		 */
		DATE,
		/**
		 * Horas y minutos.
		 * <p>
		 * Se eliminan el dia, el mes, el año, segundos y milisegundos
		 * </p>
		 * <p>
		 * {@link FormatProvider#TIME_FORMAT} es el formato por defecto de este
		 * tipo de atributos
		 * </p>
		 */
		TIME,
		/**
		 * Fechas con horas y minutos.
		 * <p>
		 * Se eliminan los segundos y milisegundos
		 * </p>
		 * <p>
		 * {@link FormatProvider#DATETIME_FORMAT} es el formato por defecto de
		 * este tipo de atributos
		 * </p>
		 */
		DATETIME
	}
}
