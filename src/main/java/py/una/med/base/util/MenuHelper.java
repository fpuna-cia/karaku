package py.una.med.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.domain.Menu;
import py.una.med.base.domain.Menu.Menus;

/**
 * Componente que provee funcionalidades básica para manipular {@link Menu} y
 * {@link Menus}.
 **/
public class MenuHelper {

	private Menus menus;
	int maxDepth;
	/**
	 *
	 */
	private static final String MENU_ID_SEPARATOR = "__";
	private static Pattern pattern;
	private final static String SPLIT_REGEX = "[a-z]*/(.*/)*(.*)";
	private final static Logger log = LoggerFactory.getLogger(MenuHelper.class);
	private HashMap<String, Menu> menusIndexByURL;

	/**
	 *
	 * Dada una lista plana de menús, crea la jerarquía completa del mismo, en
	 * forma de un árbol con N raices.
	 *
	 * @param input
	 *            lista plana de menús
	 */
	public MenuHelper(Menus input) {

		Menus salida = new Menus();
		List<Menu> fatherless = new ArrayList<Menu>();
		salida.menus = new ArrayList<Menu>();
		for (Menu m : input.menus) {
			clean(m);
			addMenu(salida, m, fatherless);
		}
		salida.menus.addAll(fatherless);

		List<Menu> current = salida.getMenus();
		List<Menu> nextGen;
		maxDepth = 0;
		while (current != null && current.size() > 0) {
			nextGen = new ArrayList<Menu>(current.size());
			for (Menu m : current) {
				if (m.getFather() == null) {
					m.setDepth(0);
				}
				addMenuIndex(m);
				for (Menu child : m.getChildrens()) {
					child.setId(m.getId() + MENU_ID_SEPARATOR + child.getId());
					child.setIdFather(m.getId());
					child.setFather(m);
					nextGen.add(child);
					child.setDepth(m.getDepth() + 1);
					if (maxDepth < m.getDepth()) {
						maxDepth = m.getDepth();
					}
				}
			}
			current = nextGen;
		}

		this.menus = salida;
	}

	/**
	 * Elimina todos los problemas relacionados con el formato en el menú, como
	 * URL's con espacios al inicio y al fin.
	 *
	 * @param m
	 *            {@link Menu} a limpiar
	 */
	private void clean(Menu m) {

		if (m == null) {
			return;
		}
		if (m.getUrl() != null) {
			String url = m.getUrl().trim();
			m.setUrl(url);
		}
	}

	/**
	 * Retorna la estructura actual de los menús.
	 * <p>
	 * Los menús retornados por este método están completamente configurados.
	 * </p>
	 *
	 * @return menus
	 */
	public Menus getMenus() {

		return menus;
	}

	/**
	 * Retorna la mayor profundidad que alcanza el menú.
	 *
	 * @return maxDepth
	 */
	public int getMaxDepth() {

		return maxDepth;
	}

	private boolean addMenuIndex(@NotNull Menu m) {

		if (m.getUrl() == null || "".equals(m.getUrl())) {
			return false;
		}

		Matcher ma = getPattern().matcher(m.getUrl());
		if (!ma.matches()) {
			return false;
		}

		if (ma.groupCount() < 2) {
			log.warn("URL not found in menu (%s)", m.getUrl());
			return false;
		}
		getMenusIndexByURL().put(ma.group(1), m);
		return true;
	}

	/**
	 * Retorna un menú dado el inicio de la cadena que representa su URI
	 *
	 * <p>
	 *
	 * <pre>
	 * /faces/views/sistema/caso_de_uso/abm.xhml
	 * </pre>
	 *
	 * Y existe en el archivo de menús, una entrada con la url:
	 *
	 * <pre>
	 * /faces/views/sistema/caso_de_uso/list.xhtml
	 * </pre>
	 *
	 * Se retorna la entrada de ese menú, es decir no hay una coincidencia
	 * exacta, pero se hace el mejor esfuerzo según el caso de uso.
	 * </p>
	 *
	 *
	 * @param uri
	 *            cadena con el formato <code>(/?.*\/)*(.*)</code>
	 * @return {@link Menu} correspondiente a al URL, o <code>null</code> si no
	 *         esta indexado.
	 */
	public Menu getMenuByStartUrl(@NotNull String uri) {

		Matcher ma = getPattern().matcher(uri);
		if (!ma.matches() || ma.groupCount() < 2) {
			return null;
		}
		return getMenusIndexByURL().get(ma.group(1));
	}

	/**
	 * @return menusIndexByURL
	 */
	private HashMap<String, Menu> getMenusIndexByURL() {

		if (menusIndexByURL == null) {
			menusIndexByURL = new HashMap<String, Menu>();
		}
		return menusIndexByURL;
	}

	private boolean addMenu(Menus input, Menu newMenu, List<Menu> fatherless) {

		List<Menu> foundFahter = new ArrayList<Menu>();
		for (Menu menu : fatherless) {
			if (newMenu.getId().equals(menu.getIdFather())) {
				newMenu.getChildrens().add(menu);
				foundFahter.add(menu);
			}
		}
		for (Menu menu : foundFahter) {
			fatherless.remove(menu);
		}

		for (Menu root : input.menus) {
			if (addMenu(root, newMenu)) {
				return true;
			}
		}

		if (newMenu.getIdFather() == null || newMenu.getIdFather().equals("")) {
			input.menus.add(newMenu);
			return true;
		}
		fatherless.add(newMenu);
		return false;
	}

	private boolean exist(Menu father, Menu newChildren) {

		for (Menu children : father.getChildrens()) {
			if (children.equals(newChildren)) {
				return true;
			}
		}
		return false;
	}

	private boolean addMenu(Menu root, Menu newMenu) {

		if (newMenu.getIdFather() != null
				&& newMenu.getIdFather().equals(root.getId())) {
			if (!exist(root, newMenu)) {
				root.getChildrens().add(newMenu);
				return true;
			} else {
				return false;
			}
		}
		for (Menu children : root.getChildrens()) {
			if (addMenu(children, newMenu)) {
				return true;
			}
		}
		return false;
	}

	private static Pattern getPattern() {

		if (pattern == null) {
			pattern = Pattern.compile(SPLIT_REGEX, Pattern.CASE_INSENSITIVE);
		}
		return pattern;
	}

	/**
	 * Retorna un menú dada la cadena que representa su URI
	 *
	 * <p>
	 *
	 * <pre>
	 * /faces/views/sistema/caso_de_uso/abm.xhml
	 * </pre>
	 *
	 * Y existe en el archivo de menús, una entrada con la url:
	 *
	 * <pre>
	 * SAF / faces / views / sistema / caso_de_uso / list.xhtml
	 * </pre>
	 *
	 * Se retorna la entrada de ese menú, es decir no hay una coincidencia
	 * exacta, pero se hace el mejor esfuerzo según el caso de uso.
	 * </p>
	 *
	 *
	 * @param uri
	 *            cadena con el formato
	 *            <code>[a-z]{@literal *}/(.{@literal *}/)*(.*)</code>
	 * @return {@link Menu} correspondiente a al URL, o <code>null</code> si no
	 *         esta indexado.
	 */
	public Menu getMenuByUrl(String uri) {

		Matcher ma = getPattern().matcher(uri);
		if (!ma.matches() || ma.groupCount() < 2) {
			return null;
		}
		return getMenusIndexByURL().get(ma.group(1));
	}
}
