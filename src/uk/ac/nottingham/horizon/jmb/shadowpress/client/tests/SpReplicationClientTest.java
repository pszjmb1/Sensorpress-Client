package uk.ac.nottingham.horizon.jmb.shadowpress.client.tests;

import static org.junit.Assert.*;

import java.util.logging.Level;

import org.junit.Test;

import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpReplicationClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpReplicationClientImpl;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpXMLRpcClient;

public class SpReplicationClientTest {
	

	static String defaultURL = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user = "admin";
	static String pwrd = "qwerty";
	static String url2 = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user2 = "admin";
	static String pwrd2 = "qwerty";

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

}
