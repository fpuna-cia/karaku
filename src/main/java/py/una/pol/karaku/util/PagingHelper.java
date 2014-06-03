/**
 * @PagingHelper 1.0 03/05/13. Sistema Integral de Gestión Hospitalaria
 */

package py.una.pol.karaku.util;

import javax.annotation.Nonnull;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.SearchParam;

/**
 * Clase que se encarga de la paginación de las tablas
 * 
 * @author Arturo Volpe
 * @author Uriel Gonzalez
 * @since 1.0
 * @version 1.3 30/07/2013
 * 
 */
public class PagingHelper {

	/**
	 * Interfaz que se utiliza para capturar los eventos de cambios que sufre
	 * este paginador.
	 * 
	 * @author Arturo Volpe Torres
	 * @since 1.0
	 * @version 1.0 Jul 30, 2013
	 * 
	 */
	public interface ChangeListener {

		/**
		 * Método invocado cada vez que se cambia de página (solo cuando hay un
		 * cambio real de página, cuando se mueve a la misma página nunca es
		 * llamado {@link PagingHelper#setPage(int)} pasando
		 * {@link PagingHelper#getPage()} no invocara este evento. <br />
		 * Notar que si se lanza una excepción, no se cambiara de página.
		 * 
		 * @param thizz
		 *            paginador
		 * @param previousPage
		 *            página anterior, va de 0 a
		 *            {@link PagingHelper#getMaxPage()} - 1
		 * @param currentPage
		 *            página a la que se muda, va de 0 a
		 *            {@link PagingHelper#getMaxPage()} - 1
		 */
		void onChange(PagingHelper thizz, int previousPage, int currentPage);
	}

	private int rowsForPage;
	private int page = 0;
	private Long currentCount;

	private ChangeListener changeListener;

	/**
	 * Inicializa un nueva instancia, el punto de entrada de esta clase es
	 * {@link #calculate(py.una.pol.karaku.business.ISIGHBaseLogic, py.una.pol.karaku.dao.restrictions.Where)}
	 * , y para obtener los resultados utilizar {@link #getISearchparam()}
	 * 
	 * @param rowsForPage
	 *            cantidad de filas por página
	 */
	public PagingHelper(int rowsForPage) {

		this.rowsForPage = rowsForPage;
	}

	/**
	 * Retorna un {@link ISearchParam} configurado con la paginación actual
	 * 
	 * @return
	 */
	@Nonnull
	public ISearchParam getISearchparam() {

		SearchParam sp = new SearchParam();
		sp.setOffset(page * rowsForPage);
		sp.setLimit(rowsForPage);
		return sp;
	}

	/**
	 * Avanza una página
	 */
	public void next() {

		if (!hasNext()) {
			return;
		}

		launch(page, page + 1);
		this.page++;
	}

	/**
	 * Se mueve a la ultima página
	 */
	public void last() {

		if (page >= getMaxPage()) {
			return;
		}

		launch(page, getMaxPage());
		this.page = getMaxPage();

	}

	/**
	 * Se mueve a la primera página
	 */
	public void first() {

		if (page == 0) {
			return;
		}

		launch(page, 0);
		this.page = 0;
	}

	/**
	 * Se mueve a la página anterior
	 */
	public void previous() {

		if (!hasPrevious()) {
			return;
		}
		launch(page, page - 1);
		this.page--;

	}

	private void setPage(int page) {

		int nextPage = page;
		if (nextPage > getMaxPage()) {
			nextPage = getMaxPage();
		}
		if (nextPage < 0) {
			nextPage = 0;
		}
		if (nextPage == this.page) {
			return;
		}
		launch(this.page, nextPage);
		this.page = nextPage;

	}

	/**
	 * Retorna la máxima página permitida actualmente para ser leída por un ser
	 * humano (va de 1 a N).
	 * 
	 * @return {@link Integer} que representa la última página que puede ser
	 *         visible
	 */
	public int getMaxReadablePage() {

		return getMaxPage(currentCount) + 1;
	}

	/**
	 * Retorna la máxima página permitida actualmente.
	 * 
	 * @return {@link Integer} que representa la última página que puede ser
	 *         visible
	 */
	private int getMaxPage() {

		return getMaxPage(currentCount);
	}

	private int getMaxPage(Long count) {

		// TODO cambiar la forma de redondear hacia arriba, usar ceil

		double total = currentCount;
		double rowForPage = rowsForPage;
		int maxPage = (int) (count / rowsForPage);

		if (total / rowForPage > maxPage) {
			maxPage++;
		}

		return maxPage - 1;
	}

	/**
	 * Verifica si se puede avanzar una página
	 * 
	 * @return <code>true</code> si se puede avanzar <code>false</code> en caso
	 *         contrario
	 */
	public boolean hasNext() {

		return page != getMaxPage(currentCount);
	}

	/**
	 * Verifica si se puede volver atrás una página
	 * 
	 * @return <code>true</code> si no es la primera página, y
	 *         <code>false</code> si es la primera página
	 */
	public boolean hasPrevious() {

		return page != 0;
	}

	/**
	 * Actualiza los valores de la paginación, este método es el punto de
	 * entrada del {@link PagingHelper}, se debe llamar primero a este método
	 * para que se calculen los limites de la consulta
	 * 
	 * <br>
	 * Si la página actual es mayor a la máxima página con la configuración
	 * actual, se vuelve a 0
	 * 
	 * @param dao
	 *            dao del caso de uso
	 * @param where
	 *            where configurado con los parámetros de búsqueda
	 */
	public void udpateCount(Long count) {

		currentCount = count;

		if (page > getMaxPage(currentCount)) {
			first();
		}
	}

	/**
	 * Retorna el numero de la primera página a la que puede ir.
	 * 
	 * @return {@link Integer} que representa la primera página
	 */
	public int getMinReadablePage() {

		return 1;
	}

	/**
	 * Retorna una versión legible para un humano del número de la página, se
	 * diferencia de {@link #getPage()} en que esta va de 1 a
	 * {@link #getMaxPage()}, y {@link #getPage()} va de 0 a
	 * {@link #getMaxPage()} - 1
	 * 
	 * @return Integer que representa la página actual
	 */
	public Integer getReadablePage() {

		return page + 1;
	}

	/**
	 * Asigna un número legible para un humano (típicamente desde interfaz), al
	 * llamar a este método con el parametro '1', se mueve a la primera pagina,
	 * que para {@link #getPage()} es 0.
	 * 
	 * @see #getReadablePage()
	 * @param page
	 *            entero entre 1 y {@link #getMaxPage()}
	 */
	public void setReadablePage(Integer page) {

		setPage(page - 1);
	}

	/**
	 * @param changeListener
	 *            changeListener para setear
	 */
	public void setChangeListener(ChangeListener changeListener) {

		this.changeListener = changeListener;
	}

	private void launch(int previous, int current) {

		if (changeListener == null) {
			return;
		}
		changeListener.onChange(this, previous, current);
	}

}
