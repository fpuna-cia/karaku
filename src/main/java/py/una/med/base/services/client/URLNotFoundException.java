package py.una.med.base.services.client;

import py.una.med.base.exception.KarakuRuntimeException;

public class URLNotFoundException extends KarakuRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3342578024752694309L;

	public URLNotFoundException(String key) {
		this(key, null);
	}

	public URLNotFoundException(String key, Throwable exception) {
		super("URL not found for key " + key, exception);
	}
}
