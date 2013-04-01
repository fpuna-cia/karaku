package py.una.med.base.util;


public interface LabelProvider<T> {

	public String getAsString(T object);

	public static class StringLabelProvider<T> implements LabelProvider<T> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see py.una.med.base.util.LabelProvider#getAsString(java.lang.Object)
		 */
		@Override
		public String getAsString(T object) {

			return object.toString();
		}
	}

}
