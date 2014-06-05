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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import py.una.pol.karaku.dynamic.forms.Field;

/**
 * Clase que implementa funcionalidades comunes a todos los wizard, además es la
 * Raíz de la jerarquía de clases.
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0 May 29, 2013
 * 
 */
public abstract class AbstractWizard extends Field implements Wizard {

	private List<Step> steps;
	private int currentStep;
	private String title;

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#getSteps()
	 */
	@Override
	public List<Step> getSteps() {

		if (steps == null) {
			steps = new ArrayList<Step>();
		}
		return steps;
	}

	/**
	 * @param steps
	 *            steps para setear
	 */
	public void setSteps(List<Step> steps) {

		this.steps = steps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#initialize()
	 */
	@Override
	public void initialize() {

		if (steps == null) {
			return;
		}
		for (Step step : steps) {
			step.initialize();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#next()
	 */
	@Override
	public Step next() {

		if (getCurrentStepNumber() < (getSteps().size() - 1)) {
			currentStep++;
		}

		return getSteps().get(currentStep);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#previous()
	 */
	@Override
	public Step previous() {

		if (currentStep != 0) {
			currentStep--;
		}
		return getSteps().get(currentStep);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#getCurrentStepNumber()
	 */
	@Override
	public int getCurrentStepNumber() {

		return currentStep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#validateEachStep()
	 */
	@Override
	public boolean validateEachStep() {

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.Field#disable()
	 */
	@Override
	public boolean disable() {

		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.wizard.Wizard#getTitle()
	 */
	@Override
	public String getTitle() {

		return title;
	}

	/**
	 * Setea un nuevo titulo al wizard.
	 * 
	 * @param title
	 *            title para setear
	 */
	public void setTitle(String key) {

		if (key == null) {
			throw new IllegalArgumentException(
					"Titulo del wizard no puede ser nullo, utilize cadena vacia.");
		}
		String value = getMessage(key);
		if (value.length() > TITLE_MAX_LENGTH) {
			throw new IllegalArgumentException(
					"Titulo del wizard demasiado extenso, vease Wizard#TITLEMAXLENGTH");
		}
		title = value;
	}
}
