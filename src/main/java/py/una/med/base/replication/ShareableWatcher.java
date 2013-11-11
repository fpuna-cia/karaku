/*
 * @ShareableWatcher.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication;

import org.springframework.stereotype.Component;
import py.una.med.base.dao.entity.Operation;
import py.una.med.base.dao.entity.watchers.AbstractWatcher;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 *
 */
@Component
public class ShareableWatcher extends AbstractWatcher<Shareable> {

	@Override
	public Shareable process(Operation origin, Operation redirected,
			Shareable bean) {

		if ((Operation.DELETE == origin) && (Operation.UPDATE == redirected)) {
			bean.inactivate();
		}
		return bean;
	}

	@Override
	public Operation redirect(Operation operation, Shareable bean) {

		if (Operation.DELETE == operation) {
			return Operation.UPDATE;
		}
		return operation;
	}
}
