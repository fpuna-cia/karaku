/*
 * @DynamicSurveyField.java 1.0 05/06/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.components;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import py.una.med.base.survey.domain.EncuestaPlantillaPregunta;

/**
 * Esta clase representa un Field, o mejor dicho un InputText con validaciones
 * de required y size MAX.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 05/06/2013
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

	public static class NotRequired implements SurveyField {

		private String value = "";
		private int index;
		private String id;

		private final int max;
		private boolean validate;

		/**
		 * Esto se hace para que se pueda validar, no necesariamente la
		 * propiedad {@link #validate} es un campo real.
		 * 
		 * @return
		 */
		@AssertTrue(message = "Se ha exedido la longitud maxima")
		public boolean isValidate() {

			validate = value.length() <= max;
			return validate;
		}

		public NotRequired(int index, int max) {

			super();
			this.max = max;
			this.index = index;
			this.id = "_cell_" + index;
		}

		@Override
		public String getValue() {

			return value;
		}

		@Override
		public void setValue(String value) {

			this.value = value;
		}

		@Override
		public int getIndex() {

			return this.index;
		}

		@Override
		public void setIndex(int index) {

			this.index = index;
		}

		@Override
		public String getId() {

			return this.id;
		}

		@Override
		public void setId(String id) {

			this.id = id;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see py.una.med.base.survey.DynamicSurveyField.SurveyField#getMax()
		 */
		@Override
		public int getMax() {

			return this.max;

		}

	}

	public static class Required implements SurveyField {

		@NotNull
		private String value = "";
		private int index;
		private String id;

		private final int max;
		private boolean validate;

		@AssertTrue(message = "Se ha exedido la longitud maxima")
		public boolean isValidate() {

			validate = value.length() <= max;
			return validate;
		}

		public Required(int index, int max) {

			super();
			this.index = index;
			this.max = max;
			this.id = "_cell_" + index;
		}

		@Override
		public String getValue() {

			return value;
		}

		@Override
		public void setValue(String value) {

			this.value = value;
		}

		@Override
		public int getIndex() {

			return this.index;
		}

		@Override
		public void setIndex(int index) {

			this.index = index;
		}

		@Override
		public String getId() {

			return this.id;
		}

		@Override
		public void setId(String id) {

			this.id = id;

		}

		@Override
		public int getMax() {

			return this.max;

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
