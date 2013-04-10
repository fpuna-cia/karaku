/**
 * @Column 1.0 20/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.reports;

/**
 * Clase que representa una columna.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 20/02/2013
 * 
 */
public class Column {

	private String title;
	private String field;
	private String nameField;
	private final int width;

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param title
	 *            Titulo de la columna
	 * @param field
	 *            Valor que representa los datos dentro de la columna.
	 * @param width
	 *            Define el ancho de la columna.
	 */
	public Column(String title, String field, int width) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = width;
	}

	public Column(String title, String field) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = field;
		this.width = 20;
	}

	public Column(String field) {

		super();
		this.title = field;
		this.field = field;
		this.nameField = field;
		this.width = 20;
	}

	public Column(String title, String nameField, String field, int width) {

		super();
		this.title = title;
		this.field = field;
		this.nameField = nameField;
		this.width = width;
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

}
