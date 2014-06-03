/*
 * @FirstChangeProviderHandler.java 1.0 Dec 4, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.server;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.una.pol.karaku.util.StringUtils;

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

				return arg1.getPriority().compareTo(arg0.getPriority());
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

	/**
	 * Retorna todas las entidades con un identificador {@link Bundle#ZERO_ID}.
	 * 
	 * @param clazz
	 *            clase a verificar
	 * @return cambios
	 */
	@Nonnull
	public <T> Bundle<T> getAll(@Nonnull Class<?> clazz) {

		return getAll(clazz, Bundle.ZERO_ID);
	}

	/**
	 * Retorna el grupo de cambios con ultima modificacion el parámetro pasado.
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Nonnull
	public <T> Bundle<T> getAll(@Nonnull Class<?> clazz, @Nonnull String id) {

		notNull(clazz, "Can't get bundle of null class");
		String currentId = id;
		if (StringUtils.isInvalid(id)) {
			currentId = Bundle.ZERO_ID;
		}
		FirstChangeProvider fcp = getChangeProvider(clazz);
		Collection s = fcp.getChanges(clazz);
		Bundle<T> bundle = new Bundle<T>();
		for (Object o : s) {
			if (o == null) {
				continue;
			}
			bundle.add((T) o, currentId);
		}
		return bundle;
	}
}
