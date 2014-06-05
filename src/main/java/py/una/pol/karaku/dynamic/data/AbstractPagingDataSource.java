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
