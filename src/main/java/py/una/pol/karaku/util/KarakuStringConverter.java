/*
 * @KarakuStringConverter.java 1.0 Mar 15, 2013 Sistema Integral de Gestión
 * Hospitalaria
 */
package py.una.pol.karaku.util;

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
public class KarakuStringConverter extends KarakuConverter {

	private LabelProvider provider;

	/**
	 * 
	 */
	public KarakuStringConverter() {

		this(null);
	}

	/**
	 * @param provider
	 */
	public KarakuStringConverter(final LabelProvider<?> provider) {

		super();
		this.provider = provider;
	}

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
