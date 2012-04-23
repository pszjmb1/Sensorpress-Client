/* Interface defining basic client operations Shadowpress.
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

package uk.ac.nottingham.horizon.jmb.shadowpress.client.v2;
import org.apache.commons.httpclient.HttpClient;
import org.apache.xmlrpc.client.XmlRpcClient;

public interface SpBaseClient {
	/**
	 * Retrieves a configured XmlRpcClient client.   
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param reset toggles whether or not to make a new connection or use an existing one
	 * @param store toggles whether or not to store this connection
	 * @return a configured XmlRpcClient client.
	 */
	public XmlRpcClient getSimpleClient(String url, boolean reset, boolean store);	
	/**
	 * Retrieves a configured XmlRpcClient client using more complex mechanisms such as proxy servers.   
	 * @param url is the URL to the XML-RPC interface (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param httpClient the HTTP accessor supporting more complex interactions
	 * @return a configured XmlRpcClient client.
	 */
	public XmlRpcClient getComplexClient(String url,boolean reset, boolean store,HttpClient httpClient);	
	/**
	 * Routine to query a DB via XML-RPC
	 * @param query is a query to pass to the server to perform
	 * @return a query result 
	 */
	public Object[] doQueryXMLRPC(String query);
	/**
	 * Routine to show Shadowpress tables from XML-RPC
	 * @return the resultant rowset
	 */
	public Object[] tablesFromXMLRPC();
	/**
	 * Routine to show Shadowpress table columns from XML-RPC
	 * @param table is the table to display the columns for
	 * @return the resultant rowset
	 */
	public Object[] columnsFromXMLRPC(String table);	
	

	
}
	