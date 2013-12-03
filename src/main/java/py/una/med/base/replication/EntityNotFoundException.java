/*
 * @ObjectNotFound.java 1.0 Nov 28, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication;

import py.una.med.base.exception.KarakuException;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 28, 2013
 * 
 */
public class EntityNotFoundException extends KarakuException {

	private static final String MESSAGE = "Can't get entity %s with the uri %s";

	/**
	 * 
	 */
	private static final long serialVersionUID = 3735749631559302678L;

	/**
	 * Crea una nueva excepción con un mensaje descriptivo para la entidad
	 * especifica que no fue encontrada.
	 * 
	 * @param uri
	 * @param className
	 */
	public EntityNotFoundException(String uri, String className) {

		super(String.format(MESSAGE, className, uri));
	}
}
