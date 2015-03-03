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
package py.una.pol.karaku.survey.components;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;

/**
 * Esta clase representa un Field, o mejor dicho un InputText con validaciones
 * de required y size MAX.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 05/06/2013
 * 
 * @author Diego Ramírez
 * @since 1.1
 * @version 1.1 Feb 24,2015
 * 
 */
public final class DynamicSurveyField {

    private DynamicSurveyField() {

    }

    public interface SurveyField {

        String getId();

        void setId(String id);

        String getValue();

        void setValue(String value);

        int getIndex();

        void setIndex(int index);

        int getMax();
    }

    public abstract static class Requirement implements SurveyField {

        protected int index;
        protected String id;
        protected final int max;
        protected boolean validate;
        protected static final String CELL = "_cell_";

        public Requirement(int index, int max) {

            super();
            this.max = max;
            this.index = index;
            this.id = this.CELL;
        }

        /**
         * Esto se hace para que se pueda validar, no necesariamente la
         * propiedad {@link #validate} es un campo real.
         * 
         * @return
         */
        @AssertTrue(message = "Se ha excedido la longitud maxima")
        public boolean isValidate() {

            validate = getValue() == null || getValue().length() <= max;
            return validate;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #getId()
         */
        @Override
        public String getId() {

            return this.id;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #setId(java.lang.String)
         */
        @Override
        public void setId(String id) {

            this.id = id;

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #getValue()
         */
        @Override
        public abstract String getValue();

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #setValue(java.lang.String)
         */
        @Override
        public abstract void setValue(String value);

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #getIndex()
         */
        @Override
        public int getIndex() {

            return this.index;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #setIndex(int)
         */
        @Override
        public void setIndex(int index) {

            this.index = index;

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField
         * #getMax()
         */
        @Override
        public int getMax() {

            return this.max;
        }
    }

    public static class NotRequired extends Requirement implements SurveyField {

        private String value = "";

        public NotRequired(int index, int max) {

            super(index, max);
        }

        @Override
        public void setValue(String value) {

            this.value = value;
        }

        @Override
        public String getValue() {

            return value;
        }

    }

    public static class Required extends Requirement implements SurveyField {

        @NotNull
        private String value = "";

        public Required(int index, int max) {

            super(index, max);
        }

        @Override
        public String getValue() {

            return value;
        }

        @Override
        public void setValue(String value) {

            this.value = value;
        }

    }

    /**
     * Construye un Field a ser utilizado dentro del formulario
     * 
     * @param columna
     *            Field a representar
     * @return Field requerido o no de acuerdo a si es obligatoria o no.
     */
    public static SurveyField fieldFactory(EncuestaPlantillaPregunta columna) {

        if ("SI".equals(columna.getObligatoria())) {
            return new DynamicSurveyField.Required(columna.getOrden(),
                    columna.getLongitudRespuesta());

        } else {
            return new DynamicSurveyField.NotRequired(columna.getOrden(),
                    columna.getLongitudRespuesta());
        }
    }
}
