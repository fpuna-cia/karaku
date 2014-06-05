/*
 * 
 */
package py.una.pol.karaku.util;

public final class Serializer {

	private Serializer() {

		// No-op
	}

	/**
	 * Dado un stringBuilder, un key y un value retorna el sb concatenado con el
	 * key, seguido de dos puntos y finalizado con ;<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>Nombre: algun nombre ; Apellido ; algun apellido</b>, retorna
	 * Nombre: algun nombre; Apellido: algun apellido
	 * </ol>
	 * 
	 * @param sb
	 * @param key
	 * @param value
	 * 
	 * @return sb
	 */
	public static StringBuilder contruct(StringBuilder sb, String key,
			String value) {

		if (sb.length() > 0) {
			sb.append("; ");
		}
		sb.append(key);
		sb.append(": ");
		sb.append(value);
		return sb;

	}
}
