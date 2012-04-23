/* XML-RPC based implementation of client operations for Shadowpress.
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
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.commons.httpclient.HttpClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class SpXMLRpcClient implements SpBaseClient {
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class.getName());
	
	XmlRpcClient client;	
	String user;
	String pwrd;
	
	public SpXMLRpcClient(String username, String password, Level level){
		LOGGER.setLevel(level);
		user = username;
		pwrd = password;
	}	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwrd() {
		return pwrd;
	}

	public void setPwrd(String pwrd) {
		this.pwrd = pwrd;
	}



	/**
	 * Retrieves a configured XmlRpcClient client.   
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param reset toggles whether or not to make a new connection or use an existing one
	 * @param store toggles whether or not to store this connection
	 * @return a configured XmlRpcClient client or null if an error occured.
	 */
	@Override
	public XmlRpcClient simpleClient(String url, boolean reset, boolean store) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		XmlRpcClient aClient = null;
		if(!reset && null != client){
			return client;
		}
	    try {
			config.setServerURL(new URL(url));
			aClient = new XmlRpcClient();
		    aClient.setConfig(config);			
			if(store){
				client = aClient;
			}
		} catch (MalformedURLException e) {
			LOGGER.severe(e.getMessage());
		}
	    return aClient;
	}
	
	/**
	 * Setsup an httpClient for use through a proxy server
	 * @param proxy_host
	 * @param proxy_port
	 * @return the proxy enabled httpClient
	 */
	public HttpClient setupProxy(String proxy_host, String proxy_port){
		HttpClient httpClient;
	    httpClient = new HttpClient();
	    httpClient.getHostConfiguration().setProxy(proxy_host, new Integer(proxy_port));
	    return httpClient;
	}

	/**
	 * Retrieves a configured XmlRpcClient client using more complex mechanisms such as proxy servers.   
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param reset toggles whether or not to make a new connection or use an existing one
	 * @param store toggles whether or not to store this connection
	 * @param httpClient the HTTP accessor supporting more complex interactions
	 * @return a configured XmlRpcClient client or null if an error occured.
	 */
	@Override
	public XmlRpcClient getComplexClient(String url, boolean reset, boolean store, HttpClient httpClient) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    XmlRpcClient aClient = null;
	    if(!reset && null != client){
			return client;
		}
	    try {
			config.setServerURL(new URL(url));
			aClient = new XmlRpcClient();
		    XmlRpcCommonsTransportFactory transportFactory  = new XmlRpcCommonsTransportFactory( client );
		    aClient.setTransportFactory(transportFactory);

		    transportFactory.setHttpClient(httpClient);
		    aClient.setTransportFactory( transportFactory );
		    aClient.setConfig(config);

			if(store){
				client = aClient;
			}
		} catch (MalformedURLException e) {
			LOGGER.severe(e.getMessage());
		}
		return aClient;
	}
	/**
	 * Stores user details for accessing a Shadowpress instance
	 * @param username
	 * @param password
	 */
	public void setUserDetails(String username, String password){
		username = user;
		pwrd = password;
	}
	
	/**
	 * Executes a client operation.
	 * @param aClient is the client to perform the operation
	 * @param operation is the operation to execute
	 * @param params are the parameters to the operation
	 * @return an obj array with the results
	 */
	public Object[] execute(XmlRpcClient aClient, String operation, Object[] params){
		if(null == aClient){
			LOGGER.info("Client is null.");
			return null;
		}else{
			try {
				return (Object[])aClient.execute(operation, params);
			} catch (XmlRpcException e) {
				LOGGER.severe(e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Executes a client operation. This version uses the stored client
	 * @param operation is the operation to execute
	 * @param params are the parameters to the operation
	 * @return an obj array with the results
	 */
	public Object[] execute(String operation, Object[] params){
		if(null == client){
			LOGGER.info("Client is null.");
			return null;
		}else{
			try {
				return (Object[])client.execute(operation, params);
			} catch (XmlRpcException e) {
				LOGGER.severe(e.getMessage());
			}
		}
		return null;
	}
	/**
	 * Routine to query a DB via XML-RPC
	 * @param query is a query to pass to the server to perform
	 * @return a query result 
	 */
	@Override
	public Object[] doQueryXMLRPC(String query) {
	    return execute("shadowpress.query", new Object[]{user,pwrd,query});
	}

}