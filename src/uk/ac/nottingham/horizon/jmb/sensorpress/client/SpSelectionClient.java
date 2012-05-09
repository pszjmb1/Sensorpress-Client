/* Interface defining selection client operations Sensorpress.
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

public interface SpSelectionClient {
	/**
	 * Routine to call select statements by XML-RPC
	 * @param type is the type of table to select from
	 * @param limit is the numebr of records to return
	 * @return the resultant rowset
	 */
	public Object[] select(String type, Integer limit);
	/**
	 * Retrieves recent blog posts. 
	 * @return the resultant rowset with the recent blog posts
	 */
	public Object[] selectRecentBlogPosts();
	/**
	 * Routine to show Sensorpress tables from XML-RPC
	 * @return the resultant rowset
	 */
	public Object[] tablesFromXMLRPC();
	/**
	 * Routine to show Sensorpress table columns from XML-RPC
	 * @param table is the table to display the columns for
	 * @return the resultant rowset
	 */
	public Object[] columnsFromXMLRPC(String table);	
	/**
	 * Select readingsets from url1 that are more recent than those in url2 
	 * @param client1 connects to the ur1 to select readings from
	 * @param client2 connects to the url to check readingsets against
	 * @return the more recent readingsets
	 */
	public Object[] intersectRecentReadingsets(SpBaseClient client1, 
			SpBaseClient client2);
	/**
	 *  Select reading.id for earliest selected url1.readingset
	 * @return String for the readingid
	 */
	public String selectLowestReadingIdForReadingSetTimestamp();
	/**
	 * Select horz_sp_import.lastrecord
	 * @param devInst is the device to select for
	 * @param filename is the filename to select for
	 * @return the query result
	 */
	public Object[] selectImportLastRecord(Integer devInst, String filename);
	/**
	 *  Select most recent horz_sp_readingset.readingset_id for given device
	 * @return String for the readingid
	 */
	public Integer selectLatestReadingsetIdForDevice(Integer device);
	/**
	 *  Select most recent import record for given file on given device
	 *  @param device is the device id to check
	 *  @param filename is the filename to check
	 * @return String with the recent timestamp
	 */
	public String selectLastImportRecord(Integer device, String filename);
	/**
	 * Selects readings within a date range for a given device 
	 * @param device is the device id
	 * @param reading_type_id is self explanatory
	 * @param datatype is self explanatory 
	 * @param startdatetime is the begining of the range
	 * @param stopdatetime is the end of the range
	 * @return
	 */
	public Object[] selectReadingsForDeviceInstanceByDateRange(
			Integer device, Integer reading_type_id, String datatype, 
			String startdatetime, String stopdatetime);

}
