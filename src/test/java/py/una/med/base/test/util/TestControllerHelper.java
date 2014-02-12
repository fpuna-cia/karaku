/*
 * @TestControllerHelper.java 1.0 Feb 11, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.util;

import java.util.Iterator;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import org.richfaces.component.UICalendar;
import py.una.med.base.util.ControllerHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 11, 2014
 * 
 */
public class TestControllerHelper extends ControllerHelper {

	private FacesMessage lastMessage;
	private String lastMessageComponentId;

	@Override
	public void createFacesMessageSimple(Severity severity, String summary,
			String detail, String componentId) {

		super.createFacesMessageSimple(severity, summary, detail, componentId);
	}

	/**
	 * Retorna el último mensaje generado.
	 * 
	 * @return lastMessage último mensaje, <code>null</code> si no se agregaron
	 *         mensajes.
	 */
	public FacesMessage getLastMessage() {

		return lastMessage;
	}

	/**
	 * Retorna el <i>id</id> del último componente al que le fue agregado un
	 * mensaje.
	 * 
	 * @return lastMessageComponentId último id, <code>null</code> si no se
	 *         agregaron mensajes.
	 */
	public String getLastMessageComponentId() {

		return lastMessageComponentId;
	}

	/**
	 * Limpia todos los mensajes mostrados
	 */
	public void clearMessages() {

		lastMessage = null;
		lastMessageComponentId = null;
	}

	@Override
	protected FacesContext getContext() {

		return new FacesContext() {

			@Override
			public void setViewRoot(UIViewRoot root) {

			}

			@Override
			public void setResponseWriter(ResponseWriter responseWriter) {

			}

			@Override
			public void setResponseStream(ResponseStream responseStream) {

			}

			@Override
			public void responseComplete() {

			}

			@Override
			public void renderResponse() {

			}

			@Override
			public void release() {

			}

			@Override
			public UIViewRoot getViewRoot() {

				return null;
			}

			@Override
			public ResponseWriter getResponseWriter() {

				return null;
			}

			@Override
			public ResponseStream getResponseStream() {

				return null;
			}

			@Override
			public boolean getResponseComplete() {

				return false;
			}

			@Override
			public boolean getRenderResponse() {

				return false;
			}

			@Override
			public RenderKit getRenderKit() {

				return null;
			}

			@Override
			public Iterator<FacesMessage> getMessages(String clientId) {

				return null;
			}

			@Override
			public Iterator<FacesMessage> getMessages() {

				return null;
			}

			@Override
			public Severity getMaximumSeverity() {

				return null;
			}

			@Override
			public ExternalContext getExternalContext() {

				return null;
			}

			@Override
			public Iterator<String> getClientIdsWithMessages() {

				return null;
			}

			@Override
			public Application getApplication() {

				return null;
			}

			@Override
			public void addMessage(String clientId, FacesMessage message) {

				lastMessageComponentId = clientId;
				lastMessage = message;
			}
		};
	}

	@Override
	public UIComponent findComponent(String id) {

		UIComponent ui = new UICalendar();
		ui.setId(id);
		return ui;
	}

	@Override
	public String getClientId(String id) {

		return id;
	}
}
