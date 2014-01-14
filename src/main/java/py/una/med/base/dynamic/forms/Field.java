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
import py.una.med.base.util.ELHelper;
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

	private static final ELHelper EL_HELPER = ELHelper.INSTANCE;

	/**
	 * Construye un nuevo field con un id generado
	 */
	public Field() {

		this.id = this.getClass().getSimpleName() + getNewId();
	}

	private static String getNewId() {

		return String.valueOf(idCounter++);
	}

	private String id;

	/**
	 * Retorna el tipo de este componente, se utiliza como URI para poder
	 * individualizar un tipo de componente. Por defecto se utiliza el nombre
	 * completo de la clase.
	 * 
	 * @return Cadena que representa de manera única un componente
	 */
	public String getType() {

		return this.getClass().getName();
	}

	/**
	 * Retorna un identificador único de esta instancia, este identificador se
	 * genera incrementalmente por el numero de elementos dinámicos generados.
	 * 
	 * @return cadena que representa de manera única esta instancia de este
	 *         componente.
	 */
	public String getId() {

		return this.id;
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

		return this.getId();
	}

	public FacesContext getFacesContext() {

		return FacesContext.getCurrentInstance();
	}

	public abstract boolean disable();

	public abstract boolean enable();

	/**
	 * Dado un ID del lado del cliente (asignado en el XHTML o por código)
	 * retorna el componente al que pertenece.
	 * 
	 * @param id
	 *            id del cliente para obtener el componente
	 * @return Componente de vista
	 */
	public UIComponent findComponent(String id) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		return this.findComponent(root, id);
	}

	/**
	 * Recorre el árbol de componentes buscando un componente con el mismo id
	 * que el pasado como parámetro.
	 * 
	 * @param root
	 *            desde que elemento buscar
	 * @param id
	 *            identificador (lado server) del componente
	 * @return <code>null</code> si no se encuentra, o {@link UIComponent}
	 *         encontrado
	 */
	public UIComponent findComponent(UIComponent root, String id) {

		if (id.equals(root.getId())) {
			return root;
		}
		Iterator<UIComponent> kids = root.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = this.findComponent(kids.next(), id);
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
	 *            Nombre del componente
	 */
	public void createFacesMessage(Severity severity, String summary,
			String detail) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage(severity, this.getMessage(summary),
				this.getMessage(detail));
		facesContext.addMessage(this.findComponent(this.getId()).getClientId(),
				msg);
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
			return this.getDegeneratedString("null");
		}
		if ("".equals(code)) {
			return this.getDegeneratedString("empty");
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

	public ELHelper getElHelper() {

		return EL_HELPER;
	}
}
