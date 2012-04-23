package uk.ac.nottingham.horizon.jmb.shadowpress.client.v2;

public interface SpReplicationClient {
	
	/**
	 * Replicates one shadowpress instance (client1) into another one (client2)
	 * @param client1 contains the data to replicate
	 * @param client2 receives new data
	 */
	public void replicate(SpXMLRpcClient client1, SpXMLRpcClient client2);
}
