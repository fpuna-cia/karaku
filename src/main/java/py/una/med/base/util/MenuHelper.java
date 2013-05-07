package py.una.med.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import py.una.med.base.domain.Menu;
import py.una.med.base.domain.Menu.Menus;

public final class MenuHelper {

	private MenuHelper() {

		// No-op
	}

	private static final String BASE_MENU = "src/main/resources/menu.xml";
	private static String menuActual = BASE_MENU;
	private static File f;
	private static FileReader fr;

	private static Menus actual;
	private static JAXBContext context;
	private static Marshaller m;
	private static Unmarshaller um;
	private static BufferedReader br;

	public static void main(String[] args) {

		br = new BufferedReader(new InputStreamReader(System.in));
		try {
			context = JAXBContext.newInstance(Menus.class);
			m = context.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		actualizarWriter();
		int opcionPrincipal;
		do {
			opcionPrincipal = menuPrincipal(br);
			try {
				switch (opcionPrincipal) {
					case 1:
						crear(br);
						break;
					case 2:
						imprimir(br);
						break;
					case 3:
						modificar(br, actual);
						break;
					case 4:
						cambiarMenu(br);
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} while (opcionPrincipal != -2);
	}

	private static void modificar(BufferedReader br, Menus menus)
			throws IOException, JAXBException {

		int opcionCrear;
		do {
			opcionCrear = menuM(br);
			switch (opcionCrear) {
				case -1:
					System.out.println("Ingrese una opcion valida");
					break;
				case 1:
					Menu tem = leerEntrada(br);
					menus.addMenu(tem);
					break;
				case 2:
					eliminar();
					break;
				case 3:
					modificarEntrada();
					break;
			}
		} while (opcionCrear != -2);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(menus, System.out);
		m.marshal(menus, f);
		actual = menus;
	}

	private static void modificarEntrada() {

		try {
			Menu aMod;
			do {
				aMod = getMenu();
				if (aMod == null) {
					System.out.println("Ingrese un id valido");
				}
			} while (aMod == null);
			int opcionModificar;
			StringBuilder sb;
			do {
				opcionModificar = menuModificar();
				switch (opcionModificar) {
					case -1:
						System.out.println("Ingrese una opcion valida");
						break;
					case 1:
						System.out.println("Actual:" + aMod.getId());
						aMod.setId(br.readLine());
						break;
					case 2:
						System.out.println("Actual:" + aMod.getName());
						aMod.setName(br.readLine());
						break;
					case 3:
						System.out.println("Actual:" + aMod.getUrl());
						aMod.setUrl(br.readLine());
						break;
					case 4:
						sb = new StringBuilder("[");
						for (String s : aMod.getPermissions()) {
							sb.append(s).append(",");
						}
						sb.append("]");
						System.out.println("Actual:" + sb.toString());
						aMod.addPermission(br.readLine());
						break;
					case 5:
						sb = new StringBuilder("[");
						for (String s : aMod.getPermissions()) {
							sb.append(s).append(",");
						}
						sb.append("]");
						System.out
								.println("Obs: eliminar todos los permisos que empiezen con al cadena");
						System.out.println("Actual:" + sb.toString());
						String aDel = br.readLine();
						List<String> aDels = new ArrayList<String>();
						for (String s : aMod.getPermissions()) {
							if (s.startsWith(aDel)) {
								aDels.add(s);
							}
						}
						for (String s : aDels) {
							aMod.getPermissions().remove(s);
						}
						break;
					case 6:
						System.out.println("Actual:" + aMod.getIdFather());
						aMod.setIdFather(br.readLine());
						break;
				}
			} while (opcionModificar != -2);
		} catch (Exception e) {
			System.out.println("ingrese un id valido" + e.getMessage());
		}
	}

	private static void eliminar() {

		try {
			Menu aDel = getMenu();
			if (aDel == null) {
				throw new Exception("Id no encontado");
			}
			actual.menus.remove(aDel);
		} catch (Exception e) {
			System.out.println("ingrese un id valido" + e.getMessage());
		}
	}

	private static Menu getMenu() throws IOException {

		System.out.println("ingrese un id");
		String id = br.readLine();
		Menu aDel = null;
		for (Menu m : actual.menus) {
			if (m.getId().equals(id)) {
				aDel = m;
			}
		}
		return aDel;
	}

	private static void imprimir(BufferedReader br) throws JAXBException {

		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(actual, System.out);
	}

	private static void crear(BufferedReader br) throws IOException,
			JAXBException {

		Menus menus = new Menus();
		modificar(br, menus);

	}

	private static void actualizarWriter() {

		try {
			f = new File(menuActual);
			if (!f.exists()) {
				f.createNewFile();
			} else {
				try {
					um = context.createUnmarshaller();
					fr = new FileReader(f);
					actual = (Menus) um.unmarshal(fr);
				} catch (Exception e) {
					System.out
							.println("Archivo existente invalido, incializando");
					actual = new Menus();
				}
			}
			System.out.println("Listo para escribir en " + f.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("No se pudo crear el archivo");
		}
	}

	private static void cambiarMenu(BufferedReader br) throws IOException {

		System.out.println("Ingrese una ruta a un archivo");
		menuActual = br.readLine();
		actualizarWriter();
	}

	public static Menu leerEntrada(BufferedReader br) throws IOException {

		Menu aRet = new Menu();
		boolean leido = false;
		System.out.println("Ingrese un id: ");
		do {
			aRet.setId(br.readLine());
			if (aRet.getId() == "") {
				return aRet;
			}
			leido = true;
		} while (!leido);
		System.out.println("Ingrese un nombre: ");
		aRet.setName(br.readLine());
		System.out.println("Ingrese la url: ");
		aRet.setUrl(br.readLine());
		String per = "";
		do {
			System.out
					.println("Ingrese un permiso, empieze con - para terminar");
			per = br.readLine();
			if (per != null && !per.startsWith("-")) {
				aRet.addPermission(per);
			}
		} while (per != null && !per.startsWith("-"));
		System.out.println("Ingrese un padre: ");
		do {
			try {
				aRet.setIdFather(br.readLine());
				if (aRet.getId() == "") {
					return aRet;
				}
				leido = true;
			} catch (Exception e) {
				System.out.println("Ingrese un numero para el padre");
				leido = false;
			}
		} while (!leido);
		return aRet;
	}

	public static int menuPrincipal(BufferedReader br) {

		System.out.println("Elija una opcion");
		System.out.println("1. Crear un menu");
		System.out.println("2. Imprimir un menu");
		System.out.println("3. Modificar un menu");
		System.out.println("4. Elejir un menu [" + BASE_MENU + "]");
		System.out.println("Otro. Salir");
		int leido;
		try {
			leido = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			leido = -1;
		}
		if (leido < -1 || leido > 4) {
			return -2;
		}
		return leido;
	}

	public static int menuM(BufferedReader br) {

		System.out.println("Elija una opcion");
		System.out.println("1. Agregar una entrada");
		System.out.println("2. Eliminar una entrada");
		System.out.println("3. Modificar una entrada");
		System.out.println("Otro. Salir");
		int leido;
		try {
			leido = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			leido = -1;
		}
		if (leido < -1 || leido > 3) {
			return -2;
		}
		return leido;
	}

	public static int menuModificar() {

		System.out.println("Elija una opcion");
		System.out.println("1. Cambiar id");
		System.out.println("2. Cambiar nombre");
		System.out.println("3. Cambiar url");
		System.out.println("4. Agregar permiso");
		System.out.println("5. Eliminar permiso");
		System.out.println("6. Cambiar padre");
		System.out.println("Otro. Salir");
		int leido;
		try {
			leido = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			leido = -1;
		}
		if (leido < -1 || leido > 6) {
			return -2;
		}
		return leido;
	}

	public static Menus createHierarchy(Menus input) {

		// XXX algorimto poco optimos
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
