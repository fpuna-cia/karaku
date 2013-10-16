package py.una.med.base.util;

public interface LabelProvider<T> {

	String getAsString(T object);

	class StringLabelProvider<T> implements LabelProvider<T> {

		@Override
		public String getAsString(T object) {

			return object.toString();
		}
	}

}
