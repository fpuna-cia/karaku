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

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.exception.KarakuException;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.menu.schemas.MenuRequest;
import py.una.pol.karaku.menu.schemas.MenuResponse;
import py.una.pol.karaku.services.client.WSCallBack;
import py.una.pol.karaku.services.client.WSCaller;
import py.una.pol.karaku.services.client.WSInformationProvider.Info;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 * 
 */
@Component
public class MenuWSCaller {

	@Autowired
	private WSCaller wSCaller;

	/**
	 * @param request
	 * @param callback
	 * @see py.una.pol.karaku.services.client.WSCaller#call(java.lang.Object,
	 *      py.una.pol.karaku.services.client.WSCallBack)
	 */
	public void call(MenuRequest request, Info info,
			final WSCallBack<List<Menu>> callback) {

		wSCaller.call(request, info, new WSCallBack<MenuResponse>() {

			@Override
			public void onFailure(Exception exception) {

				callback.onFailure(exception);
			}

			@Override
			public void onSucess(MenuResponse object) {

				if (object.getMenu() == null) {
					onFailure(new KarakuException("Null menu from WS"));
				}
				callback.onSucess(buildResponse(object));
			}
		});
	}

	private List<Menu> buildResponse(MenuResponse response) {

		if ("true".equalsIgnoreCase(response.getSkipRoot())
				|| "true".equalsIgnoreCase(response.getMenu().getSkipThis())) {
			return response.getMenu().getItems();
		}
		return Arrays.asList(response.getMenu());
	}
}
