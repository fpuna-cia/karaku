/*
 * @AbstractPagingDataSource.java 1.0 Jun 24, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dynamic.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase base para los {@link PagingDataSource}, provee funcionalidades básicas
 * de paginación.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 24, 2013
 * 
 */
public abstract class AbstractPagingDataSource implements PagingDataSource {

	public static final Logger LOG = LoggerFactory
			.getLogger(AbstractPagingDataSource.class);

	private Long currentPage;

	/**
	 * Define la cantidad de registros que serán mostrados por página.
	 */
	public static final Long REGISTER_PER_PAGE = 10L;

	/**
	 * 
	 */
	public AbstractPagingDataSource() {

		this(1L);
	}

	public AbstractPagingDataSource(Long page) {

		currentPage = page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.util.PagingDataSource#getCurrentPage()
	 */
	@Override
	public Long getCurrentPage() {

		return currentPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.util.PagingDataSource#getRegistersPerPage()
	 */
	@Override
	public Long getRegistersPerPage() {

		return REGISTER_PER_PAGE;
	}

	/**
	 * Avanza una página.
	 */
	public void next() {

		if (currentPage < getMaxPage()) {
			currentPage++;
			refresh();
		} else {
			LOG.warn("Next() llamado estando en la última página");
		}
	}

	/**
	 * Va a la última página.
	 */
	public void last() {

		if (currentPage < getMaxPage()) {
			currentPage = getMaxPage();
			refresh();
		} else {
			LOG.warn("Last() llamado estando en la última página");
		}
	}

	/**
	 * Va atrás una página.
	 */
	public void previous() {

		if (currentPage > 0) {
			currentPage--;
			refresh();
		} else {
			LOG.warn("Previous() llamado estando en la primera página");
		}
	}

	/**
	 * Vuelve a la primera página, nótese, que siempre vuelve a la primera, no a
	 * la página por defecto.
	 */
	public void first() {

		if (currentPage > 0) {
			currentPage = 0L;
			refresh();
		} else {
			LOG.warn("First() llamado estando en la primera página");
		}
	}

	private Long getMaxPage() {

		double totalRecords = getTotalCount();
		double recordsPerPage = getRegistersPerPage();
		double maxPage = Math.ceil(totalRecords / recordsPerPage);

		return (long) maxPage;

	}
}
