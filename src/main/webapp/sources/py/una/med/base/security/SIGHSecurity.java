/*
 * public static final String DEFAULT = "DEFAULT";
 * 
 * @SIGHSecurity.java 1.0 Mar 14, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.med.base.security;

import org.aspectj.lang.JoinPoint;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import py.una.med.base.controller.AuthorityController;
import py.una.med.base.controller.ISIGHBaseController;
import py.una.med.base.util.StringUtils;

/**
 * 
 * @author Arturo Volpe Volpe
 * @since 1.0
 * @version 1.0 Mar 14, 2013
 * 
 */
@Service
public class SIGHSecurity {

	public static final String DEFAULT = "DEFAULT";
	public static final String DEFAULT_CREATE = "DEFAULT_CREATE";
	public static final String DEFAULT_EDIT = "DEFAULT_EDIT";
	public static final String DEFAULT_DELETE = "DEFAULT_DELETE";

	@SuppressWarnings("rawtypes")
	public void doIt(final JoinPoint joinPoint, final HasRole annotation) {

		String permission = annotation.value();
		if (StringUtils.isInvalid(permission)) {
			// LANZAR UNA EXCEPCION O ALGO
		}

		Object target = joinPoint.getTarget();

		if (target instanceof ISIGHBaseController) {
			// LANZAR OTRA VEZ
		}

		ISIGHBaseController controller = (ISIGHBaseController) target;

		if (DEFAULT.equals(permission)) {
			permission = controller.getDefaultPermission();
		} else if (DEFAULT_CREATE.equals(permission)) {
			permission = controller.getCreatePermission();
		} else if (DEFAULT_EDIT.equals(permission)) {
			permission = controller.getEditPermission();
		} else if (DEFAULT_DELETE.equals(permission)) {
			permission = controller.getDeletePermission();
		}
		if (!AuthorityController.hasRoleStatic(permission))
			throw new AccessDeniedException(String.format(
					"User doesn't have permission %s", permission));
	}
}
