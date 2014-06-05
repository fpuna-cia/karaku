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

/**
 * Wizard que se compone de un conjunto de {@link Step}, provee funciones
 * Básicas como avanzar y retroceder. <br>
 * Tener en cuenta que para este tipo de wizard, todos los pasos se ejecutan
 * aislados, y el paso al siguiente representa una peticion ajax que valida los
 * datos ya ingresados.<br>
 * El comportamiento que debe tener es como sigue:
 * <ol>
 * <li>Se limpian todos los pasos, una limpieza incluye la reincialización de
 * todos sus formularios y que cada paso se marque como no visitado.
 * <li>Se renderiza el primer {@link Step}, se lo marca como visitado, y se
 * permite al usuario cargar datos. Además se muestra solo el botón para
 * avanzar.
 * <li>Se renderiza sucesivamente los siguientes pasos (marcando cada uno como
 * visitado), mostrando los botones avanzar, y retroceder. Tener en cuenta que
 * si es el paso 2 y se presiona el botón atrás se vuelve al punto 1
 * </ol>
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public class SimpleWizard extends AbstractWizard implements Wizard {

	@Override
	public String getTogglePanelId() {

		return getId() + "_" + "dynamicToogle";
	}

	/**
	 * Retorna el paso actual
	 * 
	 * @return {@link Step} actual
	 */
	public Step getCurrentStep() {

		return getSteps().get(getCurrentStepNumber());
	}

	/**
	 * 
	 */
	public boolean getRenderNext() {

		return false;
	}

	public boolean getRenderPrevious() {

		return getCurrentStepNumber() > 0;
	}

	public boolean getRenderFinish() {

		return getCurrentStepNumber() == getSteps().size();
	}

	/**
	 * Agrega unos {@link Step} al wizard, si no existía ningún paso anterior,
	 * estos pasos se vuelven todo el wizard, o sino se anexan al final
	 * 
	 * @param steps
	 *            para el wizard, el orden se respeta
	 */
	public void addStep(Step ... steps) {

		for (Step step : steps) {
			getSteps().add(step);
		}
	}

	public String getPopupId() {

		return getId() + "_popupId";
	}

}
