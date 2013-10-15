package py.una.med.base.dao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.med.base.dao.entity.annotations.FieldAnnotation;

/**
 * Anotación que sirve para determinar si un field es case sensitive, si lo es
 * el {@link py.una.med.base.repo.SIGHBaseDao} no se encargara de ponerlo en
 * mayúsculas
 *
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.0
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@FieldAnnotation
public @interface CaseSensitive {

}
