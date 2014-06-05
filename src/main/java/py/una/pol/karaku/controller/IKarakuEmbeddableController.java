/*
 * 
 */
package py.una.pol.karaku.controller;

/**
 *
 * @author Arturo Volpe
 * @since 2.0, 30/01/2013
 * @version 1.0
 *
 */
public interface IKarakuEmbeddableController {

	/**
	 * Setea el controller al que estara atado, si este controller es nulo, el
	 * controller no deberia actual como un controller anidado
	 *
	 * @param mainController
	 */
	void setMainController(IKarakuMainController mainController);

	/**
	 * Retorna el controlador principal asociado a este controller anidado.
	 *
	 * @return Controlador principal
	 **/
	IKarakuMainController getMainController();

	/**
	 * Retorna la URI donde se encuentra el header a ser expuesto
	 *
	 * @return URI de la cabecera
	 */
	String getHeaderPath();

	/**
	 * Delega la entidad tratada al main controller para que este lo ajuste
	 *
	 * @param child
	 */
	void delegateChild(Object child);

	/**
	 * Setea este controller que apartir de ahora actuara como un controller
	 * hijo
	 */
	void init();

	/**
	 * Cancela todas las actividades y le dice al controller que deje de ser
	 * embebido
	 */
	void cancel();

	/**
	 * Guarda todas las actividades y le dice al controller que deje de ser
	 * embebido
	 */
	void save();

	/**
	 * Metodo que retorna la URI donde se encuentra el formulario de edicion,
	 * esto deberia hacerse con el archivo faces-config.xml pero como no esta en
	 * uso, se hace aca.
	 *
	 * @return URI de la pagina de edicion
	 */
	String goEdit();

	/**
	 * Metodo que retorna la URI donde se encuentra el formulario de creacion,
	 * esto deberia hacerse con el archivo faces-config.xml pero como no esta en
	 * uso, se hace aca.
	 *
	 * @return URI de la pagina de creacion
	 */
	String goNew();

	/**
	 * Metodo que retorna la URI donde se encuentra el formulario de delete,
	 * esto deberia hacerse con el archivo faces-config.xml pero como no esta en
	 * uso, se hace aca.
	 *
	 * @return URI de la pagina de eliminacion
	 */
	String goDelete();

}
