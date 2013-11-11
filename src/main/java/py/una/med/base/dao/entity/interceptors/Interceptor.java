/*
 * @Interceptor.java 1.0 Oct 7, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.reflect.Field;
import py.una.med.base.dao.entity.Operation;

/**
 * Interfaz que define un EntityInterceptor
 *
 * <p>
 * Las implementaciones de esta interfaz realizan acciones sobre la entidad
 * antes de ser persistida.
 * </p>
 * <p>
 * Las clases que la implementan deben tener la anotación {@literal @}
 * {@link org.springframework.stereotype.Component}
 * </p>
 *
 * @see AbstractInterceptor
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 7, 2013
 *
 */
public interface Interceptor {

	/**
	 * Lista de tipos observados.
	 *
	 * <p>
	 * Si el vector que retorna, retorna {@link void}.class, entonces escuchara
	 * a cualquier tipo.
	 *
	 * </p>
	 *
	 * @return vector de clases que definen los tipos observados.
	 */
	Class<?>[] getObservedTypes();

	/**
	 * Lista de anotaciones escuchadas
	 *
	 * <p>
	 * Si el vector que retorna, retorna {@link void}.class, entonces escuchara
	 * a cualquier anotación.
	 * </p>
	 *
	 * @return vector de clases que definen las anotaciones observados.
	 */
	Class<?>[] getObservedAnnotations();

	/**
	 * Intercepta un atributo para aplicarle la lógica deseada.
	 *
	 * <p>
	 * En este punto, el {@link Field} es accesible y se pueden realizar todas
	 * las tareas necesarias, ademas se asegura que:
	 * <ol>
	 * <li>El Field es accesible</li>
	 * <li>El Field es del tipo de alguna de las clases retornadas por
	 * {@link #getObservedTypes()}</li>
	 * <li>El Field tiene alguna de las anotaciones retornadas en
	 * {@link #getObservedAnnotations()}</li>
	 * <li>Cualquier cambio que se realize será validado para asegurar su
	 * cumplimiento con las anotaciones de la entidad</li>
	 * <li>Una excepción no controlada lanzada en este punto interrumpirá la
	 * creación/actualización del bean</li>
	 * <li>El Field no es estático, final, transient (es decir es accesible)</li>
	 * <li>El Field no tiene la anotación {@link javax.persistence.Transient},
	 * final, transient (es decir es accesible)</li>
	 * </ol>
	 * </p>
	 *
	 * @param field
	 *            campo actualmente procesado
	 * @param bean
	 *            bean a guardar
	 * @param operation
	 *            tipo de operación.
	 * @see Operation
	 */
	void intercept(Operation operation, Field field, Object bean);

	/**
	 * Define si un atributo debe ser interceptado.
	 *
	 * <p>
	 * Sirve como un paso previo a {@link #intercept(Field, Object)}, y define
	 * sin un atributo debe ser o no interceptado, por ejemplo se puede
	 * controlar que tenga otra anotación, o el nombre del mismo.
	 * </p>
	 * <p>
	 * No se debe verificar que:
	 * <ol>
	 * <li>Sea final</li>
	 * <li>Sea transient</li>
	 * <li>Sea estático</li>
	 * <li>Tenga la anotación Transient</li>
	 * <li>Que sea <code>null</code></li>
	 * </ol>
	 * Esto es por que estas características son necesarias para que un
	 * {@link Field} llege a este punto.
	 * </p>
	 *
	 * @param field
	 *            atributo a interceptar
	 * @param bean
	 *            bean actual del objeto
	 * @param operation
	 *            tipo de operación.
	 * @return <code>true</code> si se desea interceptar, <code>false</code> si
	 *         no cumple las precondiciones de la lógica.
	 * @see Operation
	 */
	boolean interceptable(Operation op, Field field, Object bean);

}
