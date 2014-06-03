/*
 * @IMenuProvider.java 1.0 Oct 22, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.menu.client;

import java.util.List;
import py.una.pol.karaku.menu.schemas.Menu;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 22, 2013
 *
 */
public interface IMenuProvider {

	/**
	 * Retorna los menús actuales, en orden.
	 *
	 *
	 * @return Menú actual del sistema
	 */
	List<Menu> getMenu();

	/**
	 * Agrega un notificador de eventso.
	 */
	void addMenuChangeListener(MenuChangeListener mcl);

	/**
	 * Evento que se lanza cuando se modifica el menú.
	 *
	 * <p>
	 * Es útil para componentes que cachean ciertos elementos o realizan un
	 * trabajo adicional sobre el mismo.
	 * </p>
	 *
	 * TODO ver factibilidad de crear métodos como #onAdd o #onRemove para que
	 * no se reconstruya todo siempre.
	 *
	 * @author Arturo Volpe
	 * @since 2.2.8
	 * @version 1.0 Oct 22, 2013
	 *
	 */
	interface MenuChangeListener {

		/**
		 * Notifica que <code>newMenu</code> es el nuevo menú del sistema.
		 *
		 * @param newMenu
		 */
		void onChange(List<Menu> newMenu);
	}

	/**
	 * Retorna el menú del sistema actual.
	 *
	 * @return Menú del sistema actual.
	 */
	List<Menu> getLocalMenu();
}
