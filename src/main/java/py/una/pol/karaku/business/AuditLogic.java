/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.domain.AuditTrail;
import py.una.pol.karaku.domain.AuditTrailDetail;
import py.una.pol.karaku.repo.IAuditTrailDao;
import py.una.pol.karaku.repo.IAuditTrailDetailDao;
import py.una.pol.karaku.repo.IKarakuBaseDao;

/**
 * Clase que implementa la interfaz {@link IAuditLogic}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
@Service
@Transactional
public class AuditLogic extends KarakuBaseLogic<AuditTrail, Integer> implements
		IAuditLogic {

	@Autowired
	private IAuditTrailDao dao;

	@Autowired
	private IAuditTrailDetailDao detailDao;

	@Override
	public IKarakuBaseDao<AuditTrail, Integer> getDao() {

		return dao;
	}

	@Override
	public void saveAudit(AuditTrail auditTrail, List<AuditTrailDetail> details) {

		dao.add(auditTrail);
		for (AuditTrailDetail detail : details) {
			detailDao.add(detail);
		}
	}

}
