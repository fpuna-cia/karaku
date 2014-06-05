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
package py.una.pol.karaku.dynamic.wizard;

import py.una.pol.karaku.dynamic.forms.Field;
import py.una.pol.karaku.dynamic.forms.ToolBar;

/**
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public abstract class AbstractStep extends Field implements Step {

	private ToolBar toolBar;
	private String description;

	@Override
	public ToolBar getToolBar() {

		if (toolBar == null) {
			toolBar = new ToolBar();
		}
		return toolBar;
	}

	/**
	 * @param toolBar
	 *            toolBar para setear
	 */
	public void setToolBar(ToolBar toolBar) {

		this.toolBar = toolBar;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public void setDescription(String description) {

		this.description = description;
	}

	@Override
	public void initialize() {

		// ninguna acción por defecto
	}
}
