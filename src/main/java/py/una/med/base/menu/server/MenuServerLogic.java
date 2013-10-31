/*
 * @MenuServerLogic.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.menu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.log.Log;
import py.una.med.base.menu.schemas.Menu;
import py.una.med.base.util.I18nHelper;

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
	I18nHelper helper;

	@Autowired
	PropertiesUtil util;

	@Log
	Logger log;

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
					resource.getInputStream()));

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
			e.printStackTrace();
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
		HashSet<Menu> toSort = new HashSet<Menu>();
		toSort.add(menu);
		Menu next = menu;
		while (next != null) {

			toSort.remove(next);
			handleMenu(next);
			if ((next.getItems() != null) && !next.getItems().isEmpty()) {
				sortInMemory(next.getItems());
				for (Menu m : next.getItems()) {
					toSort.add(m);
				}
			}
			if (toSort.isEmpty()) {
				break;
			}
			Iterator<Menu> it = toSort.iterator();
			if (it.hasNext()) {
				next = it.next();
			} else {
				next = null;
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
	private void handleMenu(Menu menu) {

		if (skip(menu)) {
			return;
		}
		if (menu.getOrder() == 0) {
			menu.setOrder(Integer.MAX_VALUE);
		}
		menu.setName(helper.getString(menu.getName()));
		if (menu.getUrl() != null) {
			String pre = util.get("application.host");
			String url = menu.getUrl();
			if (!pre.endsWith("/") && !url.startsWith("/")) {
				pre += "/";
			}
			menu.setUrl(pre + url);
		}
	}
}
