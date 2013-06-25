package py.una.med.base.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Componente que provee acceso a URL's a travez de la entidad
 * {@link WSEndpoint}
 * 
 * @author Arturo Volpe
 * @since 1.2 11 de Junio de 2013
 * @version 1.0
 * 
 */
@Service
@Transactional
public class EntityURLProvider implements WSURLProvider {

	@Autowired
	private WSEndpointDAO dao;

	@Override
	public String getByKey(String key) {

		WSEndpoint example = new WSEndpoint();
		example.setKey(key);
		WSEndpoint toRet = dao.getByExample(example);
		if ((toRet == null) || (toRet.getUrl() == null)) {
			throw new URLNotFoundException(key);
		}
		return toRet.getUrl();
	}

	@Override
	public String getByReturnType(Class<?> type) {

		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null");
		}
		return getByKey(type.getSimpleName());
	}

}
