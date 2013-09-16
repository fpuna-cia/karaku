/*
 * @Util.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util;

/**
 * Provee utilidades para los test
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 *
 */
public class Util {

	/**
	 * Retorna un vector de elementos a partir de una lista de los mismos.
	 *
	 * @param elements de clase T
	 * @return T[]
	 */
	public static <T> T[] getAsArray(T ... elements) {

		return elements;
	}
}
