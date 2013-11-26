/*
 * @EntityDao.java 1.0 Nov 4, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.replication.layers;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.repo.SIGHBaseDao;

/**
 * Dao para los test de replicacion
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 *
 */
@Transactional
@Repository
public class ReplicatedEntityDao extends SIGHBaseDao<ReplicatedEntity, Long> {

}
