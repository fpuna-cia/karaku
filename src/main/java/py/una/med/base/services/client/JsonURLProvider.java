package py.una.med.base.services.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.log.Log;

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

	@Autowired
	private PropertiesUtil util;

	private Holder h;

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
				InputStream is = getResource();
				h = mapper.readValue(is, new TypeReference<Holder>() {});
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

	private InputStream getResource() throws IOException {

		String url = util.get("karaku.menu.json_urls", "urls.json");
		if (url.startsWith("/")) {
			return new FileInputStream(url);
		} else {
			return new ClassPathResource(util.get("karaku.menu.json_urls",
					"urls.json")).getInputStream();
		}
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
