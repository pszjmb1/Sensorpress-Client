package uk.ac.nottingham.horizon.jmb.sensorpress.client.tests;

import static org.junit.Assert.*;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.logging.Level;

import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpSelectionClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpSelectionClientImpl;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpXMLRpcClient;

public class SpSelectionClientImplTest {

	static String defaultURL = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user = "admin";
	static String pwrd = "qwerty";
	// see rsyncaddon for real values
	static String url2 = "http://...";
	static String user2 = "...";
	static String pwrd2 = "...";
	static String proxy_host = "...";
	static String proxy_port = "...";
	static String lastid = "-1";
	static SpBaseClient base;

	@BeforeClass
	public static void setLastIdValue() {
		base = new SpXMLRpcClient(user, pwrd, Level.INFO, "sensorpress");
		base.simpleClient(defaultURL, true, true);
		String aQuery = 
				"SELECT `reading_id` FROM `sensorpress`." +
				"`hn_sp_reading` ORDER BY `reading_id` " +
				"DESC LIMIT 1";

		Object[] results = base.doQueryXMLRPC(aQuery);

		Assert.assertEquals(1, results.length);
		lastid = results[0].toString().split("[=}]")[1];
		//System.out.println(lastid);
	}
	
	@Test
	public void testSpSelectionClientImpl() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMyClient() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testQuerySelect() {
		String aQuery = "SELECT * FROM `sensorpress`.`hn_sp_readingset_info` ORDER BY `readingset_info_id` DESC LIMIT 10";
		//String aQuery = "SELECT `lastrecord` FROM `shadowpress`.`hn_sp_import` WHERE filename = \"2012-03-22.txt\" AND `hn_sp_deviceinstance_idhn_sp_deviceinstance` = 1 ORDER BY `lastrecord` DESC LIMIT 1";
		Object[] results = base.doQueryXMLRPC(aQuery);
		/*
		 * for(int i = 0; i < results.length; i++){
		 * System.out.println(results[i]); }
		 */

		Assert.assertEquals(1, results.length);
	}

	@Test
	public void testTables() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.tablesFromXMLRPC();
		Assert.assertEquals(14, results.length);
		/*
		 * for(int i = 0; i < results.length; i++){
		 * System.out.println(results[i]); }
		 */
	}

	@Test
	public void testColumns() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.columnsFromXMLRPC("hn_sp_reading");
		Assert.assertEquals(9, results.length);
		for (int i = 0; i < results.length; i++) {
			System.out.println(results[i]);
		}
	}

	@Test
	public void testSelectReadings() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.select("reading", 10);
		Assert.assertEquals(10, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(lastid, outs[8].split("=")[1].replaceAll("}", ""));
	}

	@Test
	public void testSelectReadingSets() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.select("readingset", 10);
		Assert.assertEquals(10, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" readingset_id=13187", outs[1]);
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testSelectReadingtype() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.select(
				"readingtype", 10);
		Assert.assertEquals(9, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" min_value_dec_4_1=-40.0", outs[1]);
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testSelectReadingSet_info() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.select(
				"readingset_info", 10);
		Assert.assertEquals(1, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals("{frq_unit=minutes", outs[0]);
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testSelectDeviceInstance() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.select(
				"deviceinstance", 10);
		Assert.assertEquals(1, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals("{description=Horizon Weather Station 1", outs[0]);
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testSelectRecentBlogPosts() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersectRecentReadingsets() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		SpBaseClient client2 = new SpXMLRpcClient(user2, pwrd2, Level.INFO, "sensorpress");

		if(null == client2.setProxyClient(url2, proxy_host, proxy_port)){
			assert(false);
		}
		Object[] results = sc.intersectRecentReadingsets(base,client2);
		if(null !=results){
			for(int i = 0; i < results.length; i++){
				System.out.println(results[i]);
			}
		}
		//Assert.assertEquals(new Integer(10726), results);
	}

	@Test
	public void testSelectLowestReadingIdForReadingSetTimestamp() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectLatestReadingsetIdForDevice() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Integer results = sc.selectLatestReadingsetIdForDevice(1);
		Assert.assertEquals(new Integer(13187), results);
	}

	@Test
	public void testSelectLatestReadingsetIdForDeviceWithBadData() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Integer results = sc.selectLatestReadingsetIdForDevice(0);
		Assert.assertEquals(new Integer(-1), results);
	}
	
	@Test
	public void testSelectLastImportRecord() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		String results = sc.selectLastImportRecord(1, "2012-03-22.txt");
		Assert.assertEquals("{lastrecord=2012-03-22 23:55:09}", results);
	}

}
