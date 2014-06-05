/*
 * 
 */
package py.una.pol.karaku.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.springframework.stereotype.Component;

@ManagedBean(name = "language")
@SessionScoped
@Component
public class LanguageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Map<String, Locale> countries;
	static {
		countries = new LinkedHashMap<String, Locale>();
		countries.put("English", Locale.ENGLISH);
		countries.put("Español", new Locale("es", "PY"));
	}

	public Map<String, Locale> getCountriesInMap() {

		return countries;
	}

	public Locale getCurrentLocale() {

		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public void countryLocaleCodeChanged(ValueChangeEvent e) {

		String newLocaleValue = e.getNewValue().toString();

		// loop a map to compare the locale code
		for (Map.Entry<String, Locale> entry : countries.entrySet()) {

			if (entry.getValue().toString().equals(newLocaleValue)) {

				FacesContext.getCurrentInstance().getViewRoot()
						.setLocale(entry.getValue());

			}
		}

	}

}
