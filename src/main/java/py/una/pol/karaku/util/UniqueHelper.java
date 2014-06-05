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
package py.una.pol.karaku.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.exception.UniqueConstraintException;
import py.una.pol.karaku.model.Unique;

/**
 * Componente que se utiliza para convertir Excepciones a excepciones del tipo
 * {@link UniqueConstraintException}, para poder obtener una referencia a los
 * atributos que fallaron cuando el SQL fracaso.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 19, 2013
 * 
 */
@Component
public class UniqueHelper {

	/**
	 * Si es una exception {@link ConstraintViolationException} que viene de la
	 * base de datos, entonces recupera información valiosa y genera una
	 * Excepción {@link UniqueConstraintException}.
	 * 
	 * @param exception
	 *            Excepcion genérica
	 * @param clazz
	 *            Clase de la entidad dueña de los atributos
	 * @return {@link UniqueConstraintException} si es el caso, o la misma
	 *         Excepción que se recibe
	 */
	public Exception createUniqueException(final Exception exception,
			final Class<?> clazz) {

		if (exception instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) exception;
			String constraint = cve.getConstraintName();
			Map<String, UniqueRestrintion> constrainsName = getConstrains(clazz);
			if (constrainsName.get(constraint) != null) {
				UniqueRestrintion ur = constrainsName.get(constraint);
				return new UniqueConstraintException(ur);
			}
		}
		return exception;
	}

	private Map<String, UniqueRestrintion> getConstrains(final Class<?> clazz) {

		// TODO esto se crea cada vez que surge una excepción, considerar la
		// posibilidad de guardarlo y utilizarlo mas tarde.
		HashMap<String, UniqueRestrintion> restrintions = new HashMap<String, UniqueRestrintion>();
		for (Field f : clazz.getDeclaredFields()) {
			Unique uniqueName = f.getAnnotation(Unique.class);
			if (uniqueName != null) {
				for (String fieldConstraint : uniqueName.value()) {
					UniqueRestrintion restrintion = restrintions
							.get(fieldConstraint);
					if (restrintion == null) {
						restrintion = new UniqueRestrintion(fieldConstraint);
						restrintions.put(fieldConstraint, restrintion);
					}
					restrintion.addField(f.getName());
				}
			}
		}

		return restrintions;
	}

	/**
	 * Clase auxiliar que se utiliza para agrupar logicamente un constraint de
	 * base de datos con su atributo en una entidad.
	 * 
	 * @author Arturo Volpe Torres
	 * @since 1.0
	 * @version 1.0 Feb 19, 2013
	 * 
	 */
	public static class UniqueRestrintion {

		protected UniqueRestrintion(final String uniqueConstraintName) {

			super();
			this.uniqueConstraintName = uniqueConstraintName;
		}

		private String uniqueConstraintName;
		private List<String> fields;

		/**
		 * Agrega un nuevo field a la lista de fields manejados por esta clase
		 * 
		 * @param field
		 */
		public void addField(final String field) {

			if (fields == null) {
				fields = new ArrayList<String>(2);
			}
			fields.add(field);
		}

		/**
		 * Retorna la lista de elementos manejados por esta clase
		 * 
		 * @return lista de fields
		 */
		public List<String> getFields() {

			return fields;
		}

		/**
		 * Retorna el nombre del constraint
		 * 
		 * @return nombre de constraint de base de datos
		 */
		public String getUniqueConstraintName() {

			return uniqueConstraintName;
		}
	}
}
