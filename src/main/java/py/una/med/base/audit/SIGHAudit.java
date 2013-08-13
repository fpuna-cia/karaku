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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import py.una.med.base.business.IAuditLogic;
import py.una.med.base.domain.AuditTrail;
import py.una.med.base.domain.AuditTrailDetail;

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

	private Logger log = LoggerFactory.getLogger(SIGHAudit.class);

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

		AuditTrail auditTrail = new AuditTrail();

		List<AuditTrailDetail> details = new ArrayList<AuditTrailDetail>();
		auditTrail.setMethodSignature(joinPoint.getSignature().toShortString());

		String ip = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest().getRemoteAddr();
		auditTrail.setIp(ip);

		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		auditTrail.setUsername(userName);

		// String ip = ((ServletRequestAttributes) RequestContextHolder
		// .currentRequestAttributes()).getRequest().getRemoteAddr();
		// auditTrail.setIp(ip);

		String[] toAudit = annotation.toAudit();
		String[] paramsToAudit = annotation.paramsToAudit();
		Object object = joinPoint.getTarget();
		Object[] params = joinPoint.getArgs();
		ExpressionParser parser = new SpelExpressionParser();
		if (toAudit != null) {
			for (String string : toAudit) {
				Expression exp = parser.parseExpression(string);
				Object value = exp.getValue(object);
				AuditTrailDetail detail = new AuditTrailDetail();
				detail.setHeader(auditTrail);
				detail.setValue((Serializable) value);
				detail.setExpression(string);
				details.add(detail);
				log.info("Audit", string + ":" + value);
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
