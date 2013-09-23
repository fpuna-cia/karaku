package py.una.med.base.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa un item del menu,
 * 
 * @see Menus
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.1
 */
@XmlRootElement(name = "menu")
public class Menu {

	private String id;

	private String name;

	private String url;

	private List<String> permissions;

	private String idFather;

	private List<Menu> childrens;

	private Menu father;

	private int depth;

	/**
	 * Retorna la profundidad de este menú, la profundidad se define como la
	 * cantidad de padres que tiene en la jerarquía.
	 * 
	 * @return depth
	 */
	public int getDepth() {

		return depth;
	}

	/**
	 * @param depth
	 *            depth para setear
	 */
	public void setDepth(int depth) {

		this.depth = depth;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public List<String> getPermissions() {

		return permissions;
	}

	public void setPermissions(List<String> permissions) {

		this.permissions = permissions;
	}

	public String getIdFather() {

		return idFather;
	}

	public void setIdFather(String idFather) {

		this.idFather = idFather;
	}

	public void addPermission(String permission) {

		if (permissions == null) {
			permissions = new ArrayList<String>();
		}
		permissions.add(permission);
	}

	public List<Menu> getChildrens() {

		if (childrens == null) {
			childrens = new ArrayList<Menu>();
		}
		return childrens;
	}

	public void setChildrens(List<Menu> childrens) {

		this.childrens = childrens;
	}

	/**
	 * @param father
	 *            father para setear
	 */
	public void setFather(Menu father) {

		this.father = father;
	}

	/**
	 * @return father
	 */
	public Menu getFather() {

		return father;
	}

	@Override
	public String toString() {

		return getId();
	}

	@XmlRootElement(name = "menus")
	public static class Menus {

		private List<Menu> menus;

		public Menus() {

			menus = new ArrayList<Menu>();
		}

		public void addMenu(Menu menu) {

			if (menus == null) {
				menus = new ArrayList<Menu>();
			}
			menus.add(menu);
		}

		public Menu getMenuByUrl(String url) {

			// TODO optimizar
			Menu aRet = null;
			if (menus == null) {
				return null;
			}
			for (Menu menu : menus) {
				aRet = findMenu(menu, url);
				if (aRet != null) {
					return aRet;
				}
			}
			return null;
		}

		private Menu findMenu(Menu root, String url) {

			if (root == null) {
				return null;
			}
			if (url.endsWith(root.getUrl())) {
				return root;
			}
			Menu aRet = null;
			for (Menu menu : root.getChildrens()) {
				aRet = findMenu(menu, url);
				if (aRet != null) {
					return aRet;
				}
			}
			return null;
		}

		/**
		 * Retorna la lista de {@link Menu}.
		 * 
		 * @return menus
		 */
		@XmlElement(name = "menu")
		public List<Menu> getMenus() {

			if (menus == null) {
				menus = new ArrayList<Menu>(0);
			}

			return menus;
		}

		/**
		 * @param menus
		 *            menus para setear
		 */
		public void setMenus(List<Menu> menus) {

			this.menus = menus;
		}
	}

	/**
	 * Retorna una cadena que tiene la forma del árbol.
	 * 
	 * @return cadena con forma de árbol
	 */
	public String toVerboseString() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getName());
		for (Menu m : getChildrens()) {
			stringBuilder.append(m.pGetArbol(1));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	private String pGetArbol(int nivel) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getName());
		for (Menu m : getChildrens()) {
			for (int i = 0; i < nivel; i++) {
				stringBuilder.append("\t");
			}
			stringBuilder.append(m.pGetArbol(nivel + 1));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Menu)) {
			return false;
		}
		Menu other = (Menu) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
