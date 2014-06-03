/*
 * @SIGHStringConverter.java 1.0 Mar 15, 2013 Sistema Integral de Gestión
 * Hospitalaria
 */
package py.una.med.base.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 15, 2013
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SIGHStringConverter extends SIGHConverterV2 {

	private LabelProvider provider;

	/**
	 * 
	 */
	public SIGHStringConverter() {

		this(null);
	}

	/**
	 * @param provider
	 */
	public SIGHStringConverter(final LabelProvider<?> provider) {

		super();
		this.provider = provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * py.una.med.base.util.SIGHConverterV2#getAsObject(javax.faces.context.
	 * FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {

		if (provider == null) {
			return super.getAsObject(context, component, value);
		} else {
			return provider.getAsString(super.getAsObject(context, component,
					value));
		}
	}
}
