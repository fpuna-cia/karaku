package py.una.med.base.controller;

import java.util.List;
import org.richfaces.event.ItemChangeEvent;
import py.una.med.base.dao.restrictions.Where;

/**
 * 
 * @author Arturo Volpe
 * @since 2.0, 30/01/2013
 * @version 1.0
 * 
 */
public interface ISIGHMainController {

	/**
	 * metodo que se llama para configurar todos los controllers que dependen de
	 * el
	 * 
	 * @return lista de controlles embeddables
	 */
	List<ISIGHEmbeddableController> configureEmbeddableControllers();

	/**
	 * Metodo que retorna el URI completa del header
	 * 
	 * @return path de la cabecera
	 */
	String getHeaderPath();

	/**
	 * Metodo que configura una entidad hija para que pertenezca a la entidad
	 * padre <br>
	 * Por ejemplo, si este controller es PersonaFisicaController, entonces este
	 * metodo tiene que tener una funcion asi:
	 * 
	 * <pre>
	 * {@code ...
	 * if (childBean instanceof Direccion) 
	 * 	Direccion d = (Direccion) childBean;
	 * 	d.setPersona(getBean());
	 * 	return d
	 * ...
	 * }
	 * </pre>
	 * 
	 * @param childBean
	 * @return Elemento ya configurado
	 */
	Object configureChildBean(Object childBean);

	/**
	 * Retorna el where que se usara (en adicion del where base del controller)
	 * para filtrar la lista
	 * 
	 * @param base
	 * @param clazz
	 *            clase del objeto que se esta pasando
	 * @return Base Where<T> con los filtros seteados para filtrar
	 */
	Where<?> configureBaseWhere(Where<?> base, Class<?> clazz);

	/**
	 * Inicia una transaccion
	 */
	void init();

	/**
	 * Guarda la transaccion
	 */
	void save();

	/**
	 * Cancela la transaccion, rollback
	 */
	void cancel();

	/**
	 * Retorna la lista de controlles embedables
	 * 
	 * @return Retorna una lista de controlladores hijos
	 * 
	 */
	List<ISIGHEmbeddableController> getEmbeddableControllers();

	/**
	 * Retorna la url que sera mostrada cuando se acepta, edita o elimina
	 * <i>Notese que la URI tiene que ser global</i>
	 * 
	 * @return URI
	 */
	String childrenGoView();

	/**
	 * Metodo que captura el cambio de tab entre detalles
	 * 
	 * @param event
	 */
	void tabChange(ItemChangeEvent event);

	/**
	 * Retorna el Bean padre
	 * 
	 * @return retorna el objeto principal
	 */
	Object getHeaderBean();

	/**
	 * Retorna el ID del padre
	 * 
	 * @return retorna el id del objeto principal
	 */
	Object getHeaderBeanID();
}
