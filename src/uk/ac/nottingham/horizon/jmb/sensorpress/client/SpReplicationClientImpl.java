package uk.ac.nottingham.horizon.jmb.sensorpress.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpReplicationClientImpl implements SpReplicationClient {
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class.getName());
	
	public SpReplicationClientImpl(Level level){
			LOGGER.setLevel(level);
	}

	/**
	 * Replicates one sensorpress instance (client1) into another one (client2)
	 * @param client1 contains the data to replicate
	 * @param client2 receives new data
	 */
	@Override
	public void replicate(SpBaseClient client1, SpBaseClient client2) {
		LOGGER.info("calling: intersectRecentReadingsets");
		SpSelectionClient select = new SpSelectionClientImpl(client1, LOGGER.getLevel());
		Object[] recentReadings = select.intersectRecentReadingsets(client1, client2);
		if(null==recentReadings){			
			LOGGER.severe("Failed to intersect recent readingsets. Aborting operation.");
			return;
		}
		LOGGER.info("calling: insertRecordsIntoReadingsets");
		SpInsertionClient insert = new SpInsertionClientImpl(client2, LOGGER.getLevel());
		insert.insertRecordsIntoReadingsets(recentReadings);	
		LOGGER.info("calling: insertRecentReadings");	
		insert.insertRecentReadings(client1, client2);
		
		LOGGER.info("Complete.");

	}

}
