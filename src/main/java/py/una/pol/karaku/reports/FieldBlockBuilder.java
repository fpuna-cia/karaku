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
package py.una.pol.karaku.reports;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import py.una.pol.karaku.reports.KarakuReportBlockField.Field;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.StringUtils;

/**
 * Clase utilizada para el diseño de bloques de un determinado reporte.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 07/04/2014
 * 
 */

public final class FieldBlockBuilder {

    private String title;
    private boolean isSubBlock;
    private String nameDataSource;
    private int widthLabel;
    private int widthValue;
    private List<Field> fields;
    private boolean isTitle;

    /**
     * Construye una instancia de un builder para diseñar bloques de reporte del
     * tipo {@link KarakuReportBlockField} y {@link KarakuReportBlockGrid}
     * 
     * @param title
     *            título del bloque a construir.
     *            <p>
     *            <li>Debe ser una cadena de internacionalización.
     */
    public FieldBlockBuilder(String title) {

        this.title = getMessage(title);
        this.fields = new ArrayList<Field>();
        this.isSubBlock = false;
    }

    public FieldBlockBuilder() {

        this.fields = new ArrayList<Field>();
        this.isSubBlock = false;
    }

    /**
     * 
     * @param title
     * @param i18n
     *            true si se desea internacionalizar el título del bloque
     */
    public FieldBlockBuilder(String title, boolean i18n) {

        checkTitle(title, i18n);
        this.fields = new ArrayList<Field>();
        this.isSubBlock = false;
    }

    public FieldBlockBuilder(String title, boolean i18n, boolean isSubBlock) {

        checkTitle(title, i18n);
        this.fields = new ArrayList<Field>();
        this.isSubBlock = isSubBlock;
    }

    private void checkTitle(String title, boolean i18n) {

        if (i18n) {
            this.title = getMessage(title);

        } else {
            this.title = title;

        }
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getNameDataSource() {

        return nameDataSource;
    }

    /**
     * Configura el identificador del dataSource utilizado en el bloque.
     * 
     * <li>El identificador debe ser único.
     * 
     * @param nameDataSource
     *            identificador del {@link JRDataSource}
     * @return
     */
    protected FieldBlockBuilder setNameDataSource(String nameDataSource) {

        this.nameDataSource = nameDataSource;
        return this;
    }

    public int getWidthLabel() {

        return widthLabel;
    }

    /**
     * Configura el ancho del label de un bloque del tipo
     * {@link KarakuReportBlockField}.
     * 
     * @param widthLabel
     *            ancho de la columna que representa el label del bloque
     *            (columna izquierda.)
     * @return
     */
    public FieldBlockBuilder setWidthLabel(int widthLabel) {

        this.widthLabel = widthLabel;
        return this;
    }

    public int getWidthValue() {

        return widthValue;
    }

    /**
     * Configura el ancho del valor de un bloque del tipo
     * {@link KarakuReportBlockField}.
     * 
     * @param widthValue
     *            ancho de la columna que representa el valor del bloque
     *            (columna derecha.)
     * @return
     */
    public FieldBlockBuilder setWidthValue(int widthValue) {

        this.widthValue = widthValue;
        return this;
    }

    /**
     * Fields utilizados para representar un bloque del tipo
     * {@link KarakuReportBlockField}
     * 
     * @param fields
     *            atributos a mostrar
     */
    public void setFields(List<Field> fields) {

        this.fields = notNull(fields,
                "No se puede crear un block con fields nulos");
    }

    public List<Field> getFields() {

        return fields;
    }

    public FieldBlockBuilder addField(String label, String value) {

        this.fields.add(new Field(getLabelFied(label), value));
        return this;
    }

    public FieldBlockBuilder addEmptyField() {

        this.fields.add(new Field("", ""));
        return this;
    }

    public FieldBlockBuilder addField(String label, boolean i18n, String value) {

        if (i18n) {
            return addField(label, value);
        } else {
            this.fields
                    .add(new Field(StringUtils.join(": ", label, ""), value));
            return this;
        }
    }

    /**
     * Internacionaliza el valor del label y le concatena dos puntos.
     * <p>
     * <b>Ejemplo: </b> key ="NOMBRE_USUARIO"
     * <li>retornará "Nombre:"
     * <li>suponiendo que la internacionalización de la cadena "NOMBRE_USUARIO"
     * es "Nombre".
     * 
     * @param key
     *            Cadena de internacionalización.
     * @return
     */
    private static String getLabelFied(String key) {

        return StringUtils.join(": ", getMessage(key), "");
    }

    /**
     * Internacionaliza la cadena recibida como parámetro.
     * 
     * @param key
     *            cadena de internacionalización.
     * @return
     */
    private static String getMessage(String key) {

        return I18nHelper.getSingleton().getString(key);
    }

    /**
     * Contruye un bloque {@link KarakuReportBlockField}
     * 
     * <p>
     * Ejemplo del bloque:
     * </p>
     * 
     * <b>Datos generales</b>
     * 
     * <p>
     * Nombre: Matias Gabriel
     * </p>
     * <p>
     * Apellido: Ozuna Cantero
     * </p>
     * Profesion: Ingeniero Electromecánico.
     * 
     * @return bloque configurado correctamente.
     */
    public KarakuReportBlockField build() {

        return new KarakuReportBlockField(getTitle(), getNameDataSource(),
                fields, getWidthLabel(), getWidthValue(), isSubBlock);

    }

    public KarakuReportBlockField buildTitle() {

        return new KarakuReportBlockField(getTitle(), getNameDataSource(),
                fields, getWidthLabel(), getWidthValue(), isSubBlock, isTitle);

    }

    public boolean isSubBlock() {

        return isSubBlock;
    }

    public void setSubBlock(boolean isSubBlock) {

        this.isSubBlock = isSubBlock;
    }

    /**
     * @return isTitle
     */
    public boolean isTitle() {

        return isTitle;
    }

    /**
     * @param isTitle
     *            isTitle para setear
     */
    public void setTitle(boolean isTitle) {

        this.isTitle = isTitle;
    }

}
