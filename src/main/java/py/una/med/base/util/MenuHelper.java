package py.una.med.base.util;

import java.util.ArrayList;
import java.util.List;

import py.una.med.base.domain.Menu;
import py.una.med.base.domain.Menu.Menus;

public final class MenuHelper {

	private MenuHelper() {

		// No-op
	}

	public static Menus createHierarchy(Menus input) {

		// XXX algoritmo poco óptimos
		Menus salida = new Menus();
		List<Menu> fatherless = new ArrayList<Menu>();
		salida.menus = new ArrayList<Menu>();
		for (Menu m : input.menus) {
			addMenu(salida, m, fatherless);
		}
		salida.menus.addAll(fatherless);
		return salida;
	}

	private static boolean addMenu(Menus input, Menu newMenu,
			List<Menu> fatherless) {

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

	private static boolean exist(Menu father, Menu newChildren) {

		for (Menu children : father.getChildrens()) {
			if (children.equals(newChildren)) {
				return true;
			}
		}
		return false;
	}

	private static boolean addMenu(Menu root, Menu newMenu) {

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
}
