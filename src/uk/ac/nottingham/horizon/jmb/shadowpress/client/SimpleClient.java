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


public class SimpleClient {
	
	/**
	 * Routine to call simple select statements by XML-RPC
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user is the user name 
	 * @param pwrd is the password
	 * @param type is the type of table to select from
	 * @param limit is the numebr of records to return
	 * @return the resultant rowset
	 */
	public Object[] selectFromXMLRPC(String url, String user, String pwrd, 
			String type, Integer limit){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    Object[] params;
	    if(limit > 0){
	    	params = new Object[]{user,pwrd,new String(type), limit};
	    
		    try {
		    	return (Object[])client.execute("shadowpress.select", params);
			} catch (XmlRpcException e) {
				e.printStackTrace();
			}
	    }
	    return null;
	}
	
	/**
	 * Routine to insert readings into a DB via XML-RPC
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user is the user name 
	 * @param pwrd is the password
	 * @param type is the data_type to use (such as dec_4_1)
	 * @param value is reading value
	 * @param readingset_id is id of the reading set that the reading belongs to
	 * @param reading_type is the id of the type of reading
	 * @return an int with the new reading's ID or an error if insert was unseccessful
	 */
	public Object[] insert_readingXMLRPC(String url, String user, String pwrd, 
			String type, String value, Integer readingset_id, Integer reading_type){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    Object[] params = new Object[]{user,pwrd,type, 
	    		value,readingset_id,reading_type};
    
	    try {
	    	return (Object[])client.execute("shadowpress.insert_reading", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	    return null;
	}
	/**
	 * Routine to query a DB via XML-RPC
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user is the user name 
	 * @param pwrd is the password
	 * @param query is a query to pass to the server to perform
	 * @return a query result 
	 */
	public Object[] doQueryXMLRPC(String url, String user, String pwrd, String query){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    Object[] params;
	    params = new Object[]{user,pwrd,query};
   
	    try {
	    	return (Object[])client.execute("shadowpress.query", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	    
	    return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC("http://192.168.56.101/wordpress/xmlrpc.php", 
				"admin","qwerty","reading",10);
		
		//String aQuery = "SELECT * FROM `shadowpress`.`horz_sp_readingset_info` ORDER BY `horz_sp_readingset_info_id` DESC LIMIT 10";
		//Object[] results = sc.doQueryXMLRPC("http://192.168.56.101/wordpress/xmlrpc.php", 
		//		"admin","qwerty",aQuery);
		
				
		if(null != results){
			for(int i = 0; i < results.length; i++){
				System.out.println(results[i].getClass());
				System.out.println(results[i]);
			}
		}
		
	}
}
