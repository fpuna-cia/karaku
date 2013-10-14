package py.una.med.base.adapter;

import py.una.med.base.math.Quantity;

/**
 * Adapter para convertir el elemento base:Currency a un BigDecimal y viceversa.
 * 
 * 
 * @author Uriel González
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 14, 2013
 * 
 */
public class CurrencyAdapter {

	/**
	 * Convierte un BigDecimal a un elemento base:Currency.
	 * 
	 * @param currency
	 *            currency a convertir a base:Currency.
	 * @return Cadena en base:Currency.
	 */
	public static String marshal(final Quantity currency) {

		return currency.toString();

	}

	/**
	 * Convierte un elemento base:Currency a un Quantity.
	 * 
	 * @param currency
	 *            elemento base:currency.
	 * @return en caso de que base:Currency sea un número entero sin signo
	 *         retorna su representación en BigDecimal, en caso contrario
	 *         retorna null.
	 */
	public static Quantity unmarshal(final String currency) {

		if (currency.matches("^[0-9]+$")) {
			return new Quantity(currency);
		} else {
			return null;
		}
	}
}
