package uk.ac.nottingham.horizon.jmb.sensorpress.client.tests;

import java.util.logging.Level;

import org.junit.Test;


import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpReplicationClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpReplicationClientImpl;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpXMLRpcClient;

public class SpReplicationClientTest {
	

	static String defaultURL = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user = "admin";
	static String pwrd = "qwerty";
	static String url2 = "http://192.168.56.103/xmlrpc.php";
	static String user2 = "admin";
	static String pwrd2 = "a";

	//see ~/Shadowpress/rsynaddon for values
	
	static String url3 = "...";
	static String user3 = "...";
	static String pwrd3 = "...";
	static String proxy_host = "...";
	static String proxy_port = "...";
	static String user4 = "...";
	static String pwrd4 = "...";

	/**
	 * Note that this may be a long running test depending on content in the databases.
	 */
	@Test
	public void testReplicate() {
		SpReplicationClient replicator = new SpReplicationClientImpl(Level.INFO);
		SpBaseClient client1 = new SpXMLRpcClient(user, pwrd, Level.INFO);
		client1.simpleClient(defaultURL, true, true);
		SpBaseClient client2 = new SpXMLRpcClient(user2, pwrd2, Level.INFO);
		client2.simpleClient(url2, true, true);
		replicator.replicate(client1, client2);
	}

	/**
	 * Note that this may be a long running test depending on content in the databases.
	 */
	@Test
	public void testReplicatThroughProxy() {
		SpReplicationClient replicator = new SpReplicationClientImpl(Level.INFO);
		SpBaseClient client1 = new SpXMLRpcClient(user, pwrd, Level.INFO);
		client1.simpleClient(defaultURL, true, true);
		SpBaseClient client2 = new SpXMLRpcClient(user3, pwrd3, Level.INFO);

		if(null == client2.setProxyClient(url3, proxy_host, proxy_port)){
			assert(false);
		}
		
		replicator.replicate(client1, client2);
	}
	
	@Test
	public void testProxy(){
		SpBaseClient client = new SpXMLRpcClient(user3, pwrd3, Level.INFO);
		if(null == client.setProxyClient(url3, proxy_host, proxy_port)){
			assert(false);
		}
	    
		Object[] params = new Object[]{new Integer(1),user4, pwrd4};
	    try {
	    	Object result[] = (Object[])client.execute("wp.getAuthors", params);
			for(int i = 0; i< result.length; i++){
				System.out.println(result[i].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
