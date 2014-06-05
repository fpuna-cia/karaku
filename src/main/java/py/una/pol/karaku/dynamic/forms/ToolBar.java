/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.dynamic.forms;

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
	 * @see py.una.pol.karaku.dynamic.forms.Field#getType()
	 */
	@Override
	public String getType() {

		return this.getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.Field#disable()
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
	 * @see py.una.pol.karaku.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		for (Field f : this.items) {
			f.enable();
		}
		return true;
	}
}
