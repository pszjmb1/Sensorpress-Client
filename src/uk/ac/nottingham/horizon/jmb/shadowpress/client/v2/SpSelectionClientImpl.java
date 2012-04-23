package uk.ac.nottingham.horizon.jmb.shadowpress.client.v2;

public class SpSelectionClientImpl implements SpSelectionClient {
	SpXMLRpcClient myClient;
	
	public SpSelectionClientImpl(SpXMLRpcClient aClient){
		myClient = aClient;
	}	

	public SpXMLRpcClient geClient() {
		return myClient;
	}

	public void setMyClient(SpXMLRpcClient Client) {
		this.myClient = Client;
	}

	@Override
	public Object[] select(String type, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] selectRecentBlogPosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] intersectRecentReadingsets(String url1, String user1,
			String pwrd1, String url2, String user2, String pwrd2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectLowestReadingIdForReadingSetTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Routine to show Shadowpress tables from XML-RPC
	 * @param url is the URL to the XML-RPC interface 
	 * (such as http://192.168.56.101/wordpress/xmlrpc.php)
	 * @param user is the user name 
	 * @param pwrd is the password
	 * @return the resultant rowset
	 */
	@Override
	public Object[] tablesFromXMLRPC() { 
		return myClient.execute("shadowpress.tables", 
				new Object[]{myClient.getUser(),myClient.getPwrd()});
	}	

	/**
	 * Routine to show Shadowpress table columns from XML-RPC
	 * @param table is the table to display the columns for
	 * @return the resultant rowset
	 */
	@Override
	public Object[] columnsFromXMLRPC(String table) {
		return myClient.execute("shadowpress.columns", 
				new Object[]{myClient.getUser(),myClient.getPwrd(),table});
	}

}
