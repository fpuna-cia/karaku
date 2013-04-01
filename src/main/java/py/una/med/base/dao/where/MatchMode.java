package py.una.med.base.dao.where;

public enum MatchMode {

	EQUAL {
		public String toString(String value) {
			return value;
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {
			return org.hibernate.criterion.MatchMode.EXACT;
		}
	},

	BEGIN {
		public String toString(String value) {
			return value + '%';
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {
			return org.hibernate.criterion.MatchMode.START;
		}
	},

	END {
		public String toString(String value) {
			return '%' + value;
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {
			return org.hibernate.criterion.MatchMode.END;
		}
	},

	CONTAIN {
		public String toString(String value) {
			return '%' + value + '%';
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {
			return org.hibernate.criterion.MatchMode.ANYWHERE;
		}
	};

	public abstract String toString(String pattern);

	public abstract org.hibernate.criterion.MatchMode getMatchMode();

}