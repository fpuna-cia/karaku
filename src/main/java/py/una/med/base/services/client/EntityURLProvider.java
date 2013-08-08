package py.una.med.base.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.where.MatchMode;

/**
 * Componente que provee acceso a URL's a través de la entidad
 * {@link WSEndpoint}
 * 
 * @author Arturo Volpe
 * @since 2.1.3 11 de Junio de 2013
 * @version 2.0
 * 
 */
@Service
@Transactional
public class EntityURLProvider implements WSInformationProvider {

	@Autowired
	private WSEndpointDAO dao;

	@Override
	public Info getInfoByKey(String key) {

		WSEndpoint example = new WSEndpoint();
		example.setKey(key);
		EntityExample<WSEndpoint> example2 = new EntityExample<WSEndpoint>(
				example);
		example2.setMatchMode(MatchMode.EQUAL);
		WSEndpoint toRet = dao.getByExample(example2);
		if ((toRet == null) || (toRet.getUrl() == null)) {
			throw new URLNotFoundException(key);
		}
		return new Info(toRet.getUrl(), toRet.getKey(), toRet.getPassword(),
				toRet.getUser(), toRet.getInternalTag());
	}

	@Override
	public Info getInfoByReturnType(Class<?> type) {

		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null");
		}
		return getInfoByKey(type.getSimpleName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * py.una.med.base.services.client.WSInformationProvider#getInfoByTag(java
	 * .lang.String)
	 */
	@Override
	public Info getInfoByTag(String internalTag) {

		WSEndpoint example = new WSEndpoint();
		example.setInternalTag(internalTag);
		EntityExample<WSEndpoint> example2 = new EntityExample<WSEndpoint>(
				example);
		WSEndpoint toRet = dao.getByExample(example2);
		return new Info(toRet.getUrl(), toRet.getKey(), toRet.getPassword(),
				toRet.getUser(), toRet.getInternalTag());
	}
}
