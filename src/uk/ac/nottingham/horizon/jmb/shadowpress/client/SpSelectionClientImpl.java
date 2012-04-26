/* Implementation class for selection client operations on Shadowpress.
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
		return myClient.execute("shadowpress.select", 
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
	 * Routine to show Shadowpress tables from XML-RPC
	 * @param url is the URL to the XML-RPC interface 
	 * (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user is the user name 
	 * @param pwrd is the password
	 * @return the resultant rowset
	 */
	@Override
	public Object[] tablesFromXMLRPC() { 
		return myClient.execute("shadowpress.tables", 
				new Object[]{myClient.getUser(),myClient.getPwrd()});
	}	

	/**
	 * Routine to show Shadowpress table columns from XML-RPC
	 * @param table is the table to display the columns for
	 * @return the resultant rowset
	 */
	@Override
	public Object[] columnsFromXMLRPC(String table) {
		return myClient.execute("shadowpress.columns", 
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
		String aQuery = "SELECT `timestamp` FROM `shadowpress`." +
				"`horz_sp_readingset` " +
				"ORDER BY `timestamp` DESC LIMIT 1";
		Object[] results = client2.doQueryXMLRPC(aQuery);
		if(null == results){
			return null;
		}

		// Select url1.readingsets > url2's most recent
		aQuery = "SELECT * FROM `shadowpress`.`horz_sp_readingset`";
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
			query = "SELECT `horz_sp_reading`.`idhorz_sp_reading` " + 
						"FROM `horz_sp_reading`, `horz_sp_readingset`" + 
						"WHERE `horz_sp_reading`.`horz_sp_readingset_readingset_id` =  " +
						"`horz_sp_readingset`.`readingset_id` AND " +
						"`horz_sp_readingset`.`timestamp` = " +
						"\"" + recentReadingsetTimestamp + 
						"\" ORDER BY `horz_sp_reading`.`idhorz_sp_reading` ASC LIMIT 1";
		}else{
			query = "SELECT `idhorz_sp_reading` " +
					"FROM `horz_sp_reading` " +
					"ORDER BY `idhorz_sp_reading` ASC LIMIT 1";
		}
		Object[] res = myClient.doQueryXMLRPC(query);
		
		if(res.length > 0){
			String lowestid = myClient.doQueryXMLRPC(query)[0].toString();
			if(null != lowestid){
				lowestid =lowestid.replace("{idhorz_sp_reading=", "");
				return lowestid.replace("}", "");	
			}else{
				return "1";
			}
		}else{
			return "1";
		}
	}
	
	public Object[] selectImportLastRecord(Integer devInst, String filename){
		String aQuery = "SELECT `lastrecord` FROM `shadowpress`." +
				"`horz_sp_import` WHERE filename = \"" + filename +
				"\" AND `horz_sp_deviceinstance_idhorz_sp_deviceinstance` = " + 
				devInst + " ORDER BY `lastrecord` DESC LIMIT 1";
		//System.out.println(aQuery);
		return myClient.doQueryXMLRPC(aQuery);
	}
	

	/**
	 *  Select most recent horz_sp_readingset.readingset_id for given device
	 * @return Integer for the readingid
	 */
	@Override
	public Integer selectLatestReadingsetIdForDevice(Integer device){
		Integer retval = 1;
		Object[] exec = myClient.execute(
				"shadowpress.latestReadingsetIdForDevice", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), device});
		if(exec.length < 1){
			return retval;
		}
		try{
			retval = Integer.valueOf(exec[0].toString().replace(
					"{readingset_id=", "").replace("}", ""));
		}catch(NumberFormatException e){
			// Ignore the problem
			retval = 1;
		}
		return retval;		
	}
}
