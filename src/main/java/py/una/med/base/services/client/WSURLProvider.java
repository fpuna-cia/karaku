/*
 * @WSURLProvider.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

public interface WSURLProvider {

	String getByReturnType(Class<?> type);

	String getByKey(String key);
}
