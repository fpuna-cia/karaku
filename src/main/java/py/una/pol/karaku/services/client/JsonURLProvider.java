package py.una.pol.karaku.services.client;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.log.Log;

/**
 * Componente que provee acceso a URL's a través de un archivo JSON.
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 21, 2013
 * 
 */
public class JsonURLProvider implements WSInformationProvider {

	@Log
	private Logger log;

	private InputStream stream;

	private Holder h;

	/**
	 * Construye un nuevo proveedor desde el {@link InputStream} como parámetro.
	 * 
	 * <p>
	 * El inputStream debe contener un archivo JSON que sea parseable según
	 * {@link Holder}.
	 * </p>
	 * <p>
	 * Ver el archivo <code>urls.json.example</code> para ver el formato del
	 * mismo.
	 * </p>
	 */
	public JsonURLProvider(InputStream file) {

		this.stream = file;
	}

	@Override
	public Info getInfoByKey(String key) {

		for (Info info : getHolder().getServices()) {
			if (info.getKey().equals(key)) {
				return info;
			}
		}
		return null;
	}

	@Override
	public Info getInfoByReturnType(Class<?> type) {

		return getInfoByKey(type.getSimpleName());
	}

	@Override
	public List<Info> getInfoByTag(String internalTag) {

		List<Info> respuesta = new ArrayList<WSInformationProvider.Info>(
				getHolder().getServices().size());
		for (Info info : getHolder().getServices()) {
			if (info.getInternalTag().trim()
					.equalsIgnoreCase(internalTag.trim())) {
				respuesta.add(info);
			}
		}
		return respuesta;
	}

	private Holder getHolder() {

		if (h == null) {
			ObjectMapper mapper = new ObjectMapper();

			try {
				InputStream is = stream;
				h = mapper.readValue(is, new TypeReference<Holder>() {

					@Override
					public Type getType() {

						return Holder.class;
					}
				});
				is.close();
			} catch (Exception e) {
				log.warn("No se puede leer el archivo de Url's de Menu", e);

				throw new KarakuRuntimeException(
						"No se puede leer el archivo de Url's de Menu", e);
			}
		}
		if (h.getServices() == null) {
			h.setServices(new ArrayList<Info>(0));
		}
		return h;
	}

	public static class Holder {

		private List<Info> services;

		/**
		 * @return services
		 */
		public List<Info> getServices() {

			return services;
		}

		/**
		 * @param services
		 *            services para setear
		 */
		public void setServices(List<Info> services) {

			this.services = services;
		}
	}
}
