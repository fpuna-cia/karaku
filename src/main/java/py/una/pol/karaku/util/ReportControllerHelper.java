/*
 * @ReportControllerHelper.java 1.0 03/10/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.ExportReport;

/**
 * Helper utilizado para escuchar las peticiones de impresión de reportes entre
 * el ciclo JSF.
 *
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 03/10/2013
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@RequestMapping(value = "/print")
public class ReportControllerHelper {

	private transient Map<String, Holder> holder;

	@Autowired
	private transient ExportReport exportReport;

	private boolean load;

	/**
	 * Agrega un reporte a la cola de impresión de la aplicación.
	 *
	 * @param key
	 *            Identificador del reporte.
	 * @param print
	 *            Reporte creado que se desea exportar.
	 * @param name
	 *            Nombre del reporte.
	 * @param type
	 *            Tipo del reporte.
	 */
	public void addReport(final String key, final JasperPrint print,
			final String name, final String type, final String user) {

		this.getHolder().put(key, new Holder(print, name, type, user));

	}

	/**
	 * Imprime un reporte existente en la cola.
	 *
	 * @param key
	 *            Key que referencia al reporte que se desea imprimir
	 * @param response
	 *            Servlet spring utilizado para generar el reporte
	 * @throws ReportException
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void print(@PathVariable("id") final String id,
			final HttpServletResponse httpServletResponse)
			throws ReportException {

		this.setLoad(false);

		if (this.getHolder().containsKey(id)) {
			if (this.checkPermission(this.getHolder().get(id).getUser())) {

				final Holder holderPrint = this.getHolder().get(id);
				this.exportReport.generate(httpServletResponse,
						holderPrint.getJasperPrint(), holderPrint.getName(),
						holderPrint.getType());
				this.getHolder().remove(id);
			} else {
				throw new AccessDeniedException("Access denied");
			}
		} else {
			throw new KarakuRuntimeException("Report already printed");
		}
	}

	private boolean checkPermission(String user) {

		return user.equals(SecurityContextHolder.getContext()
				.getAuthentication().getName());
	}

	/**
	 * Verifica si existe un ticket en cola de impresión.
	 *
	 * @return<code>true</code> Sí existe al menos un reporte en la cola de
	 *                          impresión de la aplicación, <code>false</code>
	 *                          Caso contrario
	 */
	public boolean isLoad() {

		return this.load;
	}

	public void setLoad(boolean load) {

		this.load = load;
	}

	public Map<String, Holder> getHolder() {

		if (this.holder == null) {
			this.holder = new HashMap<String, Holder>();
		}

		return this.holder;
	}

	public void setHolder(Map<String, Holder> holder) {

		this.holder = holder;
	}

	private static class Holder {

		private final JasperPrint jasperPrint;
		private final String name;
		private final String type;
		private final String user;

		/**
		 * Constructor por defecto.
		 *
		 * @param jasperPrint
		 *            Reporte creado que se desea exportar.
		 * @param name
		 *            Nombre del reporte.
		 * @param user
		 *            Usuario que genero el reporte.
		 * @param type
		 *            Tipo del reporte.
		 */
		public Holder(final JasperPrint jasperPrint, final String name,
				final String type, final String user) {

			super();
			this.jasperPrint = jasperPrint;
			this.name = name;
			this.type = type;
			this.user = user;
		}

		/**
		 * Obtiene el jasperPrint del reporte.
		 *
		 * @return JasperPrint
		 */
		public JasperPrint getJasperPrint() {

			return this.jasperPrint;
		}

		/**
		 * Obtiene el nombre del archivo generado por el reporte.
		 *
		 * @return Nombre del reporte.
		 */
		public String getName() {

			return this.name;
		}

		/**
		 * Retorna el tipo de reporte a generar.
		 *
		 * @return Tipo de reporte.
		 */
		public String getType() {

			return this.type;
		}

		/**
		 * Retorna el usuario que genero el reporte.
		 *
		 * @return
		 */
		public String getUser() {

			return this.user;
		}

	}

}
