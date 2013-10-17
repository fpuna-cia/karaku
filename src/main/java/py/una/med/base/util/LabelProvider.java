package py.una.med.base.util;

/**
 * Define un formato para un objeto dado.
 *
 * <p>
 * Sirve para poder mostrar información al usuario, de tal forma a no reescribir
 * el método.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 *
 */
public interface LabelProvider<T> {

	/**
	 * Define una cadena legible para un objeto dado.
	 *
	 * <p>
	 * Este método debe ser <code>null</code> safe, es decir no debería fallar
	 * cuando se le pase <code>null</code>.
	 * </p>
	 *
	 * <h3>Ejemplo:</h3>
	 *
	 * <pre>
	 * public class PaisLabelProvider implements LabelProvider{@literal <}Pais> {
	 * 	public String getAsString(Pais p) {
	 * 		if (p == null) return "";
	 * 		return p.getDescripcion();
	 * 	}
	 * }
	 * </pre>
	 *
	 * @param object
	 *            objeto a formatear
	 * @return cadena formateada.
	 */
	String getAsString(T object);

	/**
	 * Label provider que retorna el {@link #toString()} de un objeto.
	 *
	 * @author Arturo Volpe
	 * @since 2.2.8
	 * @version 1.0 Oct 17, 2013
	 *
	 */
	class StringLabelProvider<T> implements LabelProvider<T> {

		@Override
		public String getAsString(T object) {

			return object == null ? "" : object.toString();
		}
	}

}
