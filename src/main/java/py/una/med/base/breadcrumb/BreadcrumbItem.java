package py.una.med.base.breadcrumb;

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
	public boolean equals(Object obj) {

		if (obj == null)
			return false;
		if (obj instanceof BreadcrumbItem) {
			BreadcrumbItem other = (BreadcrumbItem) obj;
			if (other.uri == null || this.uri == null)
				return false;
			return uri.compareTo(other.uri) == 0;
		}
		return false;
	}
}
