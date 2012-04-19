/*  SimpleClient.java
  	Minimal example of accessing a Wordpress site's XML-RPC interface.
    Copyright (C) 2012  Jesse Blum (JMB)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.nottingham.horizon.jmb.shadowpress.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class EndToEndClient {

	/**
	 * Routine to call simple select statements by XML-RPC
	 * 
	 * @param url
	 *            is the URL to the XML-RPC interface (such as
	 *            http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user
	 *            is the user name
	 * @param pwrd
	 *            is the password
	 * @param type
	 *            is the type of table to select from
	 * @param limit
	 *            is the numebr of records to return
	 * @return the resultant rowset
	 */
	public Object[] selectFromXMLRPC(String url, String user, String pwrd,
			String type, Integer limit) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		Object[] params;
		if (limit > 0) {
			params = new Object[] { user, pwrd, new String(type), limit };

			try {
				return (Object[]) client.execute("shadowpress.select", params);
			} catch (XmlRpcException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Routine to insert readings into a DB via XML-RPC
	 * 
	 * @param url
	 *            is the URL to the XML-RPC interface (such as
	 *            http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user
	 *            is the user name
	 * @param pwrd
	 *            is the password
	 * @param type
	 *            is the data_type to use (such as dec_4_1)
	 * @param value
	 *            is reading value
	 * @param readingset_id
	 *            is id of the reading set that the reading belongs to
	 * @param reading_type
	 *            is the id of the type of reading
	 * @return an int with the new reading's ID or an error if insert was
	 *         unseccessful
	 */
	public Object[] insert_readingXMLRPC(String url, String user, String pwrd,
			String type, String value, Integer readingset_id,
			Integer reading_type) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		Object[] params = new Object[] { user, pwrd, type, value,
				readingset_id, reading_type };

		try {
			return (Object[]) client.execute("shadowpress.insert_reading",
					params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Routine to query a DB via XML-RPC
	 * 
	 * @param url
	 *            is the URL to the XML-RPC interface (such as
	 *            http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user
	 *            is the user name
	 * @param pwrd
	 *            is the password
	 * @param query
	 *            is a query to pass to the server to perform
	 * @return a query result
	 */
	public Object[] doQueryXMLRPC(String url, String user, String pwrd,
			String query) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		Object[] params;
		params = new Object[] { user, pwrd, query };

		try {
			return (Object[]) client.execute("shadowpress.query", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Select readingsets from url1 that are more recent than in url2
	 * 
	 * @param url1
	 * @param user1
	 * @param pwrd1
	 * @param url2
	 * @param user2
	 * @param pwrd2
	 * @return the more recent readingsets
	 */
	public Object[] intersectRecentReadingsets(String url1, String user1,
			String pwrd1, String url2, String user2, String pwrd2) {
		String aQuery = "SELECT `timestamp` FROM `shadowpress`.`horz_sp_readingset` " +
				"ORDER BY `timestamp` DESC LIMIT 1";
		Object[] results = doQueryXMLRPC(url2, user2, pwrd2, aQuery);

		// Select url1.readingsets > url2's most recent
		aQuery = "SELECT * FROM `shadowpress`.`horz_sp_readingset`";
		String recent = null;
		if (results.length > 0) { // only select the more recent readingsets
			recent = results[0].toString().split("[=}]")[1];
			// System.out.println(recent);
			aQuery = aQuery + " WHERE `timestamp` > " + "\"" + recent + "\"";
			// System.out.println(aQuery);
			results = doQueryXMLRPC(url1, user1, pwrd1, aQuery);
		} else { // select them all
			//aQuery = aQuery + " LIMIT 10";	// for testing purposes REMOVE for live!!!!
			results = doQueryXMLRPC(url1, user1, pwrd1, aQuery);
		}
		return results;
	}

	/**
	 * Inserts multiple records into SP readingset 
	 * @param url2
	 * @param user2
	 * @param pwrd2
	 * @param records
	 */
	public void insertRecordsIntoReadingsets(String url2, String user2,
			String pwrd2, Object[] records) {
		String aQuery = "INSERT IGNORE INTO `shadowpress`.`horz_sp_readingset`("
				+ "timestamp,readingset_id,"
				+ "horz_sp_readingset_info_horz_sp_readingset_info_id,"
				+ "horz_sp_deviceinstance_idhorz_sp_deviceinstance) "
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

		Object[] results = doQueryXMLRPC(url2, user2, pwrd2, aQuery);

		for (int i = 0; i < results.length; i++) {
			System.out.println(results[i]);
		}
	}

	/**
	 * Replicates one shadowpress instance (url1) into another one (url2)
	 * Currently only supports readings and readingsets
	 */
	public void replicateShadowpress(String url1, String user1, String pwrd1,
			String url2, String user2, String pwrd2) {
		System.out.println("calling: intersectRecentReadingsets");
		Object[] results = intersectRecentReadingsets(url1, user1, pwrd1, url2,
				user2, pwrd2);

		System.out.println("calling: insertRecordsIntoReadingsets");
		insertRecordsIntoReadingsets(url2, user2,
				pwrd2, results);
		// Select url1.reading.id for earliest selected url1.readingset
		// Select url1.reading.ids >= selected url1.reading.id
		// Insert selected url1.readings into url2.reading
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EndToEndClient sc = new EndToEndClient();
		sc.replicateShadowpress("http://192.168.56.101/wordpress/xmlrpc.php",
				"admin", "qwerty", "http://192.168.56.102/xmlrpc.php", "admin",
				"a");
		
		
		/*
		 * Object[] results =
		 * sc.selectFromXMLRPC("http://192.168.56.102/xmlrpc.php"
		 * ,//"http://192.168.56.101/wordpress/xmlrpc.php",
		 * "admin","a","reading",10);//"admin","qwerty","reading",10);
		 * 
		 * String aQuery = "SELECT * FROM `shadowpress`.`horz_sp_reading`";
		 * Object[] results =
		 * sc.doQueryXMLRPC("http://192.168.56.102/xmlrpc.php",
		 * "admin","a",aQuery);
		 * 
		 * 
		 * if(null != results){ for(int i = 0; i < results.length; i++){
		 * System.out.println(results[i]); } }
		 */
		
		/*String aQuery = "INSERT IGNORE INTO `shadowpress`.`horz_sp_readingset`(timestamp,readingset_id,horz_sp_readingset_info_horz_sp_readingset_info_id,horz_sp_deviceinstance_idhorz_sp_deviceinstance) VALUES('2012-03-12 11:37:27',2,1,1),('2012-03-12 11:42:27',3,1,1),('2012-03-12 11:47:27',4,1,1),('2012-03-12 11:52:27',5,1,1),('2012-03-12 11:57:27',6,1,1),('2012-03-12 12:02:27',7,1,1),('2012-03-12 12:07:27',8,1,1),('2012-03-12 12:12:27',9,1,1),('2012-03-12 12:17:27',10,1,1),('2012-03-12 12:22:27',11,1,1)";
		Object[] results =
		sc.doQueryXMLRPC("http://192.168.56.102/xmlrpc.php",
		"admin","a",aQuery);
		for (int i = 0; i < results.length; i++) {
			System.out.println(results[i]);
		}*/

	}
}
