/*
 * @WSURLProvider.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import java.net.URI;
import java.net.URL;

/**
 * Interfaz que define los componentes que proveen {@link URL} para llamar a los
 * servicios
 * 
 * @author Arturo Volpe
 * @since 2.1
 * @version 1.0 Aug 5, 2013
 * @see EntityURLProvider
 * 
 */
public interface WSInformationProvider {

	/**
	 * Retorna la {@link URI} para invocar a un servicio desde la clase que
	 * retorna.
	 * 
	 * @param type
	 *            retornado por el servicio
	 * @return {@link URI} utilizada para invocar al servicio
	 */
	Info getInfoByReturnType(Class<?> type);

	/**
	 * Retorna la {@link URI} para invocar a un servicio desde el nombre de la
	 * clase que retorna.
	 * 
	 * @param key
	 *            clave de la tabla {@link WSEndpoint}
	 * @return {@link URI} utilizada para invocar al servicio
	 */
	Info getInfoByKey(String key);

	/**
	 * Retorna la {@link Info} para invocar a un servicio.
	 * 
	 * @param internalTag
	 * @return {@link Info} que contiene los detalles del servicio
	 */
	Info getInfoByTag(String internalTag);

	/**
	 * PlaceHolder utilizado para manipular datos de servicios
	 * 
	 * @author Arturo Volpe
	 * @since 2.2
	 * @version 1.0 Aug 5, 2013
	 * 
	 */
	public class Info {

		private final String url;

		private final String key;

		private final String password;

		private final String user;

		private final String internalTag;

		/**
		 * @param url
		 * @param key
		 * @param password
		 * @param user
		 */
		public Info(String url, String key, String password, String user,
				String internalTag) {

			super();
			this.url = url;
			this.key = key;
			this.password = password;
			this.user = user;
			this.internalTag = internalTag;
		}

		public String getUrl() {

			return url;
		}

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

	}

}
