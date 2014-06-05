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
package py.una.pol.karaku.test.util;

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
import py.una.pol.karaku.util.ControllerHelper;

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
	 * Retorna el Ãºltimo mensaje generado.
	 * 
	 * @return lastMessage Ãºltimo mensaje, <code>null</code> si no se agregaron
	 *         mensajes.
	 */
	public FacesMessage getLastMessage() {

		return lastMessage;
	}

	/**
	 * Retorna el <i>id</id> del Ãºltimo componente al que le fue agregado un
	 * mensaje.
	 * 
	 * @return lastMessageComponentId Ãºltimo id, <code>null</code> si no se
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
