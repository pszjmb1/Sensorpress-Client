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
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC("http://192.168.56.101/wordpress/xmlrpc.php", 
				"admin","qwerty","reading",10);
		for(int i = 0; i < results.length; i++){
			System.out.println(results[i]);
		}
	}
}
