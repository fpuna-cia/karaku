/*
 * ISIGHADvancedController.java 1.0
 */
package py.una.med.base.controller;

import java.io.Serializable;
import py.una.med.base.exception.UniqueConstraintException;
import py.una.med.base.model.Unique;

/**
 * Interfaz que define las funcionalidades de un controllador avanzado
 *
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 18, 2013
 * @param <T>
 *            entidad
 * @param <ID>
 *            clase del id de la entidad
 *
 */
public interface ISIGHAdvancedController<T, ID extends Serializable> extends
		ISIGHBaseController<T, ID> {

	/**
	 * Método que se utiliza para capturar excepciones, manejarlas y mostrar
	 * mensajes de error personalizados
	 *
	 * <p>
	 * <b>Excepciones que puede recibir</b>
	 * </p>
	 * Puede recibir cualquier excepción, pero las reconocidas son:
	 * <ol>
	 * <li> {@link UniqueConstraintException}: excepciones que se lanzan cuando
	 * se un atributo con la anotación {@link Unique} duplciado.</li>
	 * </ol>
	 * <p>
	 * </p>
	 *
	 * @param exception
	 *            es la excepción capturada
	 * @return <code>true</code> si se maneja la excepción y <code>false</code>
	 *         si no se maneja, se retorna <code>false</code> cuando se desea
	 *         que sea capturada y manejada mas arriba en la jerarquía
	 */
	public boolean handleException(Exception exception);
}
