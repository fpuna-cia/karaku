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

	@Override
	public String toString() {

		return getIdFather() + " -> " + getId();
	}

	@Override
	public boolean equals(Object otro) {

		if (otro == null) {
			return false;
		}
		if (otro.getClass() != Menu.class) {
			return false;
		}
		if (((Menu) otro).getId().equals(getId())) {
			return true;
		}
		return false;
	}

	@XmlRootElement(name = "menus")
	public static class Menus {

		public Menus() {

			menus = new ArrayList<Menu>();
		}

		@XmlElement(name = "menu")
		public List<Menu> menus;

		public void addMenu(Menu menu) {

			if (menus == null) {
				menus = new ArrayList<Menu>();
			}
			menus.add(menu);
		}

		public Menu getMenuByUrl(String url) {

			// TODO optimizar
			Menu aRet = null;
			if (menus == null)
				return null;
			for (Menu menu : menus) {
				aRet = findMenu(menu, url);
				if (aRet != null)
					return aRet;
			}
			return null;
		}

		private Menu findMenu(Menu root, String url) {

			if (root == null)
				return null;
			if (url.equals(root.getUrl()))
				return root;
			Menu aRet = null;
			for (Menu menu : root.getChildrens()) {
				aRet = findMenu(menu, url);
				if (aRet != null)
					return aRet;
			}
			return null;
		}

		private Menu findMenuById(Menu root, String id) {

			if (root == null)
				return null;
			if (id.equals(root.getId()))
				return root;
			Menu aRet = null;
			for (Menu menu : root.getChildrens()) {
				aRet = findMenuById(menu, id);
				if (aRet != null)
					return aRet;
			}
			return null;
		}

		public Menu getPadre(Menu hijo) {

			if (hijo.getIdFather() == null || "".equals(hijo.getIdFather()))
				return null;
			Menu aRet;
			for (Menu menu : menus) {
				aRet = findMenuById(menu, hijo.getIdFather());
				if (aRet != null)
					return aRet;
			}
			return null;
		}
	}

	public String getArbol() {

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

}
