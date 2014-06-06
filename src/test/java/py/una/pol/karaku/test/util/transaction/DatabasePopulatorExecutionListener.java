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
package py.una.pol.karaku.test.util.transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.SQLQuery;
import org.hibernate.tool.hbm2ddl.SingleLineSqlCommandExtractor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.test.util.DatabaseUtils;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.util.StringUtils;

/**
 * {@link AbstractTestExecutionListener} que se encarga de crear datos de prueba
 * al inicio de la ejecución de cualquier método.
 *
 * <p>
 * Este {@link AbstractTestExecutionListener} se encarga de procesar las
 * anotaciones {@link SQLFiles} y {@link Sequences}
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 11, 2013
 *
 */
public class DatabasePopulatorExecutionListener extends
		AbstractTestExecutionListener {

	private String SEQUENCE_CONSTRUCTOR = "Create SEQUENCE %s;";
	private String SEQUENCE_DESTRUCTOR = "Drop SEQUENCE %s;";

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {

		super.afterTestClass(testContext);

		final Sequences sequences = AnnotationUtils.findAnnotation(
				testContext.getTestClass(), Sequences.class);

		if ((sequences != null) && (sequences.value() != null)
				&& (sequences.value().length > 0)) {
			DatabaseUtils.executeDDL(SEQUENCE_DESTRUCTOR,
					getApplicationContext(testContext), sequences.value());
		}
	}

	/**
	 * Este método carga los archivos SQL tanto de la clase como del método.
	 * <p>
	 * TODO ver la posibilidad de mover los archivos de clase a
	 * {@link #beforeTestClass(TestContext)} y eliminarlos después en
	 * {@link #afterTestClass(TestContext)}
	 * </p>
	 * {@inheritDoc}
	 *
	 * @param testContext
	 *            contexto del test.
	 * @throws Exception
	 *             si no se encuentra el archivo se lanza una
	 *             {@link IOException}
	 */
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {

		super.beforeTestMethod(testContext);

		SQLFiles sqlClassFiles = AnnotationUtils.findAnnotation(
				testContext.getTestClass(), SQLFiles.class);
		executeSQLFiles(testContext, sqlClassFiles);

		SQLFiles sqlFiles = AnnotationUtils.findAnnotation(
				testContext.getTestMethod(), SQLFiles.class);
		executeSQLFiles(testContext, sqlFiles);
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {

		super.beforeTestClass(testContext);

		final Sequences sequences = AnnotationUtils.findAnnotation(
				testContext.getTestClass(), Sequences.class);

		if ((sequences != null) && (sequences.value() != null)
				&& (sequences.value().length > 0)) {
			DatabaseUtils.executeDDL(SEQUENCE_CONSTRUCTOR,
					getApplicationContext(testContext), sequences.value());
		}
	}

	/**
	 * @param testContext
	 * @param sqlFiles
	 */
	private void executeSQLFiles(TestContext testContext, SQLFiles sqlFiles) {

		if (sqlFiles == null) {
			return;
		}
		List<String> files = getFiles(testContext, sqlFiles);
		SingleLineSqlCommandExtractor slsce = new SingleLineSqlCommandExtractor();

		for (String file : files) {
			executeSQL(testContext, file, slsce);
		}
	}

	/**
	 * @param testContext
	 * @param file
	 * @param slsce
	 */
	private void executeSQL(TestContext testContext, String file,
			SingleLineSqlCommandExtractor slsce) {

		BufferedReader br;
		ClassPathResource cpr = getClassPathResource(file, testContext);
		br = _getBr(cpr);
		// File f = new File(scriptPath);
		// FileReader fr = new FileReader(file);

		final String[] commands = slsce.extractCommands(br);
		if (commands == null) {
			return;
		}
		final HibernateTransactionManager tm = getTransactionManager(testContext);
		final TransactionTemplate tt = new TransactionTemplate(tm);
		tt.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {

				for (String s : commands) {
					if (StringUtils.isInvalid(s)) {
						continue;
					}

					SQLQuery sql = tm.getSessionFactory().getCurrentSession()
							.createSQLQuery(s.trim());
					sql.executeUpdate();
				}

			}
		});

	}

	/**
	 * @param file
	 * @return
	 */
	private ClassPathResource getClassPathResource(String file,
			TestContext context) {

		return TestUtils.getSiblingResource(context.getTestClass(), file);
	}

	/**
	 * @param cpr
	 * @param br
	 * @return
	 */
	private BufferedReader _getBr(ClassPathResource cpr) {

		try {
			return new BufferedReader(new InputStreamReader(
					cpr.getInputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new KarakuRuntimeException(e);
		} catch (IOException e) {
			throw new KarakuRuntimeException("Can not load the file "
					+ cpr.getFilename(), e);
		}
	}

	private ApplicationContext getApplicationContext(TestContext tc) {

		return tc.getApplicationContext();
	}

	/**
	 * @param testContext
	 * @param sqlFiles
	 * @return
	 */
	private List<String> getFiles(TestContext testContext, SQLFiles sqlFiles) {

		List<String> files = new ArrayList<String>(sqlFiles.value().length);
		for (String file : sqlFiles.value()) {
			String path = file.trim();
			if (path.equals(SQLFiles.NONE)) {
				continue;
			}
			if (path.equals(SQLFiles.DEFAULT) || "".equals(path)) {
				path = getSQLFile(testContext.getTestClass().getName());
			}
			if (files.contains(file)) {
				throw new IllegalArgumentException(
						"The file "
								+ path
								+ ", is already defined in the anotation, in the form of:"
								+ file);
			}
			if (!path.endsWith(".sql")) {
				path += ".sql";
			}
			files.add(path);
		}

		return files;
	}

	private HibernateTransactionManager getTransactionManager(
			TestContext context) {

		return context.getApplicationContext().getBean(
				HibernateTransactionManager.class);
	}

	/**
	 * Retorna el nombre del archivo SQL base que se ejecuta antes de cualquier
	 * test que herede de esta clase.
	 * <p>
	 * Implementación por defecto retorna un archivo que esta en el mismo lugar
	 * que el archivo .java.
	 * </p>
	 *
	 * @param path
	 *            path del archivo. Es una cadena separada por puntos ( en vez
	 *            de /) que no tiene una extensión.
	 * @return Archivo válido del classpath
	 */
	public String getSQLFile(@NotNull String path) {

		String scriptPath = path.replaceAll("\\.", "/");
		scriptPath = scriptPath.replaceAll("\\$.*", "");
		scriptPath += ".sql";
		return scriptPath;
	}
}
