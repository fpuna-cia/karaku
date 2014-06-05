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
package py.una.pol.karaku.menu.server;

import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.menu.schemas.MenuRequest;
import py.una.pol.karaku.menu.schemas.MenuResponse;
import py.una.pol.karaku.services.server.WebServiceDefinition;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 * 
 */
@WebServiceDefinition(xsds = { "/META-INF/schemas/menu/menu.xsd",
		"/META-INF/schemas/menu/menuMessages.xsd" })
public class MenuServiceEndpoint {

	public static final String TARGET_NAMESPACE = "http://sigh.med.una.py/2013/schemas/base";

	@Autowired
	private transient MenuServerLogic menuServerLogic;

	@Log
	private Logger log;

	@PayloadRoot(localPart = "menuRequest", namespace = TARGET_NAMESPACE)
	@ResponsePayload
	public MenuResponse menuRequest(@RequestPayload MenuRequest request) {

		if (request == null) {
			log.trace("Null MenuRequest");
		}
		MenuResponse toRet = new MenuResponse();
		List<Menu> menus = menuServerLogic.getCurrentSystemMenu();

		if (menus.size() == 1) {
			toRet.setMenu(menus.get(0));
		} else {
			Menu newRoot = new Menu();
			newRoot.setSkipThis("true");
			newRoot.setItems(menus);
			toRet.setSkipRoot("true");
			toRet.setMenu(newRoot);
		}
		return toRet;
	}
}
