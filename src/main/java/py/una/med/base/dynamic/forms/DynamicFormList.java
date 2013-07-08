/*
 * @DynamicFormList.java 1.0 Feb 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.util.I18nHelper;

/**
 * Formulario dinámico base, es un wrapper de {@link List}, para mantener una
 * lista de {@link Field}
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class DynamicFormList {

	private List<Field> fields;
	private String header;
	private List<PickerField<?>> pickerFields;
	private PickerField<?> currentPickerField;
	private boolean hasPickerFields;
	private PickerField<?> emptyPickerField;

	/**
	 * Asigna un titulo al formulario.
	 * 
	 * @param header
	 *            key del archivo de internacionalización
	 */
	public void setHeader(String header) {

		this.header = I18nHelper.getMessage(header);
	}

	/**
	 * Retorna la cadena ya internacionalizada del titulo.
	 * 
	 * @return header
	 */
	public String getHeader() {

		return header;
	}

	public DynamicFormList() {

		fields = new ArrayList<Field>();
	}

	public void enable() {

		for (Field f : getFields()) {
			f.enable();
		}
	}

	public void disable() {

		for (Field f : getFields()) {
			f.disable();
		}
	}

	public void add(Field ... field) {

		for (Field f : field) {
			fields.add(f);
		}
	}

	/**
	 * @param fields
	 *            fields para setear
	 */
	public void setFields(List<Field> fields) {

		this.fields = fields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.LinkedList#add(java.lang.Object)
	 */
	public boolean add(Field e) {

		if (e instanceof PickerField) {
			getPickerFields().add((PickerField<?>) e);
			withPickerFields();
		}
		return getFields().add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends Field> collection) {

		for (Field c : collection) {
			if (c instanceof PickerField) {
				getPickerFields().add((PickerField<?>) c);
				withPickerFields();
			}
		}

		return getFields().addAll(collection);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {

		return getFields().isEmpty();
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {

		getFields().clear();
	}

	/**
	 * Retorna la lista de {@link PickerField} que tiene este formulario, si
	 * nunca se agrego ningun {@link PickerField}, por defecto tendra uno vacio.
	 * 
	 * @return pickerFields
	 */
	private List<PickerField<?>> getPickerFields() {

		if (pickerFields == null) {
			pickerFields = new ArrayList<PickerField<?>>();
			getPickerFields().add(getEmptyPickerField());
		}
		return pickerFields;
	}

	/**
	 * Retorna el {@link PickerField} que actualmente esta disponible, el
	 * {@link PickerField} retornado por este método es el único que esta activo
	 * en un momento, y por consiguiente ningún otro funciona.
	 * 
	 * @return currentPickerField
	 */
	public PickerField<?> getCurrentPickerField() {

		if (currentPickerField == null) {
			currentPickerField = getPickerFields().get(0);
		}
		return currentPickerField;
	}

	/**
	 * Modifica el {@link #getCurrentPickerField()} para que retorne el
	 * {@link PickerField} con el id pasado como parámetro.<br />
	 * Para obtener este ID utilice {@link Field#getId()}
	 * 
	 * @param identificador
	 *            de {@link PickerField}
	 */
	public void setPicker(String id) {

		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("Id can not be null");
		}
		for (PickerField<?> pf : getPickerFields()) {
			if (id.equals(pf.getId())) {
				currentPickerField = pf;
				return;
			}
		}
		throw new KarakuRuntimeException(
				"Can not find the picker with the current id");
	}

	/**
	 * Notifica que el componente tendrá {@link PickerField}, esto es importante
	 * pues JSF no permite que se añadan formularios, entonces no se pueden
	 * agregar {@link PickerField} una vez que se dibuja por primera vez el
	 * formulario.
	 */
	public void withPickerFields() {

		this.hasPickerFields = true;
	}

	/**
	 * @param emptyPickerField
	 *            emptyPickerField para setear
	 */
	public void setEmptyPickerField(PickerField<?> emptyPickerField) {

		this.emptyPickerField = emptyPickerField;
	}

	/**
	 * @return emptyPickerField
	 */
	public PickerField<?> getEmptyPickerField() {

		if (emptyPickerField == null) {
			throw new KarakuRuntimeException("Set a emptyPickerField");
		}
		return emptyPickerField;
	}

	/**
	 * Retorna si el formulario tiene {@link PickerField}, por defecto es
	 * <code>false</code>.
	 * 
	 * @return <code>true</code> si tiene {@link PickerField} o
	 *         <code>false</code> si nunca se llamo a
	 *         {@link #withPickerFields()}
	 * @see #withPickerFields()
	 */
	public boolean isWithPickerFields() {

		return hasPickerFields;
	}

	/**
	 * Asigna como picker Field al
	 */
	public void resetPicker() {

		currentPickerField = emptyPickerField;
	}

	/**
	 * Retorna la lista de {@link Field} que componen este formulario
	 * 
	 * @return fields
	 */
	public List<Field> getFields() {

		if (fields == null) {
			fields = new ArrayList<Field>();
		}
		return fields;
	}

	public String getCurrentRender() {

		System.out.println("dynamic:form:dynamic:"
				+ getCurrentPickerField().getId());
		return "dynamic:form:dynamic:" + getCurrentPickerField().getId();
	}
}
