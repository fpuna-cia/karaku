/*
 * 
 */
package py.una.pol.karaku.controller;

import java.util.List;
import org.richfaces.event.ItemChangeEvent;
import py.una.pol.karaku.dao.restrictions.Where;

/**
 * 
 * @author Arturo Volpe
 * @since 2.0, 30/01/2013
 * @version 1.0
 * 
 */
public interface IKarakuMainController {

	/**
	 * metodo que se llama para configurar todos los controllers que dependen de
	 * el
	 * 
	 * @return lista de controlles embeddables
	 */
	List<IKarakuEmbeddableController> configureEmbeddableControllers();

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
	 * Persiste los cambios y se mantiene en la página para permitir la edición
	 * de los {@link IKarakuEmbeddableController}.
	 * 
	 * @return String de navegación, con "" se queda en la misma página
	 */
	String doSaveAndContinue();

	/**
	 * Persiste los cambios y vuelve a la lista
	 * 
	 * @return String de navegacion, correspondiente a la vista de lista del
	 *         caso de uso
	 */
	String doSave();

	/**
	 * Retorna la lista de controlles embedables
	 * 
	 * @return Retorna una lista de controlladores hijos
	 * 
	 */
	List<IKarakuEmbeddableController> getEmbeddableControllers();

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

	/**
	 * Verifica si se esta editando la cabecera.
	 * 
	 * @return <code>true</code> si la cabecera esta actualmente en edición,
	 *         <code>false</code> en caso contrario.
	 */
	boolean isEditingHeader();

	/**
	 * Se encarga de eliminar todas las modificaciones realizadas por el usuario
	 * al momento de editar
	 */
	void resetHeaderBean();

	/**
	 * Determina si el botón editar debe estar visible para el usuario. No debe
	 * estar visible si no tiene los permisos y si no esta en modo edición.
	 * 
	 * @return <code>true</code> si se debe visualizar el botón,
	 *         <code>false</code> en caso contrario.
	 */
	boolean embeddableListCanEdit();

	/**
	 * Determina si el botón borrar debe estar visible para el usuario en las
	 * grillas de los detalles.
	 * 
	 * @return <code>true</code> si esta en modo edición o borrado,
	 *         <code>false</code> en otro modo o si no tiene los permisos
	 */
	boolean embeddableListCanDelete();
}
