package py.una.med.base.services.util;

import java.text.ParseException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.math.Quantity;
import py.una.med.base.util.FormatProvider;

/**
 *
 *
 * @author Arturo Volpe
 * @since 2.3.0
 * @version 1.0 Dec 27, 2013
 *
 */
public class NumberAdapter extends XmlAdapter<String, Quantity> {

	@Autowired
	private FormatProvider fp;

	public static final NumberAdapter INSTANCE = new NumberAdapter();

	/**
	 * Convierte un BigDecimal a un elemento base:Number.
	 *
	 * @param numero
	 *            BigDecimal a convertir a un base:Number.
	 * @return Cadena en base:Number
	 */
	public String marshal(final Quantity numero) {

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
	public Quantity unmarshal(final String decimal) {

		try {
			return INSTANCE.fp.parseLongQuantity(decimal);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Cant parse null BigIntegers", e);
		}

	}
}
