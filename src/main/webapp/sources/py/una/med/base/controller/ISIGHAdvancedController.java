/*
 * ISIGHADvancedController.java 1.0
 */
package py.una.med.base.controller;

import java.io.Serializable;

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
	 * Metodo que se utiliza para capturar excepciones, manejarlas y mostrar
	 * mensajes de error personalizados
	 * 
	 * @param exception
	 *            es la excepcion capturada
	 * @return true si se maneja la excepcion y false si no se maneja, se
	 *         retorna false cuando se desea que sea capturada y manejada mas
	 *         arriba en la jerarquia
	 */
	public boolean handleException(Exception exception);
}
