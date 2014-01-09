package py.una.med.base.math;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 2.3.0
 * @version 1.0 Dec 27, 2013
 * 
 */
public class CurrencyAdapter extends XmlAdapter<String, Quantity> {

	/**
	 * Convierte un BigDecimal a un elemento base:Currency.
	 * 
	 * @param currency
	 *            currency a convertir a base:Currency.
	 * @return Cadena en base:Currency.
	 */
	public String marshal(final Quantity currency) {

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
	public Quantity unmarshal(final String currency) {

		if (currency.matches("^[0-9]+$")) {
			return new Quantity(currency);
		}
		return null;
	}

}
