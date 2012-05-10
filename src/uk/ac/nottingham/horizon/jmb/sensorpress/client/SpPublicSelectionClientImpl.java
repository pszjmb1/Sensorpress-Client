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

public class SpPublicSelectionClientImpl implements SpPublicSelectionClient {
	SpBaseClient myClient;
	private String recentReadingsetTimestamp = null;
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class.getName());
	
	public SpPublicSelectionClientImpl(SpBaseClient aClient,Level level){
		LOGGER.setLevel(level);
		myClient = aClient;
	}	

	public SpBaseClient getClient() {
		return myClient;
	}

	public void setMyClient(SpBaseClient Client) {
		this.myClient = Client;
	}

	/**
	 *  Select reading.id for earliest selected url1.readingset
	 * @return String for the readingid
	 */
	@Override
	public String selectLowestReadingIdForReadingSetTimestamp() {
		String query = null;
		Object[] res;
		if(null != recentReadingsetTimestamp){
			res = myClient.execute(
					"sensorpress.select_lowestReadingIdForReadingSetTimestamp", 
					new Object[]{recentReadingsetTimestamp});
			
		}else{
			query = "SELECT `reading_id` " +
					"FROM `hn_sp_reading` " +
					"ORDER BY `reading_id` ASC LIMIT 1";
				res = myClient.doQueryXMLRPC(query);
		}
		
		if(res.length > 0){
			String lowestid = res[0].toString();
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
	

	/**
	 *  Select most recent hn_sp_readingset.readingset_id for given device
	 * @return Integer for the readingid
	 */
	@Override
	public Integer selectLatestReadingsetIdForDevice(Integer device){
		Integer retval = 1;
		Object[] exec = myClient.execute(
				"sensorpress.latestReadingsetIdForDevice", 
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
