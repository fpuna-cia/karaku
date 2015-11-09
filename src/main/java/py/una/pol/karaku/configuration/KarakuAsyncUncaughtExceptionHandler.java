package py.una.pol.karaku.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author Diego Ramírez
 * @version 1.0 30/10/15
 * @since 3.10
 */
public class KarakuAsyncUncaughtExceptionHandler
        implements AsyncUncaughtExceptionHandler {

    private Logger log = LoggerFactory.getLogger(KarakuBaseConfiguration.class);

    public void handleUncaughtException(Throwable ex, Method method,
            Object... params) {

        StringBuilder sb = new StringBuilder();

        sb.append("Error en el método asincrono ");
        sb.append(method.getName());

        log.error(sb.toString(), ex);

    }
}
