package uk.ac.nottingham.horizon.jmb.shadowpress.client.tests;

import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpInsertionClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpInsertionClientImpl;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpReplicationClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpReplicationClientImpl;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpSelectionClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpSelectionClientImpl;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpXMLRpcClient;

public class ClientTests {
	static String defaultURL = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user = "admin";
	static String pwrd = "qwerty";
	static String url2 = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user2 = "admin";
	static String pwrd2 = "qwerty";
	static String lastid = "-1";
	static SpBaseClient base;

	@BeforeClass
	public static void testQuerySelect2() {
		base = new SpXMLRpcClient(user, pwrd, Level.INFO);
		base.simpleClient(defaultURL, true, true);
		String aQuery = "SELECT `idhorz_sp_reading` FROM `shadowpress`.`horz_sp_reading` ORDER BY `idhorz_sp_reading` DESC LIMIT 1";

		Object[] results = base.doQueryXMLRPC(aQuery);

		Assert.assertEquals(1, results.length);
		lastid = results[0].toString().split("[=}]")[1];
		System.out.println(lastid);
	}

	@Test
	public void testQuerySelect() {
		String aQuery = "SELECT * FROM `shadowpress`.`horz_sp_readingset_info` ORDER BY `horz_sp_readingset_info_id` DESC LIMIT 10";
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
		Assert.assertEquals(13, results.length);
		/*
		 * for(int i = 0; i < results.length; i++){
		 * System.out.println(results[i]); }
		 */
	}

	@Test
	public void testColumns() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.columnsFromXMLRPC("horz_sp_reading");
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
		Assert.assertEquals(lastid, outs[7].split("=")[1]);
	}

	@Test
	public void testSelectReadingSets() {
		SpSelectionClient sc = new SpSelectionClientImpl(base, Level.INFO);
		Object[] results = sc.select("readingset", 10);
		Assert.assertEquals(10, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" readingset_id=10726", outs[1]);
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
		Assert.assertEquals("{idhorz_sp_deviceinstance=1", outs[0]);
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testInsertReading_Dec_4_1() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"dec_4_1", "999.9", 9999, 9999);
		Assert.assertEquals(1, result.length);
		String[] outs = result[0].toString().split(",");
		int exp = Integer.valueOf(lastid) + 1;
		int res = Integer.valueOf(outs[0].split("[=}]")[1]);
		Assert.assertEquals(exp, res);
		lastid = "" + res;
	}

	@Test
	public void testInsertReading_Dec_5_2() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"dec_5_2", "999.99", 9999, 9999);
		Assert.assertEquals(1, result.length);
		String[] outs = result[0].toString().split(",");
		int exp = Integer.valueOf(lastid) + 1;
		int res = Integer.valueOf(outs[0].split("[=}]")[1]);
		Assert.assertEquals(exp, res);
		lastid = "" + res;
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testInsertReading_Dec_8_2() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"dec_8_2", "99999.99", 9999, 9999);
		Assert.assertEquals(1, result.length);
		String[] outs = result[0].toString().split(",");
		int exp = Integer.valueOf(lastid) + 1;
		int res = Integer.valueOf(outs[0].split("[=}]")[1]);
		Assert.assertEquals(exp, res);
		lastid = "" + res;
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testInsertReading_Dec_12_6() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"dec_12_6", "99999.999999", 9999, 9999);
		Assert.assertEquals(1, result.length);
		String[] outs = result[0].toString().split(",");
		int exp = Integer.valueOf(lastid) + 1;
		int res = Integer.valueOf(outs[0].split("[=}]")[1]);
		Assert.assertEquals(exp, res);
		lastid = "" + res;
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testInsertReading_blob() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"blob", "999.9", 9999, 9999);
		Assert.assertEquals(1, result.length);
		String[] outs = result[0].toString().split(",");
		int exp = Integer.valueOf(lastid) + 1;
		int res = Integer.valueOf(outs[0].split("[=}]")[1]);
		Assert.assertEquals(exp, res);
		lastid = "" + res;
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void testInsertReading_int() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"int", "9999", 9999, 9999);
		Assert.assertEquals(1, result.length);
		String[] outs = result[0].toString().split(",");
		int exp = Integer.valueOf(lastid) + 1;
		int res = Integer.valueOf(outs[0].split("[=}]")[1]);
		Assert.assertEquals(exp, res);
		lastid = "" + res;
		/*
		 * for(int i = 0; i < outs.length; i++){ System.out.println(outs[i]); }
		 */
	}

	@Test
	public void failuretestInsertReading_BADVALUE() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading(
				"BADVALUE", "9999", 9999, 9999);
		Assert.assertEquals(null, result);
	}

	/**
	 * Note that this may be a long running test depending on content in the databases.
	 */
	@Test
	public void testReplicate() {
		SpReplicationClient replicator = new SpReplicationClientImpl(Level.INFO);
		SpBaseClient client1 = new SpXMLRpcClient(user, pwrd, Level.INFO);
		client1.simpleClient(defaultURL, true, true);
		SpBaseClient client2 = new SpXMLRpcClient(user2, pwrd2, Level.INFO);
		client2.simpleClient(url2, true, true);
		replicator.replicate(client1, client2);
	}

}
