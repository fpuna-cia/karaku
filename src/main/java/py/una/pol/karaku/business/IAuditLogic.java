/*
 * @IAuditLogic 1.0 18/02/2013 Sistema integral de Gestion Hospitalaria
 */
package py.una.med.base.business;

import java.util.List;
import py.una.med.base.domain.AuditTrail;
import py.una.med.base.domain.AuditTrailDetail;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
public interface IAuditLogic extends ISIGHBaseLogic<AuditTrail, Integer> {

	/**
	 * Agrega un registro de auditoria y todos sus detalles
	 * 
	 * @param auditTrail
	 *            Cabecera del registro
	 * @param details
	 *            Lista de detalles
	 */
	void saveAudit(AuditTrail auditTrail, List<AuditTrailDetail> details);

}
