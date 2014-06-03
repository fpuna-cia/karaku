/*
 * @Clause.java 1.0 Sep 20, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;

/**
 * Interfaz base para todas las cláusulas soportadas por Karaku, una cláusula
 * puede ser vista como una condición SQL.
 * <p>
 * Si bien, la mayoría de las {@link Clause} se pueden instanciar normalmente,
 * existe una factoría para las mismas que facilita el trabajo, la misma es
 * {@link Clauses}
 * </p>
 * <p>
 * Esta interfaz es una copia de {@link Criterion} de hibernate, a fin de
 * mantener la independencia de la base de datos.
 * </p>
 * <h3>
 * Proceso de desarrollo de nuevas {@link Clause}</h3>
 * <ol>
 * <li>Crear una clase que implemente {@link Clause}, el método
 * {@link #getCriterion()} puede retornar <code>null</code>.</li>
 * <li>Crear un {@link BaseClauseHelper} con la anotación {@link Component},
 * para que sea automáticamente detectado. En la clase se debe implementar el
 * método abstracto
 * {@link BaseClauseHelper#getCriterion(org.hibernate.Criteria, Clause, java.util.Map)}
 * . Los métodos auxiliares
 * {@link BaseClauseHelper#configureAlias(org.hibernate.Criteria, String, java.util.Map)}
 * ayudan a generar automáticamente una cadena de alias y joins para búsquedas
 * anidadas. Generalmente una vez construido el Alias se utiliza la factoría
 * {@link Restrictions} para obtener una cláusula para la sesión.</li>
 * </ol>
 * 
 * 
 * <h3>Funcionamiento</h3>
 * <p>
 * Existe un componente denominado {@link RestrictionHelper} que se encarga de
 * buscar todos los {@link Component} que hereden de {@link BaseClauseHelper}, y
 * los indexa. Luego cada vez que es necesario procesar una {@link Clause} busca
 * su correspondiente helper y invoca al método
 * {@link BaseClauseHelper#getCriterion(org.hibernate.Criteria, Clause, java.util.Map)}
 * , y agrega al {@link Criterion}, se puede notar que la clase
 * {@link RestrictionHelper} puede ser inyectada dentro de cada
 * {@link BaseClauseHelper} para realizar conversiones complejas o recursivas,
 * como es el caso de la clausula {@link And} y {@link Or}.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 23, 2013
 * 
 */
public interface Clause {

	/**
	 * Opción por defecto en caso de no encontrarse un helper.
	 * 
	 * @return criterion para agregar a la consulta
	 * @deprecated No es necesario implementar esto
	 */
	@Deprecated
	Criterion getCriterion();

}
