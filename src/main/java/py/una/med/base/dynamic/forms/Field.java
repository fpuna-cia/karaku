/*
 * @Field.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import py.una.med.base.util.ControllerHelper;
import py.una.med.base.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public abstract class Field {

	private static int idCounter = 0;

	/**
	 * Construye un nuevo field con un id generado
	 */
	public Field() {

		id = getClass().getSimpleName() + idCounter++ + "";
	}

	private String id;

	/**
	 * Retorna el tipo de este componente, se utiliza como URI para poder
	 * individualizar un tipo de componente.
	 * 
	 * @return Cadena que representa de manera unica un componente
	 */
	public abstract String getType();

	/**
	 * @return id
	 */
	public String getId() {

		return id;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	public void setId(String id) {

		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return getId();
	}

	public FacesContext getFacesContext() {

		return FacesContext.getCurrentInstance();
	}

	public abstract boolean disable();

	public abstract boolean enable();

	/**
	 * Dado un ID (vease {@link ControllerHelper#getClientId(String)}) retorna
	 * el componente al que pertenece
	 * 
	 * @param id
	 *            id del cliente para obtener el componente
	 * @return Componente de vista
	 */
	public UIComponent findComponent(String id) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		UIComponent c = findComponent(root, id);
		return c;
	}

	/**
	 * Finds component with the given id
	 */
	private UIComponent findComponent(UIComponent c, String id) {

		if (id.equals(c.getId())) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * Crea un mensaje para un componente determinado
	 * 
	 * @param severity
	 *            Severidad {@link FacesMessage}
	 * @param summary
	 *            Clave internacionalizada del sumario
	 * @param detail
	 *            Clave internacionalizada del detalle
	 * @param componentId
	 *            Nombre del componente,
	 *            {@link ControllerHelper#getClientId(String)}
	 */
	public void createFacesMessage(Severity severity, String summary,
			String detail) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage(severity, getMessage(summary),
				getMessage(detail));
		facesContext.addMessage(findComponent(getId()).getClientId(), msg);
	}

	/**
	 * Retorna el mensaje internacionalizado del codigo dado, para claves no
	 * encontradas retorna &&&&&code&&&&&
	 * 
	 * @param code
	 *            llave del mensaje
	 * @return cadena internacionalizada
	 */
	public String getMessage(String code) {

		if (code == null) {
			return getDegeneratedString("null");
		}
		if ("".equals(code)) {
			return getDegeneratedString("empty");
		}
		return I18nHelper.getMessage(code);
	}

	/**
	 * Dada una cadena la retorna degenerada.
	 * 
	 * @param code
	 * @return Cadena degenerada, con el formato &&&&&code&&&&&
	 */
	public String getDegeneratedString(String code) {

		return "&&&&&" + code + "&&&&&";
	}
}
