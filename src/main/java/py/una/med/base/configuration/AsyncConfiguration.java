package py.una.med.base.configuration;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Clase que agrega soporte para tareas asíncronas a Karaku,
 * 
 * @see <a href="http://appcia.cnc.una.py/wf/index.php/Asyn_task">Wiki</a>
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 17, 2013
 * 
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

	/**
	 * Tamaño por defecto del pool.
	 */
	private static final int DEFAULT_CORE_POOL_SIZE = 10;
	/**
	 * Tamaño máximo del pool.
	 */
	private static final int DEFAULT_CORE_POOL_MAX_SIZE = 20;
	/**
	 * Tamaño de la cola de espera.
	 */
	private static final int DEFAULT_ASYNC_QUEUE_SIZE = 10;

	/**
	 * Nombre por defecto de los hilos.
	 */
	private static final String DEFAULT_THREAD_PREFIX = "karaku-async-executor";

	@Autowired
	private PropertiesUtil properties;

	@Override
	public Executor getAsyncExecutor() {

		return asyncExecutor();
	}

	/**
	 * Crea un nuevo executor. Véase {@link #getAsyncExecutor()}
	 * 
	 * @see <a href="http://appcia.cnc.una.py/wf/index.php/Asyn_task">Wiki</a>
	 * @return {@link Executor} de tareas asíncronas
	 */
	@Bean
	public Executor asyncExecutor() {

		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(getInt("karaku.async.pool.size",
				DEFAULT_CORE_POOL_SIZE));
		executor.setMaxPoolSize(getInt("karaku.async.pool.max_size",
				DEFAULT_CORE_POOL_MAX_SIZE));
		executor.setQueueCapacity(getInt("karaku.async.queue.size",
				DEFAULT_ASYNC_QUEUE_SIZE));
		executor.setThreadNamePrefix(properties.get(
				"karaku.async.thread.prefix", DEFAULT_THREAD_PREFIX));
		executor.initialize();
		return executor;
	}

	/**
	 * Retorna el valor de la llave en el archivo de propiedades, si no se puede
	 * parsear lanzar {@link KarakuRuntimeException}
	 * 
	 * @param key
	 *            llave en el archivo de propiedades
	 * @param def
	 *            valor por defecto
	 * @throws KarakuRuntimeException
	 *             si no puede parseraa el valor del archivo de propiedades
	 * @return def si no se encuentra, en caso contrario el valor parseado.
	 */
	private int getInt(String key, int def) {

		try {
			return Integer.parseInt(properties.get(key, def + ""));
		} catch (NumberFormatException nfe) {
			throw new KarakuRuntimeException("The key " + key
					+ " doesn't contain a integer", nfe);
		}
	}
}
