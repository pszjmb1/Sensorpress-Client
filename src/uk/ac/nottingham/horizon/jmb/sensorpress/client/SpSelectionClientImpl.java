/* Implementation class for selection client operations on sensorpress.
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

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpSelectionClientImpl implements SpSelectionClient {
	SpBaseClient myClient;
	private String recentReadingsetTimestamp = null;
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class.getName());
	
	public SpSelectionClientImpl(SpBaseClient aClient,Level level){
		LOGGER.setLevel(level);
		myClient = aClient;
	}	

	public SpBaseClient getClient() {
		return myClient;
	}

	public void setMyClient(SpBaseClient Client) {
		this.myClient = Client;
	}

	@Override
	public Object[] select(String type, Integer limit) {
		return myClient.execute("sensorpress.select", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), 
					type, limit});
	}
	
	/**
	 * Select the last ten blog posts
	 * @return the blog posts
	 */
	@Override
	public Object[] selectRecentBlogPosts() {
		// new Integer(1),user,password,new Integer(10)
		return myClient.execute("metaWeblog.getRecentPosts", 
				new Object[]{new Integer(1), myClient.getUser(),
					myClient.getPwrd(), new Integer(10)});
	}

	
	/**
	 * Routine to show sensorpress tables from XML-RPC
	 * @param url is the URL to the XML-RPC interface 
	 * (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user is the user name 
	 * @param pwrd is the password
	 * @return the resultant rowset
	 */
	@Override
	public Object[] tablesFromXMLRPC() { 
		return myClient.execute("sensorpress.tables", 
				new Object[]{myClient.getUser(),myClient.getPwrd()});
	}	

	/**
	 * Routine to show sensorpress table columns from XML-RPC
	 * @param table is the table to display the columns for
	 * @return the resultant rowset
	 */
	@Override
	public Object[] columnsFromXMLRPC(String table) {
		return myClient.execute("sensorpress.columns", 
				new Object[]{myClient.getUser(),myClient.getPwrd(),table});
	}
	
	/**
	 * Select readingsets from url1 that are more recent than those in url2 
	 * @param client1 connects to the ur1 to select readings from
	 * @param client2 connects to the url to check readingsets against
	 * @return the more recent readingsets
	 */
	@Override
	public Object[] intersectRecentReadingsets(
			SpBaseClient client1, SpBaseClient client2) {
		String aQuery = "SELECT `timestamp` FROM `sensorpress`." +
				"`hn_sp_readingset` " +
				"ORDER BY `timestamp` DESC LIMIT 1";
		Object[] results = client2.doQueryXMLRPC(aQuery);
		if(null == results){
			return null;
		}

		// Select url1.readingsets > url2's most recent
		aQuery = "SELECT * FROM `sensorpress`.`hn_sp_readingset`";
		recentReadingsetTimestamp = null;
		if (results.length > 0) { // only select the more recent readingsets
			recentReadingsetTimestamp = results[0].toString().split("[=}]")[1];
			// System.out.println(recent);
			aQuery = aQuery + " WHERE `timestamp` > " + "\"" + 
			recentReadingsetTimestamp + "\"";
			//System.out.println(aQuery);
			results = client1.doQueryXMLRPC(aQuery);
		} else { // select them all
			//aQuery = aQuery + " LIMIT 10";	// for testing purposes REMOVE for live!!!!
			results = client1.doQueryXMLRPC(aQuery);
			/*for(int i = 0; i < results.length; i++){
				System.out.println(results[i]);
			}*/
		}
		return results;
	}

	/**
	 *  Select reading.id for earliest selected url1.readingset
	 * @return String for the readingid
	 */
	@Override
	public String selectLowestReadingIdForReadingSetTimestamp() {
		String query = null;
		if(null != recentReadingsetTimestamp){
			query = "SELECT `hn_sp_reading`.`reading_id` " + 
						"FROM `hn_sp_reading`, `hn_sp_readingset`" + 
						"WHERE `hn_sp_reading`.`readingset_id` =  " +
						"`hn_sp_readingset`.`readingset_id` AND " +
						"`hn_sp_readingset`.`timestamp` = " +
						"\"" + recentReadingsetTimestamp + 
						"\" ORDER BY `hn_sp_reading`.`reading_id` ASC LIMIT 1";
		}else{
			query = "SELECT `reading_id` " +
					"FROM `hn_sp_reading` " +
					"ORDER BY `reading_id` ASC LIMIT 1";
		}
		Object[] res = myClient.doQueryXMLRPC(query);
		
		if(res.length > 0){
			String lowestid = myClient.doQueryXMLRPC(query)[0].toString();
			if(null != lowestid){
				lowestid =lowestid.replace("{idhn_sp_reading=", "");
				return lowestid.replace("}", "");	
			}else{
				return "1";
			}
		}else{
			return "1";
		}
	}
	
	public Object[] selectImportLastRecord(Integer devInst, String filename){
		String aQuery = "SELECT `lastrecord` FROM `sensorpress`." +
				"`hn_sp_import` WHERE filename = \"" + filename +
				"\" AND `deviceinstance_id` = " + 
				devInst + " ORDER BY `lastrecord` DESC LIMIT 1";
		//System.out.println(aQuery);
		return myClient.doQueryXMLRPC(aQuery);
	}
	
	/**
	 *  Select most recent hn_sp_readingset.readingset_id for given device
	 * @return Integer for the readingid
	 */
	@Override
	public Integer selectLatestReadingsetIdForDevice(Integer device){
		Integer retval = -1;
		Object[] exec = myClient.execute(
				"sensorpress.latestReadingsetIdForDevice", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), device});
		if(null == exec || exec.length < 1){
			return retval;
		}
		try{
			retval = Integer.valueOf(exec[0].toString().replace(
					"{readingset_id=", "").replace("}", ""));
		}catch(NumberFormatException e){
			// Ignore the problem
			retval = -1;
		}
		return retval;		
	}
	
	/**
	 *  Select most recent hn_sp_readingset.readingset_id
	 * @return Integer for the readingset_id
	 */
	@Override
	public Integer selectLatestReadingsetId(){
		Integer retval = -1;
		Object[] exec = myClient.execute(
				"sensorpress.latestReadingsetId", 
				new Object[]{myClient.getUser(),myClient.getPwrd()});
		if(null == exec || exec.length < 1){
			return retval;
		}
		try{
			retval = Integer.valueOf(exec[0].toString().replace(
					"{readingset_id=", "").replace("}", ""));
		}catch(NumberFormatException e){
			// Ignore the problem
			retval = -1;
		}
		return retval;		
	}
	

	/**
	 *  Select most recent import record for given file on given device
	 *  @param device is the device id to check
	 *  @param filename is the filename to check
	 * @return String with the recent timestamp or null on error or none found.
	 */
	@Override
	public String selectLastImportRecord(Integer device, String filename){
		String retval = null;
		Object[] exec = myClient.execute(
				"sensorpress.select_lastimportRecord", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), filename, device});
		try{
			if(null == exec ||exec.length < 1){
				return retval;
			}
			retval = exec[0].toString();
		}catch(Exception e){
			retval = null;
		}
		return retval;		
	}
	/**
	 * Selects readings within a date range for a given device 
	 * @param device is the device id
	 * @param reading_type_id is self explanatory
	 * @param datatype is self explanatory 
	 * @param startdatetime is the begining of the range
	 * @param stopdatetime is the end of the range
	 * @return an obj[] containing: readingset_id, timestamp and value
	 */
	@Override
	public Object[] selectReadingsForDeviceInstanceByDateRange(
			Integer device, Integer reading_type_id, String datatype, 
			String startdatetime, String stopdatetime){
		return myClient.execute(
				"sensorpress.readingsForDeviceInstanceByDateRange", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), device,
						reading_type_id,datatype,startdatetime,stopdatetime});	
		
	}
}
