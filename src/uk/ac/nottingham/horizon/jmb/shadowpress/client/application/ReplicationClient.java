package uk.ac.nottingham.horizon.jmb.shadowpress.client.application;

import java.util.logging.Level;

import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpReplicationClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpReplicationClientImpl;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpXMLRpcClient;

public class ReplicationClient {
	/**
	 * Replicates one SPDB to another.
	 * 
	 * @param uRL
	 * @param user
	 * @param pwrd
	 * @param proxyUrl
	 * @param proxyPort
	 * @param uRL2
	 * @param user2
	 * @param pwrd2
	 * @param proxyUrl2
	 * @param proxyPort2
	 * @param level
	 */
	public void replicate(String uRL, String user, String pwrd,
			String proxyUrl, String proxyPort, String uRL2, String user2,
			String pwrd2, String proxyUrl2, String proxyPort2, Level level) {

		SpBaseClient client1 = new SpXMLRpcClient(user, pwrd, level);
		if (null == proxyUrl || "".equals(proxyUrl)) {
			client1.simpleClient(uRL, true, true);
		} else {
			if(null == client1.setProxyClient(uRL, proxyUrl, proxyPort)){
				String output = "Failed to get proxy client.";
				System.err.println(output);
				System.exit(1);
			}
		}

		SpBaseClient client2 = new SpXMLRpcClient(user2, pwrd2, Level.INFO);
		if (null == proxyUrl2|| "".equals(proxyUrl2)) {
			client2.simpleClient(uRL2, true, true);
		} else {
			if(null == client2.setProxyClient(uRL2, proxyUrl2, proxyPort2)){
				String output = "Failed to get proxy client.";
				System.err.println(output);
				System.exit(1);
			}
		}

		SpReplicationClient replicator = new SpReplicationClientImpl(Level.INFO);
		replicator.replicate(client1, client2);
	}

	public static void main(String args[]) {
		if (!(args.length == 10)) {
			String output = "Replicates one SPDB to another."
					+ "\n* @param url to copy from"
					+ "\n* @param username to login to url" + "\n* @param password to use to login"
					+ "\n* @param proxyUrl [may be \"\"]"
					+ "\n* @param proxyPort [may be \"\"]" + "\n* @param url to copy to"
					+ "\n* @param user2" + "\n* @param pwrd2"
					+ "\n * @param proxyUrl2 [may be \"\"]"
					+ "\n * @param proxyPort2 [may be \"\"]";
			System.err.println(output);
			System.exit(1);
		}
		ReplicationClient ic = new ReplicationClient();
		ic.replicate(args[0], args[1], args[2], args[3], args[4], args[5],
				args[6], args[7], args[8], args[9], Level.INFO);
	}

}
