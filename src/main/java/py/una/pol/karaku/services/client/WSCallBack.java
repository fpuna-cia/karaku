/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.services.client;

import py.una.pol.karaku.exception.KarakuException;

/**
 * Clase que esta pensada para ser utilizada como <b>callBack</b> para la
 * llamada a los servicios, como los mismos son asincronos, la respuesta llega
 * momentos después, y se invoca al método {@link #onSucess(Object)} pasando
 * como parámetro el resultado de la consulta.
 * <p>
 * Si existieron fallas durante el proceso, se lanza el método
 * {@link #onFailure(KarakuException)} que recibe como parámetro la excepción
 * lanzada.
 * </p>
 * <p>
 * Un callback es una clase que se utiliza para no bloquear la llamada, como un
 * receptor posterior del mensaje que se pide, es importante observar que
 * automáticamente después de invocar al servicio el método continua y se
 * realiza la llamada en el fondo.
 * </p>
 * <p>
 * Como por ejemplo, el siguiente código:
 * 
 * <pre>
 * {@code 
 * printToConsole("Iniciando");
 * call(object, new WSCallBack&lt;Object>() {
 * 	public void onSucess(Object o) {
 * 		printToConsole("Recibiendo");
 * 	}
 * 	public void onFailure(Exception e) {
 * 		printToConsole("Falllando");
 * 	}
 * 	}
 * printToConsole("Finalizando")
 * }
 * </pre>
 * 
 * Tendra la siguiente salida de consola:
 * 
 * <pre>
 * 	Iniciando
 * 	<b>Finalizando</b>
 * 	Recibiendo || Fallando
 * </pre>
 * 
 * </p>
 * <p>
 * Podemos ver entonces, que el método {@link #onSucess(Object)} es invocado
 * después de que termine el código, rompiendo así la estructura lineal del
 * mismo.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.1.3
 * @version 1.0 Jun 11, 2013
 * 
 */
public interface WSCallBack<T> {

	/**
	 * Método que será ejecutado una vez que se retorne del servicio, el
	 * parámetro recibido será la respuesta del servicio.
	 * 
	 * @param object
	 *            recibido del servicio
	 */
	void onSucess(T object);

	/**
	 * Método que será ejecutado cuando hay una excepción del lado del cliente o
	 * del servidor, para ver la cause utilizar
	 * {@link KarakuException#getCause()}.
	 * <p>
	 * Este método es invocado incluso cuando hay fallas del lado del cliente,
	 * como cuando no se encuentra la URL.
	 * </p>
	 * 
	 * @param exception
	 *            lanzada al invocar al servicio.
	 */
	void onFailure(Exception exception);
}
