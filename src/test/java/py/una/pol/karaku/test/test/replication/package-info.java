/*
 * @package-info.java 1.0 Nov 4, 2013 Sistema Integral de Gestion Hospitalaria
 */
/**
 * Test para los servicios de replicación.
 *
 * La organización es como sigue:
 *
 * <p>
 * <ol>
 * <li>Layers: se encuentran los objetos no mockeados que sera utilizados para
 * los test, como una entidad, dao y lógica.</li>
 * <li>Watchers: test que se encargan de detectar cambios y almacenar la
 * información necesaria para que los proveedores puedan generar informacion.</li>
 * <li>Provider: se encuentran los test que se encargan de proveer datos</li>
 * <li>Consumer: se encuentran los test que consumen y se encargan de actualizar
 * tablas locales.</li>
 *
 * </ol>
 * </p>
 *
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 *
 */
package py.una.pol.karaku.test.test.replication;