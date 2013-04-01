package py.una.med.base.repo;

import java.io.Serializable;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.med.base.dao.impl.BaseDAOImpl;
import py.una.med.base.dao.util.CaseSensitiveHelper;

@Repository
public class SIGHBaseDao<T, ID extends Serializable> extends BaseDAOImpl<T, ID>
		implements ISIGHBaseDao<T, ID> {

	@Override
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	@Override
	@Autowired
	public void setSensitiveHelper(final CaseSensitiveHelper sensitiveHelper) {

		super.setSensitiveHelper(sensitiveHelper);
	}

}
