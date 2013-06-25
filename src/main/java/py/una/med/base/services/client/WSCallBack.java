/*
 * @WSCallback.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import py.una.med.base.exception.KarakuException;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 11, 2013
 * 
 */
public interface WSCallBack<T> {

	void onSucess(T object);

	void onFailure(KarakuException exception);
}
