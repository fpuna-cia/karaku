/*
 * @EntitySerializerTest.java 1.0 Jun 17, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.junit.Test;
import py.una.pol.karaku.util.EntitySerializer;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 17, 2014
 * 
 */
public class EntitySerializerTest {

	@Test
	public void testSerialize() throws Exception {

		class Test implements Serializable {

			private static final long serialVersionUID = 1L;

			public static final String URI_ID = "bla";

			@Id
			private Long id;
			private String name;
			private String bla;
			private String untouched;
			@ManyToMany
			private List<Object> many;
			@OneToMany
			private List<Object> depto;

			public Test(String name, String bla) {

				this.name = name;
				this.bla = bla;
				id = 100L;
				many = new ArrayList<Object>();
				depto = new ArrayList<Object>();
			}
		}

		assertEquals("name: bla; bla: ble",
				EntitySerializer.serialize(new Test("bla", "ble")));
	}
}
