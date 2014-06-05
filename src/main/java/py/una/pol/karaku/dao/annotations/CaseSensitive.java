/*
 * 
 */
package py.una.pol.karaku.dao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.pol.karaku.dao.entity.annotations.FieldAnnotation;

/**
 * Anotación que sirve para determinar si un field es case sensitive, si lo es
 * el {@link py.una.pol.karaku.repo.KarakuBaseDao} no se encargara de ponerlo en
 * mayúsculas
 *
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.0
 *
 */
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@FieldAnnotation
public @interface CaseSensitive {

}
