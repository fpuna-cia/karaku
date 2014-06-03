package py.una.pol.karaku.exception;

public class KarakuWrongConfigurationFileException extends
		KarakuRuntimeException {

	private static final String MESSAGE = "Can't read file (%s), check the path and the permissions";
	/**
	 * 
	 */
	private static final long serialVersionUID = -5072670212836277745L;

	public KarakuWrongConfigurationFileException(String filename,
			Throwable cause) {
		super(String.format(MESSAGE, filename), cause);

	}

	public KarakuWrongConfigurationFileException(String filename) {
		super(String.format(MESSAGE, filename));
	}

}
