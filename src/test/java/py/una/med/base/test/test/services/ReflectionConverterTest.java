/*
 * @ReflexionConverterTest.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.math.Quantity;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.Converter;
import py.una.med.base.services.util.ReflectionConverter;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.util.ListHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReflectionConverterTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Test
	public void testReflexionFromEntityToDTO1Level() throws Exception {

		String string = "String1";
		Calendar cal = Calendar.getInstance();
		cal.clear();
		Date date = cal.getTime();

		Quantity quantity = Quantity.ZERO.times(100);

		EntityChild ec = new EntityChild();
		ec.string = "ec1";
		Entity e = new Entity();
		e.uri = string;
		e.date = date;
		e.quantity = quantity;
		e.codigoInterno = "NOOO";

		e.child = ec;

		DTO dto = make(Entity.class, DTO.class).toDTO(e, 0);

		assertEquals(string, dto.uri);
		assertEquals(date, dto.date);
		assertEquals(quantity, dto.quantity);
		assertFalse(dto.active);
		assertEquals("ec1", dto.child.string);

	}

	@Test
	public void testReflexionFromEntityToDTO2Level() throws Exception {

		EntityChild ee1 = new EntityChild();
		ee1.string = "EE1";

		EntityChild ee2 = new EntityChild();
		ee2.string = "EE2";

		Entity e = new Entity();
		e.childs = ListHelper.getAsList(ee1, ee2);

		DTO dto = make(Entity.class, DTO.class).toDTO(e, 2);

		List<DTOChild> childs = dto.childs;
		assertNotNull(childs);
		assertEquals(2, childs.size());

		assertTrue(childs.get(0) instanceof DTOChild);
		assertTrue(childs.get(1) instanceof DTOChild);
		DTOChild first = childs.get(0);
		DTOChild second = childs.get(1);

		assertEquals(ee1.string, first.string);
		assertEquals(ee2.string, second.string);
	}

	@Test
	public void testReflexionFromDTOToEntity() throws Exception {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		Date date = cal.getTime();

		DTO dto = new DTO();
		dto.active = true;
		dto.date = date;
		dto.quantity = Quantity.ONE;
		dto.uri = "URI";

		Entity e = make(Entity.class, DTO.class).toEntity(dto);

		assertTrue(e.isActive());
		assertEquals(date, e.date);
		assertEquals(Quantity.ONE, e.quantity);
		assertEquals("URI", e.uri);

		DTOChild first = new DTOChild();
		first.string = "FIRST";
		DTOChild second = new DTOChild();
		second.string = "SECOND";

		dto.childs = ListHelper.getAsList(first, second);

		e = make(Entity.class, DTO.class).toEntity(dto);

		List<EntityChild> childs = e.childs;
		assertNotNull(childs);
		assertEquals(2, childs.size());

		assertTrue(childs.get(0) instanceof EntityChild);
		assertTrue(childs.get(1) instanceof EntityChild);
		EntityChild ee1 = childs.get(0);
		EntityChild ee2 = childs.get(1);

		assertEquals(first.string, ee1.string);
		assertEquals(second.string, ee2.string);

	}

	private <E extends Shareable, T extends py.una.med.base.replication.DTO> Converter<E, T> make(
			Class<E> eClass, Class<T> eDTO) {

		return new ReflectionConverter<E, T>(eDTO, eClass) {

			@Override
			public <Y extends Shareable, O extends py.una.med.base.replication.DTO> Converter<Y, O> getConverter(
					Class<Y> entityClass, Class<O> dtoClass) {

				return make(entityClass, dtoClass);
			}
		};
	}

	public static class Entity implements Shareable {

		private Quantity quantity;
		private Date date;
		private boolean active;
		private String uri;
		@SuppressWarnings("unused")
		private String codigoInterno;
		List<EntityChild> childs;

		EntityChild child;

		@Override
		public String getUri() {

			return uri;
		}

		@Override
		public void inactivate() {

			active = false;
		}

		@Override
		public void activate() {

			active = true;
		}

		@Override
		public boolean isActive() {

			return active;
		}

	}

	public static class EntityChild implements Shareable {

		String string;

		@Override
		public String getUri() {

			return null;
		}

		@Override
		public void inactivate() {

		}

		@Override
		public void activate() {

		}

		@Override
		public boolean isActive() {

			return false;
		}
	}

	public static class DTO implements py.una.med.base.replication.DTO {

		private Quantity quantity;
		private Date date;
		private boolean active;
		private String uri;
		List<DTOChild> childs;

		DTOChild child;

		@Override
		public String getUri() {

			return uri;
		}

		@Override
		public boolean isActive() {

			return active;
		}
	}

	public static class DTOChild implements py.una.med.base.replication.DTO {

		String string;

		@Override
		public String getUri() {

			return null;
		}

		@Override
		public boolean isActive() {

			return false;
		}
	}
}
