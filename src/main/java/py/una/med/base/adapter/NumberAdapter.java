package py.una.med.base.adapter;

import java.math.BigDecimal;

/**
 *
 * Adapter para convertir el elemento base:Number a un BigDecimal y viceversa.
 *
 * @author Uriel González
 */
@Deprecated
public final class NumberAdapter {

	/**
	 *
	 */
	private NumberAdapter() {

	}

	/**
	 * Convierte un BigDecimal a un elemento base:Number.
	 *
	 * @param numero
	 *            BigDecimal a convertir a un base:Number.
	 * @return Cadena en base:Number
	 */
	public static String marshal(final BigDecimal numero) {

		return numero.toString();
	}

	/**
	 * Convierte un elemento base:Number a un BigDecimal.
	 *
	 * @param decimal
	 *            Elemento base:Number.
	 * @return en caso de que base:Number sea un número decimal sin signo cuya
	 *         parte decimal tiene como máximo hasta 4 cifras retorna su
	 *         representación en BigDecimal, en caso contrario retorna null.
	 */
	public static BigDecimal unmarshal(final String decimal) {

		if (decimal.matches("^[0-9]+(\\.[0-9]{1,4})?$")) {
			return new BigDecimal(decimal);

		} else {
			return null;
		}

	}
}
