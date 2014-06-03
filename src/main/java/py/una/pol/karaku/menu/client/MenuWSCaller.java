/*
 * @MenuWSCaller.java 1.0 Oct 18, 2013 Sistema Integral de Gestion Hospitalaria
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
