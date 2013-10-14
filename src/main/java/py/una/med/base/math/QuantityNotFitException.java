/*
 * @QuantityNotFitException.java 1.0 Oct 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.math;

import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Conversión de tipos que reducen la precisión no se permiten.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 8, 2013
 * 
 */
public final class QuantityNotFitException extends KarakuRuntimeException {

	private static final long serialVersionUID = -3053472034060084904L;

	/**
	 * Nueva instancia con el mensaje definido
	 */
	public QuantityNotFitException(Quantity from, Number to) {

		super(buildMessage(from, to));
	}

	private static final String buildMessage(Quantity from, Number to) {

		StringBuilder sb = new StringBuilder(
				"Precision loss detected, the quantity ");
		sb.append(from.toString());
		sb.append(" does not fit in").append(to.getClass());
		sb.append("(Converted value is: ").append(to);
		sb.append(")");
		return sb.toString();
	}
}
