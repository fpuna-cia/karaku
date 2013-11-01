/*
 * @WSURLProvider.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import java.util.List;

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
	List<Info> getInfoByTag(String internalTag);

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

			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Info other = (Info) obj;
			if (internalTag == null) {
				if (other.internalTag != null)
					return false;
			} else if (!internalTag.equals(other.internalTag))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;
			return true;
		}

	}

}
