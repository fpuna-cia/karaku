/*
 * @ReplicationJSFController.java 1.0 Nov 26, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import static py.una.med.base.util.Checker.notNull;
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
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.controller.SIGHAdvancedController;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.client.WSEndpoint;
import py.una.med.base.services.client.WSEndpointLogic;
import py.una.med.base.util.ControllerHelper;
import py.una.med.base.util.LabelProvider;
import py.una.med.base.util.SelectHelper;
import py.una.med.base.util.StringUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Controller
@Scope(value = SIGHConfiguration.SCOPE_CONVERSATION)
public class ReplicationJSFController extends
		SIGHAdvancedController<ReplicationInfo, Long> {

	/**
	 * 
	 */
	private static final String BASE_PACKAGE_FOR_SCAN = "py.una.med";
	public static int ROWS_FOR_PAGE = 100;
	@Autowired
	private ReplicationInfoLogic logic;

	@Autowired
	private ControllerHelper helper;

	@Autowired
	private WSEndpointLogic endpointLogic;
	private List<SelectItem> endpoints;
	private Reflections reflections;
	private Set<SelectItem> entitiesC;
	private Set<SelectItem> responseC;
	private Set<SelectItem> requestC;
	private Set<SelectItem> dtoC;

	public ReplicationJSFController() {

		reflections = new Reflections(BASE_PACKAGE_FOR_SCAN,
				new SubTypesScanner(), new TypeAnnotationsScanner());
	}

	@Override
	public ISIGHBaseLogic<ReplicationInfo, Long> getBaseLogic() {

		return logic;
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
