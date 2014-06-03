/**
 * @Column 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.reports;

import ar.com.fdvs.dj.domain.constants.HorizontalAlign;

/**
 * Clase que representa una columna.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 20/02/2013
 * 
 */
public class Column {

	/**
	 * 
	 */
	private static final int DEFAULT_WIDTH = 20;
	private String title;
	private String field;
	private String nameField;
	private final int width;
	private HorizontalAlign align;
	private String pattern;

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param width
	 *            Define el ancho de la columna.
	 * @param align
	 *            Define el formato de alineacion de la columna
	 * 
	 */
	public Column(String title, String field, int width, HorizontalAlign align) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = width;
		this.align = align;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param width
	 *            Define el ancho de la columna.
	 * @param align
	 *            Define el formato de alineacion de la columna
	 * @param pattern
	 *            Define el patron que debe seguir la cadena
	 * 
	 */
	public Column(String title, String field, int width, HorizontalAlign align,
			String pattern) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = width;
		this.align = align;
		this.pattern = pattern;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param width
	 *            Define el ancho de la columna.
	 * 
	 */
	public Column(String title, String field, int width) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = width;
		this.align = HorizontalAlign.LEFT;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param align
	 *            Define el formato de alineacion de la columna
	 * 
	 */
	public Column(String title, String field, HorizontalAlign align) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = DEFAULT_WIDTH;
		this.align = align;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param align
	 *            Define el formato de alineacion de la columna
	 * @param pattern
	 *            Define el patron que debe seguir la cadena
	 * 
	 */
	public Column(String title, String field, HorizontalAlign align,
			String pattern) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = DEFAULT_WIDTH;
		this.align = align;
		this.pattern = pattern;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * 
	 */
	public Column(String title, String field) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = DEFAULT_WIDTH;
		this.align = HorizontalAlign.LEFT;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * 
	 */
	public Column(String field) {

		super();
		this.title = field;
		this.field = field;
		this.nameField = field;
		this.width = DEFAULT_WIDTH;
		this.align = HorizontalAlign.LEFT;
	}

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param namefield
	 *            Define el nombre del field.
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param width
	 *            Define el ancho de la columna.
	 * 
	 */
	public Column(String title, String nameField, String field, int width) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = nameField;
		this.width = width;
		this.align = HorizontalAlign.LEFT;
	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public String getField() {

		return field;
	}

	public void setField(String field) {

		this.field = field;
	}

	public int getWidth() {

		return width;
	}

	public String getNameField() {

		return nameField;
	}

	public void setNameField(String nameField) {

		this.nameField = nameField;
	}

	public HorizontalAlign getAlign() {

		return align;
	}

	public void setAlign(HorizontalAlign align) {

		this.align = align;
	}

	public String getPattern() {

		return pattern;
	}

	public void setPattern(String pattern) {

		this.pattern = pattern;
	}

}
