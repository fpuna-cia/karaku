/*
 * @DynamicHelper.java 1.0 Mar 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import py.una.med.base.dynamic.tables.ButtonColumn;
import py.una.med.base.dynamic.tables.Column;
import py.una.med.base.dynamic.tables.DataTable;
import py.una.med.base.dynamic.tables.SimpleColumn;
import py.una.med.base.model.DisplayName;

/**
 * Clase que sirve de ayuda para la creación de componentes dinámicos.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
@Component
public class DynamicHelper {

	/**
	 * Construye las columnas de acuerdo a la clase dada, si se encuentra la
	 * Anotación {@link DisplayName} se la utiliza para mostrar los vales
	 * 
	 * @param clazz
	 *            Clase de la cual se extraerán los atributos
	 */
	public <T, ID extends Serializable> void buildColumnsFromClass(
			final Class<T> clazz) {

		DataTable<T, ID> dt = new DataTable<T, ID>();
		List<Column> columns = new ArrayList<Column>();
		for (Field f : clazz.getDeclaredFields()) {
			DisplayName dn = f.getAnnotation(DisplayName.class);
			String value = (dn == null) ? f.getName() : dn.path();
			String header = (dn == null) ? f.getName() : dn.key();
			SimpleColumn nueva = new SimpleColumn();
			nueva.setHeaderText(header);
			nueva.bindAttribute(value, Object.class);
		}
		dt.setColumns(columns);
	}

	public Column getActions() {

		ButtonColumn c = new ButtonColumn();
		c.setHeaderText("BASE_LIST_ACTION_TITLE");

		return null;
	}

}
