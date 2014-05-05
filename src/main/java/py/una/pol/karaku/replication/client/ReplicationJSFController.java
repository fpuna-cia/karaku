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
package py.una.pol.karaku.replication.client;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.xml.bind.annotation.XmlType;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.richfaces.component.UIExtendedDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.configuration.KarakuBaseConfiguration;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.controller.KarakuAdvancedController;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.services.client.WSEndpoint;
import py.una.pol.karaku.services.client.WSEndpointLogic;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.LabelProvider;
import py.una.pol.karaku.util.SelectHelper;
import py.una.pol.karaku.util.StringUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Controller
@Scope(value = KarakuBaseConfiguration.SCOPE_CONVERSATION)
public class ReplicationJSFController extends
		KarakuAdvancedController<ReplicationInfo, Long> {

	private static final String DEFAULT_PERMISSION_KEY = "karaku.admin.permission";
	private static final String BASE_PACKAGE_FOR_SCAN = "py.una.med";
	public static final int ROWS_FOR_PAGE = 100;
	private static final String DEFAULT_PERMISSION_VALUE = "KARAKU_MAIN";

	@Autowired
	private ReplicationInfoLogic logic;

	@Autowired
	private PropertiesUtil util;

	@Autowired
	private ControllerHelper helper;

	@Autowired
	private WSEndpointLogic endpointLogic;
	private List<SelectItem> endpoints;
	private final Reflections reflections;
	private Set<SelectItem> entitiesC;
	private Set<SelectItem> responseC;
	private Set<SelectItem> requestC;
	private Set<SelectItem> dtoC;

	public ReplicationJSFController() {

		reflections = new Reflections(BASE_PACKAGE_FOR_SCAN,
				new SubTypesScanner(), new TypeAnnotationsScanner());
	}

	@Override
	public IKarakuBaseLogic<ReplicationInfo, Long> getBaseLogic() {

		return logic;
	}

	@Override
	public String getDefaultPermission() {

		return util.get(DEFAULT_PERMISSION_KEY, DEFAULT_PERMISSION_VALUE);
	}

	@Override
	public int getRowsForPage() {

		return ROWS_FOR_PAGE;
	}

	public List<SelectItem> getEndpoints() {

		if (endpoints == null) {
			Where<WSEndpoint> where = Where.get();
			where.addClause(Clauses.iLike("internalTag", "REPLICATION"));
			endpoints = SelectHelper.getSelectItems(
					endpointLogic.getAll(where, null),
					new LabelProvider<WSEndpoint>() {

						@Override
						public String getAsString(WSEndpoint object) {

							notNull(object);
							return object.getKey();
						}
					});
		}

		return endpoints;
	}

	public Set<SelectItem> getEntitiesClasses() {

		if (entitiesC == null) {
			entitiesC = this.getSetOfClasses(Shareable.class);
		}

		return entitiesC;
	}

	public Set<SelectItem> getDtoClasses() {

		if (dtoC == null) {
			dtoC = this.getSetOfClasses(DTO.class);
		}

		return dtoC;
	}

	public Set<SelectItem> getRequestClasses() {

		if (requestC == null) {
			requestC = this.getSetOfClasses("Request");
		}

		return requestC;
	}

	public Set<SelectItem> getResponseClasses() {

		if (responseC == null) {
			responseC = this.getSetOfClasses("Response");
		}

		return responseC;
	}

	public String getSimpleName(String className) {

		if (StringUtils.isInvalid(className)) {
			return "INVALID";
		}
		return className.substring(className.lastIndexOf('.') + 1);
	}

	/**
	 * Retorna un Set de {@link SelectItem}, cuyo valor es el nombre completo y
	 * label el nombre corto.
	 * 
	 * @param base
	 *            clase a buscar
	 * @return set de {@link SelectItem}
	 */
	private <T> Set<SelectItem> getSetOfClasses(Class<T> base) {

		Set<Class<? extends T>> classes = reflections.getSubTypesOf(base);
		notNull(classes);
		Set<SelectItem> toRet = new HashSet<SelectItem>(classes.size());
		for (Class<?> clazz : classes) {
			toRet.add(new SelectItem(clazz.getName(), clazz.getSimpleName()));
		}
		return toRet;
	}

	private <T> Set<SelectItem> getSetOfClasses(String suffix) {

		Set<Class<?>> classes = reflections
				.getTypesAnnotatedWith(XmlType.class);
		notNull(classes);
		Set<SelectItem> toRet = new HashSet<SelectItem>(classes.size());
		for (Class<?> clazz : classes) {
			if (clazz.getSimpleName().endsWith(suffix)) {
				toRet.add(new SelectItem(clazz.getName(), clazz.getSimpleName()));
			}
		}
		return toRet;
	}

	public void onActiveClicked(final AjaxBehaviorEvent event) {

		UIComponent check = (UIComponent) event.getSource();
		UIComponent uiColumn = check.getParent();
		UIExtendedDataTable dataTable = (UIExtendedDataTable) uiColumn
				.getParent();
		ReplicationInfo data = (ReplicationInfo) dataTable.getRowData();
		data.setActive(!data.isActive());
		logic.update(data);
		helper.createGlobalFacesMessageSimple(FacesMessage.SEVERITY_INFO,
				"Info for entity " + getSimpleName(data.getEntityClassName())
						+ " is now "
						+ (data.isActive() ? "active" : "inactive"));
	}
}
