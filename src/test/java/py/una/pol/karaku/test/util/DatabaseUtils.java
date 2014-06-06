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
package py.una.pol.karaku.test.util;

import org.hibernate.SQLQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import py.una.pol.karaku.util.StringUtils;

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
