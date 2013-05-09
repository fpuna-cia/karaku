/*
 * @ComboBoxField.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.LabelProvider;
import py.una.med.base.util.LabelProvider.StringLabelProvider;
import py.una.med.base.util.ListHelper;
import py.una.med.base.util.SIGHConverterV2;
import py.una.med.base.util.SelectHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class ComboBoxField<T> extends LabelField {

	/**
	 * 
	 */
	private static final String DEFAULT_LABEL_ID = "seleccionar";
	private static final String DEFAULT_LABEL_TEXT = "COMBO_BOX_DEFAULT_VALUE";

	/**
	 * Tipo de este objeto
	 */
	public static final String TYPE = "py.una.med.base.dynamic.forms.ComboBoxField";

	private SIGHConverterV2 converter;
	private HtmlSelectOneMenu bind;
	private ValueChangeListener changeListener;
	private List<String> toRender;
	private boolean withDefault;
	private boolean defaultAdded = false;

	private AjaxBehavior ajax;

	private UISelectItem defaultSelectItem;

	public ComboBoxField() {

		this(true);
	}

	public ComboBoxField(final boolean withDefault) {

		super();
		this.withDefault = withDefault;
		converter = new SIGHConverterV2();
		this.bind = SIGHComponentFactory.getNewSelectOneMenu();
		this.bind.setId(getId());
		this.bind.setConverter(converter);
		toRender = new ArrayList<String>();
	}

	private UISelectItem getDefaultSelectItem() {

		if (defaultSelectItem == null) {
			defaultSelectItem = SIGHComponentFactory.getNewSelectItem();
			defaultSelectItem.setItemLabel(I18nHelper
					.getMessage(DEFAULT_LABEL_TEXT));
			defaultSelectItem.setId(getId() + "_" + DEFAULT_LABEL_ID);
		}

		return defaultSelectItem;
	}

	public void setItems(final List<T> items) {

		setItems(items, new StringLabelProvider<T>());
	}

	public void setItems(final List<T> items, final LabelProvider<T> label) {

		clear();
		UISelectItems uiItems = SIGHComponentFactory.getNewSelectItems();
		List<SelectItem> selectItems = SelectHelper
				.getSelectItems(items, label);
		uiItems.setValue(selectItems);
		addDefault();
		getBind().getChildren().add(uiItems);

	}

	public void addDefault() {

		if (withDefault && !defaultAdded) {
			getBind().getChildren().add(getDefaultSelectItem());
			defaultAdded = true;
		}
	}

	public void clear() {

		List<UISelectItem> toRetain = ListHelper
				.getAsList(getDefaultSelectItem());
		getBind().getChildren().retainAll(toRetain);
	}

	public void addValueChangeListener(final ValueChangeListener listener) {

		getBind().addValueChangeListener(listener);
		getBind().setImmediate(true);
		ajax = new AjaxBehavior();
		ajax.setTransient(true);
		ajax.setRender(getValueChangeRender());
		getBind().addClientBehavior("valueChange", ajax);
	}

	public void setDisable(final boolean disabled) {

		getBind().setDisabled(disabled);
	}

	public List<String> getValueChangeRender() {

		return toRender;
	}

	public void clearValueChangeRender() {

		toRender.clear();
	}

	public void addValueChangeRender(final String toRender) {

		this.toRender.add(toRender);
		if (ajax != null) {
			ajax.setRender(this.toRender);
		}
	}

	/**
	 * Define donde se guardara lo que se seleccione con este componente
	 */
	public void setTarget(final ValueExpression valueExpression) {

		getBind().setValueExpression("value", valueExpression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.forms.dynamic.Field#getType()
	 */
	@Override
	public String getType() {

		return TYPE;
	}

	/**
	 * @return bind
	 */
	public HtmlSelectOneMenu getBind() {

		return bind;
	}

	/**
	 * @param bind
	 *            bind para setear
	 */
	public void setBind(final HtmlSelectOneMenu bind) {

		this.bind = bind;
	}

	/**
	 * @return changeListener
	 */
	public void changeListener(final ValueChangeEvent event) {

		System.out.println("ComboBoxField.changeListener()");
		if (changeListener != null) {
			changeListener.processValueChange(event);
		}
	}

	@Override
	public boolean enable() {

		setDisable(false);
		return true;
	}

	@Override
	public boolean disable() {

		setDisable(true);
		return true;
	}
}
