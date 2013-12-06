/*
 * @ReplicationLogic.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import static py.una.med.base.util.Checker.notNull;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.SearchParam;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.log.Log;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;
import py.una.med.base.util.DateProvider;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
@Service
public class ReplicationLogic implements IReplicationLogic {

	@Autowired
	private IReplicationInfoDao dao;

	@Autowired
	private DateProvider dateProvider;

	@Log
	private Logger log;

	@Override
	public Set<ReplicationInfo> getActiveReplications() {

		Where<ReplicationInfo> where = Where.get();
		where.addClause(Clauses.eq("active", true));

		List<ReplicationInfo> ri = dao.getAll(where, null);
		if (ri == null) {
			return Collections.emptySet();
		} else {
			return loadClass(ri);
		}
	}

	@Override
	public ReplicationInfo getByClass(Class<?> clazz) {

		String clazzName = notNull(clazz, "Cant get info of null class")
				.getName();

		Where<ReplicationInfo> where = Where.get();
		where.addClause(Clauses.eq("entityClassName", clazzName));

		List<ReplicationInfo> infos = dao.getAll(where, getSearchParam());

		switch (infos.size()) {
			case 0:
				return null;
			case 1:
				return loadClass(infos.get(0));
			default:
				log.warn("Multiple replicationInfo for the same class name");
				return loadClass(infos.get(0));
		}

	}

	/**
	 * @return
	 * 
	 */
	private SearchParam getSearchParam() {

		SearchParam sp = new SearchParam();
		sp.addOrder("number");
		return sp;
	}

	/**
	 * @return
	 */
	@Override
	@Transactional
	public Set<ReplicationInfo> getReplicationsToDo() {

		Where<ReplicationInfo> where = Where.get();
		where.addClause(Clauses.eq("active", true));

		List<ReplicationInfo> loaded = dao.getAll(where, getSearchParam());

		if (loaded == null) {
			return Collections.emptySet();
		}

		Set<ReplicationInfo> toRet = new HashSet<ReplicationInfo>(loaded.size());

		for (ReplicationInfo ri : loaded) {

			Calendar now = dateProvider.getNowCalendar();
			if (now.after(getNextSync(ri))) {
				toRet.add(loadClass(ri));
			}
		}

		return toRet;
	}

	private Calendar getNextSync(ReplicationInfo ri) {

		Calendar last = Calendar.getInstance();
		Date lastReplicated = ri.getLastSync();
		if (ri.getLastSync() == null) {
			last.clear();
			// seteamos al principio de los tiempos, para que sí o
			// sí se sincronize
		} else {
			last.setTime(lastReplicated);
			last.add(Calendar.MINUTE, ri.getInterval());
		}
		return last;
	}

	/**
	 * @param clazz
	 */
	@Override
	@Transactional(readOnly = false)
	public void notifyReplication(Class<?> clazz, String id) {

		ReplicationInfo info = getByClass(clazz);

		info.setLastSync(dateProvider.getNow());
		info.setLastId(id);

		dao.update(info);

	}

	/**
	 * @param class1
	 * @param i
	 */
	@Override
	public ReplicationInfo updateSyncTime(Class<?> class1, int i) {

		ReplicationInfo info = getByClass(class1);

		info.setInterval(i);

		return info;
	}

	@SuppressWarnings("unchecked")
	private ReplicationInfo loadClass(ReplicationInfo ri) {

		try {
			ClassLoader cl = getClass().getClassLoader();
			ri.setEntityClazz((Class<? extends Shareable>) cl.loadClass(ri
					.getEntityClassName()));
			ri.setDaoClazz((Class<? extends DTO>) cl.loadClass(ri
					.getDtoClassName()));
			ri.setRequestClazz(cl.loadClass(ri.getRequestClassName()));
			ri.setResponseClazz(cl.loadClass(ri.getResponseClassName()));
		} catch (ClassNotFoundException e) {
			log.warn("Can't load classes of Replication info with id {}", e,
					ri.getId());
		}
		return ri;

	}

	private Set<ReplicationInfo> loadClass(Collection<ReplicationInfo> loaded) {

		notNull(loaded, "Can not load a empty size set");
		Set<ReplicationInfo> toRet = new HashSet<ReplicationInfo>(loaded.size());

		for (ReplicationInfo ri : loaded) {
			toRet.add(loadClass(ri));
		}
		return toRet;
	}

	@Override
	public void configureInfo(ReplicationInfo info) {

		if (info.getEntityClazz() == null) {
			loadClass(info);
		}
	}
}
