/*
 * @MenuServerLogic.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.menu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.ListHelper;
import py.una.pol.karaku.util.StringUtils;

/**
 * Component que provee las funcionalidades para proveer menús.
 * 
 * <p>
 * Funciones de esta clase:
 * <ol>
 * <li>Obtener el menú del sistema actual, ver {@link #getCurrentSystemMenu()}</li>
 * <li>Construir un menú dado, ver {@link #configMenu(Menu)}</li>
 * 
 * </ol>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 * 
 */
@Component
public class MenuServerLogic {

	/**
	 * Ubicación de dondes se busca el menu del sistema.
	 */
	public static final String KARAKU_MENU_LOCATION_KEY = "karaku.menu.location";

	@Autowired
	private I18nHelper helper;

	@Autowired
	private PropertiesUtil util;

	/**
	 * Recibe un menú y lo construye.
	 * 
	 * <p>
	 * Se dice que un menú configurado es aquel que ya esta ordenado, todas sus
	 * cadenas están internacionalizadas y su URL es absoluta.
	 * </p>
	 * 
	 * @param m
	 *            menú a configurar
	 * @return menú ya configurado (misma instancia que el pasado como
	 *         parámetro)
	 */
	public Menu configMenu(Menu m) {

		handleRootMenu(m);
		return m;
	}

	/**
	 * Retorna el menú del sistema.
	 * 
	 * @return Menú del sistema actual
	 */
	public List<Menu> getCurrentSystemMenu() {

		try {
			Resource resource = new ClassPathResource(
					util.get(KARAKU_MENU_LOCATION_KEY));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					resource.getInputStream(), CharEncoding.ISO_8859_1));
			Unmarshaller um = JAXBContext.newInstance(Menu.class)
					.createUnmarshaller();
			Menu m = (Menu) um.unmarshal(reader);
			configMenu(m);
			if (skip(m)) {
				return m.getItems();
			}
			return Arrays.asList(m);
		} catch (UnsupportedEncodingException e) {
			throw new KarakuRuntimeException(
					"Cant open the menu (wrong encoding)", e);
		} catch (IOException e) {
			throw new KarakuRuntimeException(
					"Cant open the menu (file not found)", e);
		} catch (JAXBException e) {
			throw new KarakuRuntimeException("Cant open the menu (wrong XML)",
					e);
		}
	}

	private void handleRootMenu(Menu menu) {

		// Este hashSet se usa como un queue, solamente que es deseable que no
		// tenga valores repetidos
		NavigableSet<Menu> toSort = new TreeSet<Menu>();
		Map<Menu, Menu> parents = new HashMap<Menu, Menu>();
		toSort.add(menu);
		Menu next;
		while (!toSort.isEmpty()) {

			next = toSort.pollFirst();
			handleMenu(next, parents.get(next));
			if (ListHelper.hasElements(next.getItems())) {
				sortInMemory(next.getItems());
				toSort.addAll(next.getItems());
				for (Menu m : next.getItems()) {
					parents.put(m, next);
				}
			}
		}
	}

	private boolean skip(Menu menu) {

		return "true".equalsIgnoreCase(menu.getSkipThis());
	}

	private void sortInMemory(List<Menu> menus) {

		Collections.sort(menus);
	}

	/**
	 * Este método se ejecuta una sola vez por item en el menú. El orden no esta
	 * asegurado.
	 */
	private void handleMenu(Menu menu, Menu parent) {

		if (skip(menu)) {
			return;
		}
		if (menu.getOrder() == 0) {
			menu.setOrder(Integer.MAX_VALUE);
		}
		if (StringUtils.isValid(menu.getName())) {
			menu.setName(helper.getString(menu.getName().trim()));
		}
		if (menu.getUrl() != null) {
			String pre = util.get("application.host");
			String url = menu.getUrl().trim();
			if (!pre.endsWith("/") && !url.startsWith("/")) {
				pre += "/";
			}
			menu.setUrl(pre + url);
		}

		if (parent != null) {
			menu.setId(parent.getId() + "_" + menu.getId());
		}
	}
}
