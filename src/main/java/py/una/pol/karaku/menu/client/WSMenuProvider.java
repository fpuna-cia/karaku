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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.menu.schemas.MenuRequest;
import py.una.pol.karaku.menu.server.MenuServerLogic;
import py.una.pol.karaku.services.client.WSCallBack;
import py.una.pol.karaku.services.client.WSInformationProvider;
import py.una.pol.karaku.services.client.WSInformationProvider.Info;

/**
 * Component que provee las funcionalidades para consumir y mostrar menús.
 * 
 * <p>
 * Funciones de este componente:
 * <ol>
 * <li>Obtener menús de diferentes sistemas</li>
 * <li>Unir menús ya construidos</li>
 * </ol>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 21, 2013
 * 
 */
@Component
public class WSMenuProvider extends AbstractMenuProvider {

	/**
	 * 
	 */
	private static final String LOCAL_MENU_KEY = "LOCAL";

	/**
	 *
	 */
	private static final int CALL_DELAY = 300000;

	@Log
	private Logger logger;

	@Autowired
	private WSInformationProvider provider;

	/**
	 * Para obtener una referencia al menú del sistema actual.
	 */
	@Autowired
	private MenuServerLogic menuServerLogic;

	@Autowired
	private MenuWSCaller caller;

	@Autowired
	private PropertiesUtil properties;

	private List<Menu> menu;

	private Map<String, List<Menu>> menus;

	private boolean isDirty;

	private int numberOfMenus;
	private int currentCount;

	@Nonnull
	public static final String MENU_TAG = "WS_MENU";

	/**
	 * Tag de los items del menú.
	 */

	@PostConstruct
	void postConstruct() {

		// Vér mejor forma de conseguir la cantidad
		currentCount = 0;
		menu = new ArrayList<Menu>();
		List<Menu> local = menuServerLogic.getCurrentSystemMenu();
		menu.addAll(local);
		getMenus().put(LOCAL_MENU_KEY, local);
		isDirty = true;
	}

	@Scheduled(fixedDelay = CALL_DELAY)
	public void call() {

		if (!isEnabled()) {
			// Eliminar de la cola de peticiones
			return;
		}
		logger.trace("[BEGIN] Start scheduled task for menu sync");
		List<Info> providers = provider.getInfoByTag(MENU_TAG);
		numberOfMenus = providers.size();
		MenuRequest mr = new MenuRequest();
		for (Info i : providers) {
			logger.trace("[WORK] Quering for menu in {}", i.getUrl());
			caller.call(mr, i, new CallBack(i));
		}
		logger.trace("[FINISH] Cleaning up menu sync");
	}

	public synchronized void removeMenu(Info info) {

		getMenus().remove(info.getUrl());
		notifyMenuChange();
	}

	public synchronized void addOrUpdateMenu(Info info, List<Menu> object) {

		// only update if a hash.
		getMenus().put(info.getUrl(), object);
		notifyMenuChange();

	}

	public void notifyMenuChange() {

		isDirty = true;
		currentCount++;
		if (currentCount > numberOfMenus) {
			rebuild();
		}

	}

	@Override
	public List<Menu> getMenu() {

		if (isDirty) {
			rebuild();
		}
		return menu;
	}

	private void rebuild() {

		synchronized (menu) {
			if (!isDirty) {
				return;
			}
			logger.debug("Rebuilding menu");
			menu.clear();
			for (List<Menu> m : getMenus().values()) {
				menu.addAll(m);
			}

			Collections.sort(menu);
			currentCount = 0;
			isDirty = false;

			notifyMenuRebuild(menu);
		}
	}

	private synchronized Map<String, List<Menu>> getMenus() {

		if (menus == null) {
			menus = new HashMap<String, List<Menu>>();
		}

		return menus;
	}

	private class CallBack implements WSCallBack<List<Menu>> {

		private Info info;

		public CallBack(Info info) {

			this.info = info;
		}

		@Override
		public void onSucess(List<Menu> object) {

			logger.debug("Refresh menu from {}", info.getUrl());
			addOrUpdateMenu(info, object);
		}

		@Override
		public void onFailure(Exception exception) {

			logger.debug("Menu from url {} can not be found: {}",
					info.getUrl(), exception.getMessage());
			logger.trace("Removing menu ({})", info.getUrl());
			removeMenu(info);
		}

	}

	@Override
	public List<Menu> getLocalMenu() {

		return getMenus().get(LOCAL_MENU_KEY);
	}

	/**
	 * Define si esta habilitado el soporte para menús distribuidos.
	 * 
	 * <p>
	 * Si no esta habilitado el soporte a servicios no estara habilitado
	 * </p>
	 * 
	 * @return
	 */
	private boolean isEnabled() {

		return properties.getBoolean("karaku.ws.client.enabled", false);
	}

}
