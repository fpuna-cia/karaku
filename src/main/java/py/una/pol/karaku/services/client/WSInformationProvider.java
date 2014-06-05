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

import java.util.List;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Interfaz que define los componentes que proveen {@link Info} para llamar a
 * los servicios.
 * 
 * @author Arturo Volpe
 * @since 2.1
 * @version 1.0 Aug 5, 2013
 * @see EntityURLProvider
 * 
 */
public interface WSInformationProvider {

	/**
	 * Retorna la {@link Info} para invocar a un servicio desde la clase que
	 * retorna.
	 * 
	 * @param type
	 *            retornado por el servicio
	 * @return {@link Info} utilizada para invocar al servicio
	 */
	Info getInfoByReturnType(Class<?> type);

	/**
	 * Retorna la {@link Info} para invocar a un servicio desde el nombre de la
	 * clase que retorna.
	 * 
	 * @param key
	 *            clave de la tabla {@link WSEndpoint}
	 * @return {@link Info} utilizada para invocar al servicio
	 */
	Info getInfoByKey(String key);

	/**
	 * Retorna la {@link Info} para invocar a un servicio.
	 * 
	 * @param internalTag
	 * @return {@link Info} que contiene los detalles del servicio
	 */
	List<Info> getInfoByTag(@Nonnull String internalTag);

	/**
	 * PlaceHolder utilizado para manipular datos de servicios
	 * 
	 * @author Arturo Volpe
	 * @since 2.2
	 * @version 1.0 Aug 5, 2013
	 * 
	 */
	class Info {

		private String url;

		private String key;

		private String password;

		private String user;

		private String internalTag;

		/**
		 * Para respetar la convención de Java Beans
		 */
		protected Info() {

		}

		/**
		 * @param url
		 * @param key
		 * @param password
		 * @param user
		 */
		protected Info(String url, String key, String password, String user,
				String internalTag) {

			super();
			this.url = url;
			this.key = key;
			this.password = password;
			this.user = user;
			this.internalTag = internalTag;
		}

		/**
		 * Retorna la URL del servicio.
		 * 
		 * @return {@link String} con el formato de la URL.
		 */
		public String getUrl() {

			return url;
		}

		/**
		 * Retorna la clave del servicio.
		 * 
		 * @return {@link String} que reprenta de manera única al servicio.
		 */
		public String getKey() {

			return key;
		}

		public String getPassword() {

			return password;
		}

		public String getUser() {

			return user;
		}

		/**
		 * @return internalTag
		 */
		public String getInternalTag() {

			return internalTag;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = (prime * result)
					+ ((internalTag == null) ? 0 : internalTag.hashCode());
			result = (prime * result) + ((key == null) ? 0 : key.hashCode());
			result = (prime * result) + ((url == null) ? 0 : url.hashCode());
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {

			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			Info rhs = (Info) obj;
			return new EqualsBuilder().append(internalTag, rhs.internalTag)
					.append(key, rhs.key).append(url, rhs.url).isEquals();

		}

	}

}
