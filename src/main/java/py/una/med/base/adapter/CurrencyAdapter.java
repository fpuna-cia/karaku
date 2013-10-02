package py.una.med.base.adapter;

import java.math.BigDecimal;

/**
 * Adapter para convertir el elemento base:Currency a un BigDecimal y viceversa.
 * 
 * @author Uriel González
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
	public static String marshal(final BigDecimal currency) {

		return currency.toString();

	}

	/**
	 * Convierte un elemento base:Currency a un BigDecimal.
	 * 
	 * @param currency
	 *            elemento base:currency.
	 * @return en caso de que base:Currency sea un número entero sin signo
	 *         retorna su representación en BigDecimal, en caso contrario
	 *         retorna null.
	 */
	public static BigDecimal unmarshal(final String currency) {

		if (currency.matches("^[0-9]+$")) {
			return new BigDecimal(currency);
		} else {
			return null;
		}
	}
}
