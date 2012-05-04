/* Interface defining csv import client operations for Sensorpress.
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

public interface SpCsvImportClient {

	/**
	 * Import a file with readings into SP
	 * 
	 * @param directory
	 *            is the full directory path containing the file to import
	 * @param filename
	 *            is the name of the file to import
	 * @param deviceId
	 *            is the id of the device whose values are imported for
	 * @param horz_sp_readingset_info_id
	 *            is the readingset_info_id to insert for the records
	 * @return the query result
	 */
	public Object[] importCsv(String directory, String filename,
			Integer deviceId, Integer horz_sp_readingset_info_id);

	/**
	 * Recursively imports all pywws files in a given directory
	 * 
	 * @param directory
	 *            is the full directory path containing files to import
	 * @param deviceId
	 *            is the id of the device whose values are imported for
	 * @param horz_sp_readingset_info_id
	 *            is the readingset_info_id to insert for the records
	 * @return the query result or null if an error occurred
	 */
	public void importDirectory(String directory, Integer deviceId, 
			Integer horz_sp_readingset_info_id);

}
