/*
 * public static final String DEFAULT = "DEFAULT";
 * 
 * @SIGHSecurity.java 1.0 Mar 14, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.med.base.security;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import py.una.med.base.controller.ISIGHBaseController;
import py.una.med.base.log.Log;
import py.una.med.base.util.StringUtils;

/**
 * <p>
 * Componente encargado de la seguridad en el sistema, se encarga de interceptar
 * todos aquellos método que tengan la anotación {@link HasRole} y verifica si
 * tiene los permisos necesarios.
 * </p>
 * <p>
 * Aquellos componentes que utilicen esta anotación y hereden de
 * {@link ISIGHBaseController}, tienen la facilidad de poder utilizar las
 * palabras claves:
 * <ul>
 * <li>{@link #DEFAULT}: que define el permiso por defecto (en ausencia de los
 * siguientes), cuando se encuentre esta clave se llamara la método
 * {@link ISIGHBaseController#getDefaultPermission()}</li>
 * <li>{@link #DEFAULT_CREATE}: que define el permiso por defecto para crear, si
 * el programador no sobreescribe el método y le pasa otro permiso. Cuando se encuentre esta clave se llamara la método
 * {@link ISIGHBaseController#getCreatePermission()</li>
 * <li>{@link #DEFAULT_EDIT}: que define el permiso por defecto para editar, si
 * el programador no sobreescribe el método y le pasa otro permiso. Cuando se encuentre esta clave se llamara la método
 * {@link ISIGHBaseController#getEditPermission()</li>
 * <li>{@link #DEFAULT_DELETE}: que define el permiso por defecto para borrar,
 * si el programador no sobreescribe el método y le pasa otro permiso.Cuando se encuentre esta clave se llamara la método
 * {@link ISIGHBaseController#getDeletePermission()</li>
 * </ul>
 * </p>
 * 
 * @author Arturo Volpe
 * @see AuthorityController
 * @see HasRole
 * @since 1.0
 * @version 1.0 Mar 14, 2013
 * 
 */
@Component
public class SIGHSecurity {

	@Log
	private Logger log;

	/**
	 * Método central de la seguridad de Karaku, utiliza la anotación
	 * {@link HasRole} para auditar todos los métodos que requieran seguridad.
	 * 
	 * @param joinPoint
	 *            {@link JoinPoint} interceptado
	 * @param hasRole
	 *            {@link HasRole} utilizado para definir el punto de control
	 */
	public void doIt(final JoinPoint joinPoint, final HasRole hasRole) {

		String permission = hasRole.value();
		if (StringUtils.isInvalid(permission)) {
			throw new AccessDeniedException("Permission invalid");
		}

		Object target = joinPoint.getTarget();

		if (target instanceof HasDefaultPermissions) {

			HasDefaultPermissions controller = (HasDefaultPermissions) target;

			if (HasDefaultPermissions.DEFAULT.equals(permission)) {
				permission = controller.getDefaultPermission();
			} else if (HasDefaultPermissions.DEFAULT_CREATE.equals(permission)) {
				permission = controller.getCreatePermission();
			} else if (HasDefaultPermissions.DEFAULT_EDIT.equals(permission)) {
				permission = controller.getEditPermission();
			} else if (HasDefaultPermissions.DEFAULT_DELETE.equals(permission)) {
				permission = controller.getDeletePermission();
			}
		}
		if (!AuthorityController.hasRoleStatic(permission)) {
			log.warn("User {} don't have the perimssion: {}",
					AuthorityController.getUsernameStatic(), permission);
			throw new AccessDeniedException("Access denied");
		}
	}

	/**
	 * Cuando sea un {@link HasDefaultPermissions} el componente que se este
	 * auditando, y se encuentre esta clave, no se la utilizara como permiso,
	 * sino se invocara al método
	 * {@link HasDefaultPermissions#getDefaultPermission()}
	 */
	public static final String DEFAULT = HasDefaultPermissions.DEFAULT;

	/**
	 * Cuando sea un {@link HasDefaultPermissions} el componente que se este
	 * auditando, y se encuentre esta clave, no se la utilizara como permiso,
	 * sino se invocara al método
	 * {@link HasDefaultPermissions#getCreatePermission()}
	 */
	public static final String DEFAULT_CREATE = HasDefaultPermissions.DEFAULT_CREATE;

	/**
	 * Cuando sea un {@link HasDefaultPermissions} el componente que se este
	 * auditando, y se encuentre esta clave, no se la utilizara como permiso,
	 * sino se invocara al método
	 * {@link HasDefaultPermissions#getEditPermission()}
	 */
	public static final String DEFAULT_EDIT = HasDefaultPermissions.DEFAULT_EDIT;

	/**
	 * Cuando sea un {@link HasDefaultPermissions} el componente que se este
	 * auditando, y se encuentre esta clave, no se la utilizara como permiso,
	 * sino se invocara al método
	 * {@link HasDefaultPermissions#getDeletePermission()}
	 */
	public static final String DEFAULT_DELETE = HasDefaultPermissions.DEFAULT_DELETE;
}
