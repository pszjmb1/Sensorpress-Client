package uk.ac.nottingham.horizon.jmb.shadowpress.client.v2;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SpReplicationClientImpl implements SpReplicationClient {
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class.getName());
	
	public SpReplicationClientImpl(Level level){
			LOGGER.setLevel(level);
	}

	/**
	 * Replicates one shadowpress instance (client1) into another one (client2)
	 * @param client1 contains the data to replicate
	 * @param client2 receives new data
	 */
	@Override
	public void replicate(SpXMLRpcClient client1, SpXMLRpcClient client2) {
		LOGGER.info("calling: intersectRecentReadingsets");
		System.out.println("calling: intersectRecentReadingsets");
		SpSelectionClient select = new SpSelectionClientImpl(client1, LOGGER.getLevel());
		Object[] recentReadings = select.intersectRecentReadingsets(client1, client2);

		LOGGER.info("calling: insertRecordsIntoReadingsets");
		SpInsertionClient insert = new SpInsertionClientImpl(client2, LOGGER.getLevel());
		insert.insertRecordsIntoReadingsets(recentReadings);		
		insert.insertRecentReadings(client1, client2);
		
		LOGGER.info("Complete.");

	}

}
