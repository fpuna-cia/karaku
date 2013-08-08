/*
 * @WSCallback.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import py.una.med.base.exception.KarakuException;

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
	void onFailure(KarakuException exception);
}
