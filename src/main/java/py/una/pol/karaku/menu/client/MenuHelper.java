/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.menu.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.menu.schemas.Menu;

/**
 * Componente que provee funcionalidades básica para manipular {@link Menu}.
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 30, 2013
 *
 */
@Component
public class MenuHelper {

	private interface MenuWalker {

		void walk(Menu father, Menu child, boolean leaf);
	}

	@Autowired
	private transient IMenuProvider menuProvider;

	private transient Map<String, String> aliases;

	private transient Map<String, Menu> cachedByUri;

	private transient Map<Menu, Menu> fathersCached;

	/**
	 * Reinicia este componente.
	 *
	 * <p>
	 * Al reiniciarlo, obtiene nuevamente el menú del
	 * {@link py.una.pol.karaku.menu.server.MenuServerLogic}, y construye
	 * nuevamente su cache.
	 * </p>
	 */
	@PostConstruct
	public void createAlias() {

		aliases = new HashMap<String, String>();
		aliases.put("abm.xhtml", "CU");
		aliases.put("list.xhtml", "CU");

		rebuildAliasAndCached();
	}

	private synchronized void rebuildAliasAndCached() {

		cachedByUri = new HashMap<String, Menu>();
		fathersCached = new HashMap<Menu, Menu>();
		walkAllMenu(new MenuWalker() {

			@Override
			public void walk(Menu father, Menu m, boolean leaf) {

				if (leaf && (m.getUrl() != null)) {

					String url = m.getUrl();
					int index = url.indexOf("/views/", 0);
					if (index != -1) {
						url = url.substring(index);
					}
					cachedByUri.put(getAliasedUrl(url), m);
				}

				fathersCached.put(m, father);
			}
		});
	}

	private String replace(String src, String toReplace, String replacement) {

		return src.replaceAll(Pattern.quote(toReplace),
				Matcher.quoteReplacement(replacement));
	}

	/**
	 * @param current
	 * @return
	 */
	public Menu getFather(Menu current) {

		return fathersCached.get(current);
	}

	/**
	 * Retorna el menú dada su URL.
	 *
	 * <p>
	 * El formato de la url debe ser:
	 *
	 * <pre>
	 * SISTEMA / faces / views / XXXX.xhtml
	 * </per>
	 *
	 * </p>
	 *
	 * @param url
	 * @return
	 */
	public Menu getMenuByUrl(String url) {

		String real = getAliasedUrl(url);
		return cachedByUri.get(real);
	}

	/**
	 * @param url
	 * @return
	 */
	private String getAliasedUrl(String url) {

		String real = url;
		for (Entry<String, String> alias : aliases.entrySet()) {
			real = replace(real, alias.getKey(), alias.getValue());
		}
		return real;
	}

	protected List<Menu> getMenus() {

		return menuProvider.getLocalMenu();
	}

	private void walkAllMenu(MenuWalker mw) {

		List<Menu> root = getMenus();
		if ((root == null) || root.isEmpty()) {
			return;
		}
		for (Menu child : root) {

			walkAllMenu(null, child, mw);
		}
	}

	private void walkAllMenu(Menu father, Menu node, MenuWalker mw) {

		mw.walk(father, node, node.getItems().isEmpty());
		for (Menu child : node.getItems()) {
			walkAllMenu(node, child, mw);
		}
	}
}
