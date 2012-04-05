/* SimpleProxyClient uses XML-RPC to demonstrate access to a remote Wordpress 
 * blog through a proxy.
 * Copyright (C) 2012  Jesse Blum (JMB)

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
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class SimpleProxyClient {
	public void getData(String url, String user, String password, String proxy_host, String proxy_port){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    XmlRpcClient client = new XmlRpcClient();
	    XmlRpcCommonsTransportFactory transportFactory  = new XmlRpcCommonsTransportFactory( client );
	    client.setTransportFactory(transportFactory);
	    HttpClient httpClient = new HttpClient();
	    httpClient.getHostConfiguration().setProxy(proxy_host, new Integer(proxy_port));

	    transportFactory.setHttpClient(httpClient);
	    client.setTransportFactory( transportFactory );
	    client.setConfig(config);
	    
	    //used for metaWeblog.getRecentPosts
	    Object[] params = new Object[]{new Integer(1),user,password,new Integer(10)};
	    
	    //used for wp.getAuthors
	    //Object[] params = new Object[]{new Integer(1),user,password};
	    
	    try {
			Object result[] = (Object[])client.execute("metaWeblog.getRecentPosts", params);
	    	//Object result[] = (Object[])client.execute("wp.getAuthors", params);
			for(int i = 0; i< result.length; i++){
				System.out.println(result[i].toString());
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args 0 = url , 1 = user name, 2 = password, 3 = proxy host, 4= proxy port
	 */
	public static void main(String[] args) {
		SimpleProxyClient sc = new SimpleProxyClient();
		//sc.getData("http://192.168.56.101/wordpress/xmlrpc.php");
		sc.getData(args[0],args[1],args[2], args[3], args[4]);
	}

}
