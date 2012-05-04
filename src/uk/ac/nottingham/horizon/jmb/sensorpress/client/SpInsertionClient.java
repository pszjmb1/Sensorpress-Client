/* Interface defining insertion client operations Sensorpress.
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

public interface SpInsertionClient{
	/**
	 * Routine to insert readings into a DB via XML-RPC
	 * @param type is the data_type to use (such as dec_4_1)
	 * @param value is reading value
	 * @param readingset_id is id of the reading set that the reading belongs to
	 * @param reading_type is the id of the type of reading
	 * @return an int with the new reading's ID or null or an error if insert was unsuccessful
	 */
	public Object[] insertReading(String type, String value, 
			Integer readingset_id, Integer reading_type);
	/**
	 * Routine to insert import records into a DB via XML-RPC
	 * @param type is the data_type to use (such as dec_4_1)
	 * @param filname is the file that was imported
	 * @param deviceInstanceId is the device instance that the data was captured from
	 * @param timestamp is the last timestamp value from the imported file
	 * @return the query result
	 */
	public Object[] insertImportRecord(String filname, Integer deviceInstanceId, 
			String timestamp);
	/**
	 * Inserts multiple records into SP readingset 
	 * @param records are the values to insert
	 * @return the insertion result
	 */
	public Object[] insertRecordsIntoReadingsets(Object[] records);
	/**
	 * insert readingsets from url1 that are more recent than in url2
	 * @param client1 is used to select readingsets from
	 * @param client2 is used to check readingsets against
	 * @return the insertion result
	 */
	public Object[] insertRecentReadings(SpBaseClient client1, SpBaseClient client2);

}
