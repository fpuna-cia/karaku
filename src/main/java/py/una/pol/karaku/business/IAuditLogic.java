/*
 * @IAuditLogic 1.0 18/02/2013 Sistema integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.business;

import java.util.List;
import py.una.pol.karaku.domain.AuditTrail;
import py.una.pol.karaku.domain.AuditTrailDetail;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
public interface IAuditLogic extends IKarakuBaseLogic<AuditTrail, Integer> {

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
