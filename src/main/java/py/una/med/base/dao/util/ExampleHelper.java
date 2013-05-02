package py.una.med.base.dao.util;

import java.lang.reflect.Field;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public final class ExampleHelper {

	private ExampleHelper() {

		// No-op
	}

	public static <T> void addRelations(T example, Criteria criteria) {

		if (example == null) {
			return;
		}
		try {
			for (Field f : example.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.get(example) == null) {
					continue;
				}
				if (f.isAnnotationPresent(ManyToOne.class)
						|| f.isAnnotationPresent(OneToOne.class)) {
					criteria.add(Restrictions.eq(Util.getColumnName(f),
							f.get(example)));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());

		}
	}

}
