/* Interface defining insertion client operations Shadowpress.
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

package uk.ac.nottingham.horizon.jmb.shadowpress.client.v2;

public interface SpInsertionClient extends SpBaseClient {
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
	 * Inserts multiple records into SP readingset 
	 * @param records
	 * @return the insertion result
	 */
	public Object[] insertRecordsIntoReadingsets(Object[] records);
	/**
	 * insert readingsets from url1 that are more recent than in url2
	 * @param url1 is a url to select readingsets from
	 * @param user1 is the user for url1
	 * @param pwrd1 is the password for url1
	 * @param url2 is a url to check readingsets against
	 * @param user1 is the user for url2
	 * @param pwrd1 is the password for url2
	 */
	public void insertRecentReadings(String url1, String user1,
			String pwrd1, String url2, String user2, String pwrd2);

}
