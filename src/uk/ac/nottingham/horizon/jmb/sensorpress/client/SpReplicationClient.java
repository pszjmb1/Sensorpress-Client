/* Interface defining replication client operations for Sensorpress.
 Copyright (C) 2012  Jesse Blum (JMB)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package uk.ac.nottingham.horizon.jmb.sensorpress.client;

public interface SpReplicationClient {
	
	/**
	 * Replicates one shadowpress instance (client1) into another one (client2)
	 * @param client1 contains the data to replicate
	 * @param client2 receives new data
	 */
	public void replicate(SpBaseClient client1, SpBaseClient client2);
}
