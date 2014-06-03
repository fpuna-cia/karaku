/*
 * @ICurrentpageHelper.java 1.0 Oct 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.jsf.utils;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.event.FacesListener;
import py.una.pol.karaku.controller.MenuBean;
import py.una.pol.karaku.menu.schemas.Menu;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 7, 2013
 *
 */
public interface ICurrentpageHelper {

	/**
	 * Método que se encarga de inicializar este componente, debe ser invocado
	 * al inicio de cada página, no es un {@link FacesListener} por qué debe ser
	 * un componente de Spring.
	 */
	String initialize();

	/**
	 * <b>WorkAround</b> para permitir que esto sea lo primero que se ejecuta a
	 * la hora de mostrar una página.
	 * <p>
	 * Gracias a este método, se ejecuta la acción {@link #initialize()} antes
	 * de que ocurra cualquier otro <code>bind</code> con algún componente, como
	 * ocurre con {@link MenuBean}.
	 * </p>
	 *
	 * @return dummy {@link HtmlOutputText}
	 */
	HtmlOutputText getBind();

	/**
	 * <b>WorkAround</b> para permitir que esto sea lo primero que se ejecuta a
	 * la hora de mostrar una página.
	 * <p>
	 *
	 * @see #getBind()
	 */
	void setBind(HtmlOutputText input);

	/**
	 * Retorna el menú que actualmente esta seleccionado, si el mismo no se
	 * puede detectar, se retorna <code>null</code>
	 *
	 * @return menu actual elemento seleccionado.
	 */
	Menu getCurrentMenu();

}
