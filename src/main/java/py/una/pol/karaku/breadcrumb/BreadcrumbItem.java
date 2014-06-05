/*
 * 
 */
package py.una.pol.karaku.breadcrumb;

/**
 * Clase que sirve como auxilio de {@link BreadcrumbController} para representar
 * un item del breadcrum
 * 
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.1
 */
public class BreadcrumbItem {

	private String uri;

	private String name;

	public BreadcrumbItem(String uri, String name) {

		super();
		this.uri = uri;
		this.name = name;
	}

	public String getUri() {

		return uri;
	}

	public void setUri(String uri) {

		this.uri = uri;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BreadcrumbItem)) {
			return false;
		}
		BreadcrumbItem other = (BreadcrumbItem) obj;
		if (uri == null) {
			if (other.uri != null) {
				return false;
			}
		} else if (!uri.equals(other.uri)) {
			return false;
		}
		return true;
	}

}
