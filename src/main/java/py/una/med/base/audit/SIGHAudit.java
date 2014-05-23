/*
 * @SIGHAudit.java 1.0 Feb 15, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.audit;

import static py.una.med.base.util.ELParser.getParamNumber;
import static py.una.med.base.util.ELParser.removeParamNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import py.una.med.base.business.IAuditLogic;
import py.una.med.base.domain.AuditTrail;
import py.una.med.base.domain.AuditTrailDetail;
import py.una.med.base.log.Log;
import py.una.med.base.security.AuthorityController;
import py.una.med.base.util.Util;

/**
 * Servicio que se encarga de capturar el {@link JoinPoint} de auditoría y
 * mediante la función {@link SIGHAudit#doAudit(JoinPoint, Audit)} lo audita.
 * 
 * @author Arturo Volpe
 * @author Romina Fernandez
 * @version 1.0
 * @since 1.0
 * 
 */
@Service
public class SIGHAudit {

	@Autowired
	private AuthorityController authorityController;

	@Autowired
	private Util util;

	@Log
	private Logger log;

	@Autowired
	private IAuditLogic logic;

	/**
	 * Método que se encarga de extraer los parámetros del método llamado, como
	 * la signatura, el IP del usuario, el usuario, y con los parámetros de
	 * {@link Audit} construye expresiones EL para obtener los valores del
	 * registro de auditoría. <br>
	 * Cada vez que genera un nuevo registro {@link AuditTrailDetail} (uno por
	 * cada detalle de la anotación) lo persiste como detalle del registro
	 * {@link AuditTrail} persistido al inicio. <br>
	 * Captura todas las excepciones y las vuelve a lanzar, pero como
	 * {@link RuntimeException}, con los mensajes "Imposible Parsear", y
	 * "Imposible serializar"
	 * 
	 * 
	 * @param joinPoint
	 *            método interceptado
	 * @param annotation
	 *            anotación del método
	 * @throws RuntimeException
	 *             ("Imposible Parsear", y "Imposible serializar")
	 */
	public void doAudit(JoinPoint joinPoint, Audit annotation) {

		doAudit(annotation, joinPoint.getSignature().toShortString(),
				joinPoint.getTarget(), joinPoint.getArgs());
	}

	/**
	 * @param annotation
	 * @param methodSignature
	 * @param target
	 * @param gS
	 */
	public void doAudit(Audit annotation, String methodSignature,
			Object target, Object[] params) {

		AuditTrail auditTrail = new AuditTrail();

		List<AuditTrailDetail> details = new ArrayList<AuditTrailDetail>();
		auditTrail.setMethodSignature(methodSignature);

		auditTrail.setIp(util.getIpAdress());

		auditTrail.setUsername(authorityController.getUsername());

		String[] toAudit = annotation.toAudit();
		String[] paramsToAudit = annotation.paramsToAudit();
		ExpressionParser parser = new SpelExpressionParser();
		if (toAudit != null) {
			for (String string : toAudit) {
				Expression exp = parser.parseExpression(string);

				Object value = exp.getValue(target);
				AuditTrailDetail detail = new AuditTrailDetail();
				detail.setHeader(auditTrail);
				detail.setValue((Serializable) value);
				detail.setExpression(string);
				details.add(detail);
				log.info("Audit {}:{}", string, value);
			}
		}

		if (paramsToAudit != null) {
			for (String string : paramsToAudit) {

				Integer nroParm = getParamNumber(string);
				String expression = removeParamNumber(string);
				Object value;
				if (expression == null) {
					value = params[nroParm];
				} else {
					Expression exp = parser.parseExpression(expression);
					value = exp.getValue(params[nroParm]);
				}
				AuditTrailDetail detail = new AuditTrailDetail();
				detail.setHeader(auditTrail);
				detail.setValue((Serializable) value);
				detail.setExpression(string);
				details.add(detail);
			}
		}
		logic.saveAudit(auditTrail, details);

	}

}
