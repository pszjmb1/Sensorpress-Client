package uk.ac.nottingham.horizon.jmb.shadowpress.client;

public interface SpReplicationClient {
	
	/**
	 * Replicates one shadowpress instance (client1) into another one (client2)
	 * @param client1 contains the data to replicate
	 * @param client2 receives new data
	 */
	public void replicate(SpBaseClient client1, SpBaseClient client2);
}
