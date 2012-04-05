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
	
	public void getData(String url){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    Object[] params = new Object[]{new Integer(1),"admin","qwerty"};
	    try {
			String result = (String)client.execute("shadowpress.display", params);
			System.out.println(result);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleClient sc = new SimpleClient();
		sc.getData("http://192.168.56.101/wordpress/xmlrpc.php");
	}
}
