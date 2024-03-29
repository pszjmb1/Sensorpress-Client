package uk.ac.nottingham.horizon.jmb.sensorpress.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpInsertionClientImpl implements SpInsertionClient {
	SpBaseClient myClient;
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class.getName());
	
	public SpInsertionClientImpl(SpBaseClient aClient, Level aLevel){
		myClient = aClient;
		LOGGER.setLevel(aLevel);
	}	

	public SpBaseClient getClient() {
		return myClient;
	}

	public void setMyClient(SpBaseClient Client) {
		this.myClient = Client;
	}

	/**
	 * Routine to insert readings into a DB via XML-RPC
	 * @param type is the data_type to use (such as dec_4_1)
	 * @param value is reading value
	 * @param readingset_id is id of the reading set that the reading belongs to
	 * @param reading_type is the id of the type of reading
	 * @return an int with the new reading's ID or null or an error if insert was unsuccessful
	 */
	@Override
	public Object[] insertReading(String type, String value,
			Integer readingset_id, Integer reading_type) {		
			
		return myClient.execute("sensorpress.insert_reading", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), 
				type, value, readingset_id, reading_type});
	}	

	/**
	 * Routine to insert import records into a DB via XML-RPC
	 * @param type is the data_type to use (such as dec_4_1)
	 * @param filname is the file that was imported
	 * @param deviceInstanceId is the device instance that the data was captured from
	 * @param timestamp is the last timestamp value from the imported file
	 * @return the query result
	 */
	public Object[] insertImportRecord(String filname, Integer deviceInstanceId, 
			String timestamp){			
		return myClient.execute("sensorpress.insert_importRecord", 
				new Object[]{myClient.getUser(),myClient.getPwrd(), 
				filname, deviceInstanceId, timestamp});		
	}

	/**
	 * Inserts multiple records into SP readingset 
	 * @param records are the values to insert
	 * @return the insertion result
	 */
	@Override
	public Object[] insertRecordsIntoReadingsets(Object[] records) {
		String aQuery = "INSERT IGNORE INTO `sensorpress`.`hn_sp_readingset`("
				+ "timestamp,readingset_id,"
				+ "readingset_info_id,"
				+ "deviceinstance_id) "
				+ "VALUES";
		String result;
		for (int i = 0; i < records.length; i++) {
			result = records[i].toString();
			String[] fields = result.split("[=,}]");

			aQuery = aQuery + "('" + fields[1] + "'," + fields[3] + ","
					+ fields[5] + "," + fields[7] + "),";
		}
		aQuery = aQuery.substring(0, aQuery.length() - 1); // trim final ,
		//System.out.println(aQuery);

		return myClient.doQueryXMLRPC(aQuery);
	}
	/**
	 * insert readingsets from url1 that are more recent than in url2
	 * @param client1 is used to select readingsets from
	 * @param client2 is used to check readingsets against
	 * @return the insertion result or null if not completed.
	 */
	@Override
	public Object[] insertRecentReadings(SpBaseClient client1, SpBaseClient client2) {
		// Select url1.reading.id for earliest selected url1.readingset
		SpSelectionClient select = new SpSelectionClientImpl(client1, LOGGER.getLevel());
		String lowestid = select.selectLowestReadingIdForReadingSetTimestamp();
		String aQuery = 
				"SELECT COUNT(*) FROM `hn_sp_reading` WHERE reading_id >=" + lowestid;
		Object[] records = client1.doQueryXMLRPC(aQuery);
		if (records != null && records.length > 0) { 
			Integer numrecords = Integer.valueOf(records[0].toString().replace(
					"{COUNT(*)=","").replace("}", ""));
			Integer offsetIncrease = 500;
			HashMap<String, String> dictionary;
			for(int i = 0; i < numrecords; i+= offsetIncrease){
				aQuery = "SELECT * FROM `hn_sp_reading` " +
							"WHERE reading_id >=" + lowestid + " LIMIT " + i + "," + offsetIncrease;
				records = client1.doQueryXMLRPC(aQuery);
				if (records != null && records.length > 0) {
					aQuery = "INSERT IGNORE INTO `sensorpress`.`hn_sp_reading` " +
							"(`reading_id`, `value_blob`, `value_dec_4_1`, `value_dec_5_2`, `value_dec_8_2`, " +
							"`value_dec_12_6`, `value_int`, `readingset_id`, " +
							"`reading_type_id`) VALUES";
					String result;
					for (int j = 0; j < records.length; j++) {
						dictionary = new HashMap<String, String>();
						result = records[j].toString().replace("{", "").replace("}", "");
						String[] fields = result.split(",");
						String[] fields2;
						String val;
						for (int k = 0; k < fields.length; k++) {
							fields2 = fields[k].split("=");
							if(fields2.length > 1){
								val = fields2[1];
							}else{
								val = "null";
							}
							dictionary.put(fields2[0].trim(), val);
						}
						aQuery = aQuery + "('" + dictionary.get("reading_id") + "'," + 
								 dictionary.get("value_blob") + "," + dictionary.get("value_dec_4_1") + "," +
								 dictionary.get("value_dec_5_2") + "," + dictionary.get("value_dec_8_2") + "," +
								 dictionary.get("value_dec_12_6") + "," + dictionary.get("value_int") + "," +
								 dictionary.get("readingset_id") + "," + 
								 dictionary.get("reading_type_id") + "),";
					}		
					client2.doQueryXMLRPC(aQuery.substring(0,aQuery.length()-1));			
				}
			}
		}
		return null;
	}

}
