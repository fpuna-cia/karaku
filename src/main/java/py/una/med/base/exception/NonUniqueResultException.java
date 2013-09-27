package py.una.med.base.exception;

/**
 * Excepción lanzada cuando la aplicación realiza una consulta que debe retornar
 * un solo resultado, pero retorna más resultados.
 * 
 * 
 * @author Uriel González
 * 
 */
public class NonUniqueResultException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construye un nuevo NonUniqueResultException con el número de resultados
	 * obtenidos.
	 * 
	 * @param size
	 *            número de resultados
	 */
	public NonUniqueResultException(final int size) {

		super("Query did not return a unique result: " + size);
	}
}
