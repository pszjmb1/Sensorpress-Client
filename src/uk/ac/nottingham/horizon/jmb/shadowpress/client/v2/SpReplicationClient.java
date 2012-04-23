package uk.ac.nottingham.horizon.jmb.shadowpress.client.v2;

public interface SpReplicationClient extends SpBaseClient {
	
	/**
	 * Replicates one shadowpress instance (url1) into another one (url2)
	 * @param url1 is a url to replicate
	 * @param user1 is the user for url1
	 * @param pwrd1 is the password for url1
	 * @param url2 is a url receive new data
	 * @param user1 is the user for url2
	 * @param pwrd1 is the password for url2
	 */
	public void replicate(String url1, String user1, String pwrd1,
			String url2, String user2, String pwrd2);
}
