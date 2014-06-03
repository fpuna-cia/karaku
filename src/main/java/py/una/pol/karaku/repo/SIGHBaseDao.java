package py.una.pol.karaku.repo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import py.una.pol.karaku.dao.impl.BaseDAOImpl;
import py.una.pol.karaku.log.Log;

@Repository
public class SIGHBaseDao<T, K extends Serializable> extends BaseDAOImpl<T, K>
		implements ISIGHBaseDao<T, K> {

	@Log
	private Logger log;

	private Method prePersist;
	private Method preUpdate;
	private boolean metodoscargados;

	@Override
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	@Override
	@Nonnull
	public T update(@Nonnull T entity) {

		this.doPreUpdate(entity);
		return super.update(entity);
	}

	@Override
	@Nonnull
	public T add(@Nonnull T entity) {

		this.doPrePersist(entity);
		return super.add(entity);
	}

	public Method getPrePersist() {

		if (!this.metodoscargados) {
			this.cargarMetodos();
		}
		return this.prePersist;
	}

	public Method getPreUpdate() {

		if (!this.metodoscargados) {
			this.cargarMetodos();
		}
		return this.preUpdate;
	}

	private void cargarMetodos() {

		Method[] m = this.getClassOfT().getMethods();

		for (Method method : m) {
			if (method.isAnnotationPresent(PrePersist.class)) {
				this.prePersist = method;
			}
			if (method.isAnnotationPresent(PreUpdate.class)) {
				this.preUpdate = method;
			}
		}

	}

	private void doPrePersist(T entity) {

		if (this.getPrePersist() != null) {
			try {
				this.getPrePersist().invoke(entity, (Object[]) null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				this.log.error("Error al intentar persistir", e);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				this.log.error("Error al intentar persistir", e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				this.log.error("Error al intentar persistir", e);
			}
		}
	}

	private void doPreUpdate(T entity) {

		if (this.getPreUpdate() != null) {
			try {
				this.getPreUpdate().invoke(entity, (Object[]) null);
			} catch (IllegalArgumentException e) {
				this.log.error("Error al intentar actualizar", e);
			} catch (IllegalAccessException e) {
				this.log.error("Error al intentar actualizar", e);
			} catch (InvocationTargetException e) {
				this.log.error("Error al intentar actualizar", e);
			}
		}
	}
}
