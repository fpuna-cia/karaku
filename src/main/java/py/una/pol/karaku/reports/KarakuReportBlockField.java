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

import java.util.List;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * Esta clase es utilizada para representar los bloques de un reporte, los
 * cuales estan conformados de una lista de labels. Hay que tener en cuenta que
 * cada bloque definido dentro del reporte debe tener un nameDataSource unico,
 * de manera a poder incorporar el dataSource correspondiente dentro de la lista
 * de parametros.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 27/01/2014
 * 
 */
public final class KarakuReportBlockField extends KarakuReportBlock {

    private int widthLabel;
    private int widthValue;
    private boolean isSubBlock;
    private boolean isTitle;

    public KarakuReportBlockField(String title, String nameDataSource,
            List<Field> fields, int widthLabel, int widthValue,
            boolean isSubBlock) {

        super(title, nameDataSource);
        setDataSource(buildDataSource(fields));
        this.widthLabel = widthLabel;
        this.widthValue = widthValue;
        this.isSubBlock = isSubBlock;

    }

    public KarakuReportBlockField(String title, String nameDataSource,
            List<Field> fields, int widthLabel, int widthValue) {

        super(title, nameDataSource);
        setDataSource(buildDataSource(fields));
        this.widthLabel = widthLabel;
        this.widthValue = widthValue;
        this.isSubBlock = false;

    }

    public KarakuReportBlockField(String title, String nameDataSource,
            List<Field> fields, int widthLabel, int widthValue,
            boolean isSubBlock, boolean isTitle) {

        super(title, nameDataSource);
        setDataSource(buildDataSource(fields));
        this.widthLabel = widthLabel;
        this.widthValue = widthValue;
        this.isSubBlock = isSubBlock;
        this.isTitle = isTitle;

    }

    public JRDataSource buildDataSource(List<Field> fields) {

        DRDataSource ds = new DRDataSource("label", "value");

        for (Field field : fields) {
            ds.add(field.getLabel(), field.getValue());
        }
        return ds;
    }

    public int getWidthValue() {

        return widthValue;
    }

    public void setWidthValue(int widthValue) {

        this.widthValue = widthValue;
    }

    public int getWidthLabel() {

        return widthLabel;
    }

    public void setWidthLabel(int widthLabel) {

        this.widthLabel = widthLabel;
    }

    public static class Field {

        private String label;
        private String value;

        public Field(String label, String value) {

            super();
            this.label = label;
            this.value = value;
        }

        public String getValue() {

            return value;
        }

        public void setValue(String value) {

            this.value = value;
        }

        public String getLabel() {

            return label;
        }

        public void setLabel(String label) {

            this.label = label;
        }

    }

    public boolean isSubBlock() {

        return isSubBlock;
    }

    public void setSubBlock(boolean isSubBlock) {

        this.isSubBlock = isSubBlock;
    }

    public boolean isTitle() {

        return isTitle;
    }

    public void setTitle(boolean isTitle) {

        this.isTitle = isTitle;
    }

}
