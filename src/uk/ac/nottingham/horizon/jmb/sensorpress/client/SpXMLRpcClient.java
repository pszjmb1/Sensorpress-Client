/* XML-RPC based implementation of client operations for Sensorpress.
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

package uk.ac.nottingham.horizon.jmb.sensorpress.client;

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
	private final static Logger LOGGER = Logger.getLogger(SpXMLRpcClient.class
			.getName());

	XmlRpcClient client;
	String user;
	String pwrd;
	String dbname;

	/**
	 * Default constructor uses the "sensorpress database"
	 * @param username
	 * @param password
	 * @param level
	 */
	public SpXMLRpcClient(String username, String password, Level level) {
		LOGGER.setLevel(level);
		user = username;
		pwrd = password;
		dbname = "sensorpress";
	}

	public SpXMLRpcClient(String username, String password, Level level, String databaseName) {
		LOGGER.setLevel(level);
		user = username;
		pwrd = password;
		dbname = databaseName;
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

	public String getDbName() {
		return dbname;
	}

	public void setDbName(String DbName) {
		this.dbname = DbName;
	}
	
	

	/**
	 * Retrieves a configured XmlRpcClient client.
	 * 
	 * @param url
	 *            is the URL to the XML-RPC interface (such as
	 *            http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param reset
	 *            toggles whether or not to make a new connection or use an
	 *            existing one
	 * @param store
	 *            toggles whether or not to store this connection
	 * @return a configured XmlRpcClient client or null if an error occured.
	 */
	@Override
	public XmlRpcClient simpleClient(String url, boolean reset, boolean store) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		XmlRpcClient aClient = null;
		if (!reset && null != client) {
			return client;
		}
		try {
			config.setServerURL(new URL(url));
			aClient = new XmlRpcClient();
			aClient.setConfig(config);
			if (store) {
				client = aClient;
			}
		} catch (MalformedURLException e) {
			LOGGER.severe(e.getMessage());
		}
		return aClient;
	}

	@Override
	public XmlRpcClient setProxyClient(String url, 
			String proxy_host, String proxy_port) {
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(url));

			XmlRpcClient aClient = new XmlRpcClient();
			XmlRpcCommonsTransportFactory transportFactory = new XmlRpcCommonsTransportFactory(
					aClient);
			aClient.setTransportFactory(transportFactory);
			HttpClient httpClient = new HttpClient();
			httpClient.getHostConfiguration().setProxy(proxy_host,
					Integer.valueOf(proxy_port));

			transportFactory.setHttpClient(httpClient);
			aClient.setTransportFactory(transportFactory);
			aClient.setConfig(config);
			client = aClient;
			return client;
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
			logStackTrace(e);
		}
		return null;
	}

	/**
	 * Setsup an httpClient for use through a proxy server
	 * 
	 * @param proxy_host
	 * @param proxy_port
	 * @return the proxy enabled httpClient
	 */
	public HttpClient setupProxy(String proxy_host, String proxy_port) {
		HttpClient httpClient;
		httpClient = new HttpClient();
		httpClient.getHostConfiguration().setProxy(proxy_host,
				new Integer(proxy_port));
		return httpClient;
	}

	/**
	 * Retrieves a configured XmlRpcClient client using more complex mechanisms
	 * such as proxy servers.
	 * 
	 * @param url
	 *            is the URL to the XML-RPC interface (such as
	 *            http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param reset
	 *            toggles whether or not to make a new connection or use an
	 *            existing one
	 * @param store
	 *            toggles whether or not to store this connection
	 * @param httpClient
	 *            the HTTP accessor supporting more complex interactions
	 * @return a configured XmlRpcClient client or null if an error occured.
	 */
	@Override
	public XmlRpcClient getComplexClient(String url, boolean reset,
			boolean store, HttpClient httpClient) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		XmlRpcClient aClient = null;
		if (!reset && null != client) {
			return client;
		}
		try {
			config.setServerURL(new URL(url));
			aClient = new XmlRpcClient();
			XmlRpcCommonsTransportFactory transportFactory = new XmlRpcCommonsTransportFactory(
					client);
			aClient.setTransportFactory(transportFactory);

			transportFactory.setHttpClient(httpClient);
			aClient.setTransportFactory(transportFactory);
			aClient.setConfig(config);

			if (store) {
				client = aClient;
			}
		} catch (MalformedURLException e) {
			LOGGER.severe(e.getMessage());
		}
		return aClient;
	}

	/**
	 * Stores user details for accessing a Sensorpress instance
	 * 
	 * @param username
	 * @param password
	 */
	public void setUserDetails(String username, String password) {
		username = user;
		pwrd = password;
	}

	public void logStackTrace(Exception e) {
		StackTraceElement[] ste = e.getStackTrace();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ste.length; i++) {
			sb.append(ste[i].toString());
			sb.append("\n");
		}
		LOGGER.severe(sb.toString());
	}

	/**
	 * Executes a client operation.
	 * 
	 * @param aClient
	 *            is the client to perform the operation
	 * @param operation
	 *            is the operation to execute
	 * @param params
	 *            are the parameters to the operation
	 * @return an obj array with the results
	 */
	public Object[] execute(XmlRpcClient aClient, String operation,
			Object[] params) {
		if (null == aClient) {
			LOGGER.info("Client is null.");
			return null;
		} else {
			try {
				return (Object[]) aClient.execute(operation, params);
			} catch (XmlRpcException e) {
				LOGGER.severe(e.getMessage());
				logStackTrace(e);
			}
		}
		return null;
	}

	/**
	 * Executes a client operation. This version uses the stored client
	 * 
	 * @param operation
	 *            is the operation to execute
	 * @param params
	 *            are the parameters to the operation
	 * @return an obj array with the results
	 */
	public Object[] execute(String operation, Object[] params) {
		if (null == client) {
			LOGGER.info("Client is null.");
			return null;
		} else {
			try {
				/*System.out.println(operation);
				for(int i = 0; i < params.length; i ++){

					System.out.println(params[i]);
				}*/
				return (Object[]) client.execute(operation, params);
			} catch (Exception e) {
				LOGGER.severe(e.getMessage());
				logStackTrace(e);
			}
		}
		return null;
	}

	/**
	 * Routine to query a DB via XML-RPC
	 * 
	 * @param query
	 *            is a query to pass to the server to perform
	 * @return a query result
	 */
	@Override
	public Object[] doQueryXMLRPC(String query) {
		LOGGER.info(dbname+".query: " + user + ":"+ pwrd + ":"+ query);
		return execute(dbname+".query", new Object[] { user, pwrd, query });
	}

}
