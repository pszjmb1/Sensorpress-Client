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

import junit.framework.Assert;

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
	
	private XmlRpcClient setupProxyConnection(String url, String proxy_host, String proxy_port){
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    try {
			config.setServerURL(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	    XmlRpcClient client = new XmlRpcClient();
	    XmlRpcCommonsTransportFactory transportFactory  = new XmlRpcCommonsTransportFactory( client );
	    client.setTransportFactory(transportFactory);
	    HttpClient httpClient = new HttpClient();
	    httpClient.getHostConfiguration().setProxy(proxy_host, new Integer(proxy_port));

	    transportFactory.setHttpClient(httpClient);
	    client.setTransportFactory( transportFactory );
	    client.setConfig(config);
	    return client;
	}
	
	public Object[] selectFromXMLRPC(String url, String user, String password, String proxy_host, 
			String proxy_port, String type, Integer limit){
		
		XmlRpcClient client = setupProxyConnection(url, proxy_host, proxy_port);
	    Object[] params;
	    if(limit > 0){
	    	params = new Object[]{user,password,new String(type), limit};
	    
		    try {
		    	return (Object[])client.execute("shadowpress.select", params);
			} catch (XmlRpcException e) {
				e.printStackTrace();
			}
	    }
	    return null;
	}
	
	public Object[] insertUsingXMLRPC(String url, String user, String password, String proxy_host, 
			String proxy_port, String type, String value, Integer readingset_id, Integer reading_type){
		
		XmlRpcClient client = setupProxyConnection(url, proxy_host, proxy_port);
	    Object[] params = new Object[]{user,password,type, 
	    		value,readingset_id,reading_type};
    
	    try {
	    	return (Object[])client.execute("shadowpress.insert_reading", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	    return null;
	}

	/**
	 * @param args 0 = url , 1 = user name, 2 = password, 3 = proxy host, 4= proxy port
	 */
	public static void main(String[] args) {
		
		SimpleProxyClient sc = new SimpleProxyClient();

		String url = args[0];
		String user = args[1];
		String pwd = args[2];
		String proxyurl = args[3];
		String port  = args[4];
		String type = args[5];
		String limit  = args[6];
		
		//sc.getData("http://192.168.56.101/wordpress/xmlrpc.php");
		//sc.getData(args[0],args[1],args[2], args[3], args[4]);
		/*Object[] results = sc.selectFromXMLRPC("http://192.168.56.102/xmlrpc.php",//"http://192.168.56.101/wordpress/xmlrpc.php", 
				"admin","a","reading",10);//"admin","qwerty","reading",10);*/
		Object[] results = sc.selectFromXMLRPC(url, user, pwd, proxyurl, port, type , Integer.valueOf(limit) );
		
		System.out.println(url + ","+user);
		if(null != results){
			System.out.println(results.length);
			for(int i = 0; i < results.length; i++){
				//System.out.println(results[i].getClass());
				System.out.println(results[i]);
			}
		}else{
			System.out.println("null results");
		}
		
		results = sc.insertUsingXMLRPC(
				url, user, pwd, proxyurl, port,"dec_4_1","999.9",9999,9999);
		Assert.assertEquals(1, results.length);
		System.out.println(results.length);
		for(int i = 0; i < results.length; i++){
			System.out.println(results[i]);
		}
	}

}
