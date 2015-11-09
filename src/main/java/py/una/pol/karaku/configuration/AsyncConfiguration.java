/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.configuration;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Clase que agrega soporte para tareas asíncronas a Karaku,
 *
 * @author Arturo Volpe Torres
 * @version 1.0 Jun 17, 2013
 * @see <a href="http://appcia.cnc.una.py/wf/index.php/Asyn_task">Wiki</a>
 * @since 1.0
 */
@Configuration
@EnableAsync
@EnableScheduling
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

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return new KarakuAsyncUncaughtExceptionHandler();
    }

    /**
     * Crea un nuevo executor. Véase {@link #getAsyncExecutor()}
     *
     * @return {@link Executor} de tareas asíncronas
     * @see <a href="http://appcia.cnc.una.py/wf/index.php/Asyn_task">Wiki</a>
     */
    @Bean
    public AsyncTaskExecutor asyncExecutor() {

        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(
                getInt("karaku.async.pool.size", DEFAULT_CORE_POOL_SIZE));
        executor.setMaxPoolSize(getInt("karaku.async.pool.max_size",
                DEFAULT_CORE_POOL_MAX_SIZE));
        executor.setQueueCapacity(
                getInt("karaku.async.queue.size", DEFAULT_ASYNC_QUEUE_SIZE));
        executor.setThreadNamePrefix(properties
                .get("karaku.async.thread.prefix", DEFAULT_THREAD_PREFIX));
        // TODO cambiar por un SyncTaskExecutor
        return executor;
    }

    /**
     * Retorna el valor de la llave en el archivo de propiedades, si no se puede
     * parsear lanzar {@link py.una.pol.karaku.exception.KarakuRuntimeException}
     *
     * @param key llave en el archivo de propiedades
     * @param def valor por defecto
     * @return def si no se encuentra, en caso contrario el valor parseado.
     * @throws py.una.pol.karaku.exception.KarakuRuntimeException si no puede parsear el valor del archivo de propiedades
     */
    private int getInt(String key, int def) {

        return properties.getInteger(key, def);
    }

}
