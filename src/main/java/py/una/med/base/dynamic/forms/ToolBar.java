/*
 * @ButtonBar.java 1.0 May 31, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 31, 2013
 * 
 */
public class ToolBar extends Field {

	private List<Field> items;
	private String info;

	/**
	 * Agrega un elemento (o conjunto de estos) a la barra.
	 * 
	 * @param field
	 *            , o array de fiels.
	 */
	public void addItem(Field ... fields) {

		for (Field f : fields) {
			this.getItems().add(f);
		}
	}

	/**
	 * Retorna la lista de elementos que componen esta barra.
	 * 
	 * @return items
	 */
	public List<Field> getItems() {

		if (this.items == null) {
			this.items = new ArrayList<Field>();
		}
		return this.items;
	}

	/**
	 * Información que se despliega en la barra.
	 * 
	 * @param info
	 *            Cadena que será mostrada.
	 * 
	 */
	public void setInfo(String info) {

		this.info = info;
	}

	/**
	 * Retorna la información actual de la barra.
	 * 
	 * @return info Cadena de información
	 */
	public String getInfo() {

		return this.info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#getType()
	 */
	@Override
	public String getType() {

		return this.getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#disable()
	 */
	@Override
	public boolean disable() {

		for (Field f : this.items) {
			f.disable();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		for (Field f : this.items) {
			f.enable();
		}
		return true;
	}
}
