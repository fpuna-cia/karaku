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
package py.una.pol.karaku.replication.client;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 29, 2013
 * 
 */
public final class ReplicationContextHolder {

	private static final ThreadLocal<ReplicationContext> HOLDER = new ThreadLocal<ReplicationContext>();

	public static ReplicationContext getContext() {

		return HOLDER.get();

	}

	static void setContext(ReplicationContext rc) {

		HOLDER.set(rc);
	}

	private ReplicationContextHolder() {

	}

	public static final class ReplicationContext {

		private String replicationUser;
		private String currentClassName;

		/**
		 * @return currentClassName
		 */
		public String getCurrentClassName() {

			return currentClassName;
		}

		/**
		 * @return replicationUser
		 */
		public String getReplicationUser() {

			return replicationUser;
		}

		/**
		 * @param currentClassName
		 *            currentClassName para setear
		 */
		public void setCurrentClassName(String currentClassName) {

			this.currentClassName = currentClassName;
		}

		/**
		 * @param replicationUser
		 *            replicationUser para setear
		 */
		public void setReplicationUser(String replicationUser) {

			this.replicationUser = replicationUser;
		}
	}

}
