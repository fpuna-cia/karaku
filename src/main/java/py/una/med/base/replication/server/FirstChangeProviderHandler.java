/*
 * @FirstChangeProviderHandler.java 1.0 Dec 4, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
@Service
public class FirstChangeProviderHandler {

	@Autowired
	private List<FirstChangeProvider<?>> providers;

	@PostConstruct
	@SuppressWarnings("rawtypes")
	void sort() {

		Collections.sort(providers, new Comparator<FirstChangeProvider>() {

			@Override
			public int compare(FirstChangeProvider arg0,
					FirstChangeProvider arg1) {

				return -arg0.getPriority().compareTo(arg1.getPriority());
			}
		});
	}

	/**
	 * Retorna el proveedor de cambios para la clase definida.
	 * 
	 * @return {@link FirstChangeProvider} encargado de proveer las entidades
	 *         del cambio.
	 * @throws IllegalStateException
	 *             si no se encuentra un cambio
	 */
	public FirstChangeProvider<?> getChangeProvider(Class<?> clazz) {

		for (FirstChangeProvider<?> fcp : providers) {
			if (fcp.getSupportedClass().isAssignableFrom(clazz)) {
				return fcp;
			}
		}
		throw new IllegalStateException(
				"Can't find first change provider for class: "
						+ clazz.getName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Bundle<T> getAll(Class<?> clazz) {

		FirstChangeProvider fcp = getChangeProvider(clazz);
		Collection s = fcp.getChanges(clazz);
		Bundle<T> bundle = new Bundle<T>();
		for (Object o : s) {
			bundle.add((T) o, Bundle.ZERO_ID);
		}
		return bundle;
	}
}
