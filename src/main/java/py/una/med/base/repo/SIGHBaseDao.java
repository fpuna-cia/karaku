package py.una.med.base.repo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.med.base.dao.impl.BaseDAOImpl;
import py.una.med.base.dao.util.CaseSensitiveHelper;
import py.una.med.base.util.ControllerHelper;

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

	@Override
	public T update(T entity) {

		doPreUpdate(entity);
		return super.add(entity);
	}

	@Override
	public T add(T entity) {

		doPrePersist(entity);
		return super.add(entity);
	};

	@Autowired
	ControllerHelper helper;

	Method prePersist;
	Method preUpdate;
	private boolean metodoscargados;

	public Method getPrePersist() {

		if (!metodoscargados) {
			cargarMetodos();
		}
		return prePersist;
	}

	public Method getPreUpdate() {

		if (!metodoscargados) {
			cargarMetodos();
		}
		return preUpdate;
	}

	private void cargarMetodos() {

		Method[] m = getClassOfT().getMethods();

		for (Method method : m) {
			if (method.isAnnotationPresent(PrePersist.class)) {
				prePersist = method;
			}
			if (method.isAnnotationPresent(PreUpdate.class)) {
				preUpdate = method;
			}
		}

	}

	private void doPrePersist(T entity) {

		if (getPrePersist() != null) {
			try {
				getPrePersist().invoke(entity, (Object[]) null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void doPreUpdate(T entity) {

		if (getPreUpdate() != null) {
			try {
				getPreUpdate().invoke(entity, (Object[]) null);
			} catch (IllegalArgumentException e) {
				helper.showException(e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				helper.showException(e);
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				helper.showException(e);
				e.printStackTrace();
			}
		}
	}
}
