/*
 * @Quantity.java 1.0 Oct 8, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.math;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representa un valor numérico con precisión limitada.
 * 
 * <p>
 * Clase central para las operaciones matemáticas. Es decir, solamente las
 * operaciones que se ejecutan con esta clase son probadas y se puede garantizar
 * su precisión.
 * </p>
 * <p>
 * Es inmutable y provee redondeo paso por paso, es decir, cada operación de
 * esta clase retorna un objeto ya redondeado y nuevo.
 * </p>
 * 
 * @see MathContextProvider
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 8, 2013
 * 
 */
public class Quantity extends Number implements Comparable<Quantity>,
		Serializable {

	private static final long serialVersionUID = 1084173922837888565L;
	private MathContextProvider mcp;
	private BigDecimal wrapped;

	public static final Quantity ZERO = new Quantity();
	public static final Quantity ONE = new Quantity(1);
	public static final Quantity TWO = new Quantity(2);
	public static final Quantity THREE = new Quantity(3);
	public static final Quantity FOUR = new Quantity(4);

	/**
	 * Nueva cantidad con valor 0.
	 */
	public Quantity() {

		this(BigDecimal.ZERO);
	}

	/**
	 * Nueva cantidad con el valor del parámetro.
	 * 
	 * <p>
	 * Este constructor automáticamente redondea el decimal para adecuarlo a la
	 * precisión definida en al aplicación
	 * </p>
	 * 
	 * @param bigDecimalValue
	 *            {@link BigDecimal} de donde se quita el valor.
	 */
	public Quantity(BigDecimal bigDecimalValue) {

		mcp = MathContextProvider.INSTANCE;

		wrapped = new BigDecimal(bigDecimalValue.toString(), mcp.getContext());
		wrapped = wrapped.setScale(mcp.getScale(), mcp.getRoundingMode());

	}

	/**
	 * Nueva cantidad con el valor del parámetro.
	 * 
	 * <p>
	 * Este constructor automáticamente redondea el numero en punto flotante
	 * para adecuarlo a la precisión definida en al aplicación
	 * </p>
	 * 
	 * @param doubleValue
	 *            {@link Double} de donde se quita el valor.
	 */
	public Quantity(Double doubleValue) {

		this(new BigDecimal(doubleValue.toString()));
	}

	/**
	 * Nueva cantidad con el valor del parámetro.
	 * 
	 * <p>
	 * Este constructor automáticamente redondea el numero entero para adecuarlo
	 * a la precisión definida en al aplicación.
	 * </p>
	 * 
	 * @param intvalue
	 *            {@link Integer} de donde se quita el valor.
	 */
	public Quantity(Integer intvalue) {

		this(new BigDecimal(intvalue));
	}

	/**
	 * Nueva cantidad con el valor del parámetro.
	 * 
	 * <p>
	 * Este constructor automáticamente redondea el numero largo para adecuarlo
	 * a la precisión definida en al aplicación.
	 * </p>
	 * 
	 * @param intvalue
	 *            {@link Long} de donde se quita el valor.
	 */
	public Quantity(Long longValue) {

		this(new BigDecimal(longValue));
	}

	/**
	 * Nueva cantidad con el valor parseado del parámetro.
	 * 
	 * <p>
	 * Este método realizar parsea la cadena para obtener la cantidad, acepta
	 * una cantidad variada de formas de cadenas, <b>y es el constructor que
	 * debe ser preferido para esta clase</b>.
	 * </p>
	 * 
	 * <p>
	 * <i>Para detalles de las cadenas que pueden ser pasadas, ver
	 * {@link BigDecimal#BigDecimal(String)}<i> ya que este constructor
	 * simplemente es un wrapper del citado.
	 * </p>
	 * 
	 * @see BigDecimal#BigDecimal(String)
	 * @param string
	 *            cadena a parsear
	 */
	public Quantity(String string) {

		this(new BigDecimal(string));
	}

	/**
	 * Retorna el valor absoluto de esta cantidad.
	 * <p>
	 * Si la entidad ya es positiva, entonces se retorna a si misma, en cambio,
	 * si la cantidad es negativa, retorna una nueva cantidad con el signo
	 * cambiado.
	 * </p>
	 * <p>
	 * Se considera a 0 como positivo
	 * </p>
	 * 
	 * @return <code>this</code> si ya es positiva, nueva cantidad con el signo
	 *         cambiado en caso contrario.
	 */
	public Quantity abs() {

		if (getWrapped().signum() < 0) {
			return new Quantity(getWrapped().abs());
		} else {
			return this;
		}
	}

	/**
	 * Retorna una cantidad aumentada cuyo valor es this + toSum.
	 * 
	 * <p>
	 * A diferencia del método {@link BigDecimal#add(BigDecimal)}, este método
	 * siempre retorna la misma precisión y escala.
	 * </p>
	 * 
	 * @param toSum
	 *            cantidad a sumar
	 * @return {@link Quantity} cuyo valor es: this + toSum
	 */
	public Quantity add(Quantity toSum) {

		return new Quantity(getWrapped().add(toSum.getWrapped()));
	}

	/**
	 * Retorna un {@link BigDecimal} con el mismo valor que esta cantidad.
	 * 
	 * @return {@link BigDecimal} con el mismo valor que la cantidad.
	 */
	public BigDecimal bigDecimalValue() {

		return new BigDecimal(getWrapped().toString());
	}

	private void checkConversion(Number to) {

		if (getWrapped().compareTo(new BigDecimal(to.toString())) != 0) {
			throw new QuantityNotFitException(this, to);
		}
	}

	/**
	 * Compara dos cantidades.
	 * 
	 * <p>
	 * Retorna:
	 * <ol>
	 * <li>{@literal <} 0: si es menor a otherQuantity</li>
	 * <li>0: si es igual a otherQuantity</li>
	 * <li>{@literal >} a 0: si es mayor a otherQuantity</li>
	 * </ol>
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	public int compareTo(Quantity otherQuantity) {

		return this.getWrapped().compareTo(otherQuantity.getWrapped());
	}

	/**
	 * Retorna una cantidad cuyo valor es <code>this/bigDecimal</code>.
	 * 
	 * @param bigDecimal
	 *            factor por el cual dividir
	 * @return nueva instancia con el valor <code>this/bigDecimal</code>
	 * @see BigDecimal#divide(BigDecimal, java.math.MathContext)
	 */
	public Quantity divide(BigDecimal bigDecimal) {

		return new Quantity(getWrapped().divide(bigDecimal, mcp.getScale(),
				mcp.getRoundingMode()));
	}

	/**
	 * Retorna una cantidad cuyo valor es <code>this/integer</code>.
	 * 
	 * @param integer
	 *            factor por el cual dividir
	 * @return nueva instancia con el valor <code>this/bigDecimal</code>
	 * @see #divide(BigDecimal)
	 */
	public Quantity divide(int integer) {

		return divide(new BigDecimal(integer));
	}

	/**
	 * Retorna una cantidad cuyo valor es <code>this/integer</code>.
	 * 
	 * @param integer
	 *            factor por el cual dividir
	 * @return nueva instancia con el valor <code>this/quantity</code>
	 * @see #divide(BigDecimal)
	 */
	public Quantity divide(Quantity quantity) {

		return divide(quantity.getWrapped());
	}

	@Override
	public double doubleValue() {

		double toRet = getWrapped().doubleValue();
		checkConversion((Double) toRet);
		return toRet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see BigDecimal#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Quantity)) {
			return false;
		}
		return compareTo((Quantity) obj) == 0;
	}

	@Override
	public float floatValue() {

		Float toRet = getWrapped().floatValue();
		checkConversion(toRet);
		return toRet;
	}

	private BigDecimal getWrapped() {

		return wrapped;
	}

	@Override
	public int intValue() {

		Integer toRet = getWrapped().intValue();
		checkConversion(toRet);
		return toRet;
	}

	@Override
	public long longValue() {

		Long toRet = getWrapped().longValue();
		checkConversion(toRet);
		return toRet;
	}

	/**
	 * Retorna el valor negativo de esta cantidad.
	 * <p>
	 * Si la entidad ya es negativa, entonces se retorna a si misma, en cambio,
	 * si la cantidad es positiva, retorna una nueva cantidad con el signo
	 * cambiado.
	 * </p>
	 * <p>
	 * Se considera a 0 como negativo
	 * </p>
	 * 
	 * @return <code>this</code> si ya es negativa, nueva cantidad con el signo
	 *         cambiado en caso contrario.
	 */
	public Quantity negate() {

		if (getWrapped().signum() > 0)
			return new Quantity(getWrapped().negate());
		else
			return this;
	}

	/**
	 * Retorna una cantidad aumentada cuyo valor es this - toSum.
	 * 
	 * <p>
	 * A diferencia del método {@link BigDecimal#subtract(BigDecimal)}, este
	 * método siempre retorna la misma precisión y escala.
	 * </p>
	 * 
	 * @param toSubstract
	 *            cantidad a restar
	 * @return {@link Quantity} cuyo valor es: this - toSum
	 */
	public Quantity subtract(Quantity toSubstract) {

		return new Quantity(getWrapped().subtract(toSubstract.getWrapped()));
	}

	/**
	 * Retorna una cantidad cuyo valor es <code>this * bigDecimal</code>.
	 * 
	 * @param other
	 *            factor por el cual multiplicar
	 * @return nueva instancia con el valor <code>this * bigDecimal</code>
	 * @see BigDecimal#multiply(BigDecimal, java.math.MathContext)
	 */
	public Quantity times(BigDecimal other) {

		return new Quantity(getWrapped().multiply(other, mcp.getContext()));
	}

	/**
	 * Retorna una cantidad cuyo valor es <code>this * integer</code>.
	 * 
	 * @param integer
	 *            factor por el cual multiplicar
	 * @return nueva instancia con el valor <code>this * integer</code>
	 * @see #times(BigDecimal)
	 * 
	 */
	public Quantity times(int integer) {

		return times(new BigDecimal(integer));
	}

	/**
	 * Retorna una cantidad cuyo valor es <code>this * quantity</code>.
	 * 
	 * @param quantity
	 *            factor por el cual multiplicar
	 * @return nueva instancia con el valor <code>this * quantity</code>
	 * @see #times(BigDecimal)
	 * 
	 */
	public Quantity times(Quantity quantity) {

		return times(quantity.getWrapped());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see BigDecimal#toString()
	 */
	@Override
	public String toString() {

		return getWrapped().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {

		return wrapped.hashCode();
	}
}
