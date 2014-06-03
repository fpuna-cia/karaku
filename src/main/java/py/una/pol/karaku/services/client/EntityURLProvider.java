package py.una.pol.karaku.services.client;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.util.EntityExample;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.dao.where.MatchMode;

/**
 * Componente que provee acceso a URL's a través de la entidad
 * {@link WSEndpoint}
 *
 * @author Arturo Volpe
 * @since 2.1.3 11 de Junio de 2013
 * @version 2.0
 *
 */
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
		return getInfoByEndPoint(toRet);
	}

	@Override
	public Info getInfoByReturnType(Class<?> type) {

		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null");
		}
		return getInfoByKey(type.getSimpleName());
	}

	@Override
	public List<Info> getInfoByTag(String internalTag) {

		Where<WSEndpoint> where = new Where<WSEndpoint>().makeDistinct();
		where.addClause(Clauses.eq("internalTag", internalTag));
		List<WSEndpoint> result = dao.getAll(where, null);
		List<Info> toRet = new ArrayList<Info>(result.size());
		for (WSEndpoint ws : result) {
			toRet.add(getInfoByEndPoint(ws));
		}
		return toRet;
	}

	private Info getInfoByEndPoint(WSEndpoint endPoint) {

		return new Info(endPoint.getUrl(), endPoint.getKey(),
				endPoint.getPassword(), endPoint.getUser(),
				endPoint.getInternalTag());
	}
}
