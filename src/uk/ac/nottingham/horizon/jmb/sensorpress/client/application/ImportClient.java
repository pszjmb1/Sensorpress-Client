package uk.ac.nottingham.horizon.jmb.sensorpress.client.application;

import java.util.logging.Level;

import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpPywwsImportClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpXMLRpcClient;

public class ImportClient {

	/**
	 * Driver to use the import client
	 * 
	 * @param uRL
	 *            is the url to connect to
	 * @param user
	 *            is the username for the SP client
	 * @param pwrd
	 *            is password corresponding to the username
	 * @param level
	 *            is the level of logging
	 * @param devId
	 *            is the device instance id
	 * @param infoId
	 *            is the readingset info id
	 * @param importDirectory
	 *            is the path to the directory to import
	 */
	public void runImport(String uRL, String user, String pwrd, Level level,
			Integer devId, Integer infoId, String importDirectory) {
		SpBaseClient base = new SpXMLRpcClient(user, pwrd, level);
		base.simpleClient(uRL, true, true);
		SpPywwsImportClient sc = new SpPywwsImportClient(base, level);
		sc.importDirectory(importDirectory, devId, infoId);
	}

	/**
	 * Driver to use the import client
	 * 
	 * @param uRL
	 *            is the url to connect to
	 * @param user
	 *            is the username for the SP client
	 * @param pwrd
	 *            is password corresponding to the username
	 * @param level
	 *            is the level of logging
	 * @param devId
	 *            is the device instance id
	 * @param infoId
	 *            is the readingset info id
	 * @param importDirectory
	 *            is the path to the directory to import
	 * @param proxyUrl
	 *            is self explanatory
	 * @param proxyPort
	 *            is self explanatory
	 */
	public void runImportProxy(String uRL, String user, String pwrd,
			Level level, Integer devId, Integer infoId, String importDirectory,
			String proxyUrl, String proxyPort) {
		SpBaseClient base = new SpXMLRpcClient(user, pwrd, level);
		base.simpleClient(uRL, true, true);
		SpPywwsImportClient sc = new SpPywwsImportClient(base, level);
		sc.importDirectory(importDirectory, devId, infoId);
	}

	public static void main(String args[]) {
		if (!(args.length == 6 || args.length == 8)) {
			String output = "ImportClient imports Pywws CSV files "
					+ "from a directory into Sensorpress."
					+ "\n@param uRL is the url to connect to"
					+ "\n@param user is the username for the SP client"
					+ "\n@param pwrd is password corresponding to the username"
					+ "\n@param devId is the device instance id"
					+ "\n@param infoId is the readingset info id"
					+ "\n@param importDirectory is the path to the directory to import"
					+ "\n@param proxyUrl is self explanatory [optional]"
					+ "\n@param proxyPort is self explanatory [optional]";
			System.err.println(output);
			System.exit(1);
		}
		ImportClient ic = new ImportClient();
		ic.runImport(args[0], args[1], args[2], Level.INFO,
				Integer.valueOf(args[3]), Integer.valueOf(args[4]), args[5]);
	}
}
