/* Interface defining csv import client operations for Shadowpress.
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

package uk.ac.nottingham.horizon.jmb.shadowpress.client;


public interface SpCsvImportClient {

	/**
	 * Import a file with readings into SP 
	 * @param directory is the full directory path containing the file to import
	 * @param filename is the name of the file to import
	 * @param deviceId is the id of the device whose values are imported for
	 * @return the query result
	 */
	public Object[] importCsv(String directory, 
			String filename, Integer deviceId);
	
}
