/*
 * @DatabaseHelper.java 1.0 Nov 1, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util;

import org.hibernate.SQLQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import py.una.med.base.util.StringUtils;

/**
 * Define un conjunto de operaciones que pueden ejecutar los test, o son de uso
 * común dentro de karaku.
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 1, 2013
 *
 */
public class DatabaseUtils {

	/**
	 * Ejecuta el DDL pasado, una vez por valor en el parámetro params.
	 *
	 * <p>
	 * Ejemplo de uso: Supongamos que deseamos crear una lista de secuencias,
	 * entonces utilizamos el siguiente código:
	 *
	 * <pre>
	 * String[] secuencias = new String[] { &quot;seq1&quot;, &quot;seq2&quot;, &quot;seq3&quot; };
	 * DatabaseUtils.executeDDL(&quot;Create sequence %d&quot;, context, secuencias);
	 * </pre>
	 *
	 * Lo mismo ejecuta los siguientes comandos:
	 *
	 * <pre>
	 * Create sequence seq1;
	 * Create sequence seq2;
	 * Create sequence seq3;
	 * </pre>
	 *
	 * <h1>No se debe ejecutar este método dentro de una transacción, pues el
	 * mismo la comitea</h1>
	 *
	 * @param testContext
	 * @param sequences
	 */
	public static void executeDDL(final String ddl,
			ApplicationContext testContext, final String[] params) {

		final HibernateTransactionManager tm = getTransactionManager(testContext);
		final TransactionTemplate tt = new TransactionTemplate(tm);
		tt.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				for (String s : params) {
					if (StringUtils.isInvalid(s)) {
						continue;
					}

					SQLQuery sql = tm.getSessionFactory().getCurrentSession()
							.createSQLQuery(String.format(ddl, s));
					sql.executeUpdate();
				}

			}
		});
	}

	private static HibernateTransactionManager getTransactionManager(
			ApplicationContext context) {

		return context.getBean(HibernateTransactionManager.class);
	}
}
