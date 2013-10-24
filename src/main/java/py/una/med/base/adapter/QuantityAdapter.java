package py.una.med.base.adapter;

import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.math.Quantity;
import py.una.med.base.util.FormatProvider;

/**
 * 
 * Adapter para convertir el elemento base:Number a un {@link Quantity} y de un
 * {@link Quantity} a su representación en cadena.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 14, 2013
 */
public final class QuantityAdapter {

	@Autowired
	private FormatProvider fp;

	public static final QuantityAdapter INSTANCE = new QuantityAdapter();

	private QuantityAdapter() {

	}

	/**
	 * Convierte un BigDecimal a un elemento base:Number.
	 * 
	 * @param numero
	 *            BigDecimal a convertir a un base:Number.
	 * @return Cadena en base:Number
	 */
	public static String marshal(final Quantity numero) {

		return INSTANCE.fp.asNumber(numero);
	}

	/**
	 * Convierte un elemento base:Number a un BigDecimal.
	 * 
	 * @param decimal
	 *            Elemento base:Number.
	 * @return una nueva cantidad con el decimal especificado en un mismo
	 *         formato que {@link java.math.BigDecimal#BigDecimal(String)}.
	 */
	public static Quantity unmarshal(final String decimal) {

		try {
			return INSTANCE.fp.parseLongQuantity(decimal);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Cant parse null BigIntegers");
		}

	}
}
