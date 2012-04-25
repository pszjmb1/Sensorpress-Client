package uk.ac.nottingham.horizon.jmb.shadowpress.client.tests;

import static org.junit.Assert.*;

import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpInsertionClient;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpInsertionClientImpl;
import uk.ac.nottingham.horizon.jmb.shadowpress.client.SpXMLRpcClient;

public class SpInsertionClientImplTest {
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
	public void testSpInsertionClientImpl() {
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
	public void testInsertReading_Dec_4_1() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertReading("dec_4_1", "999.9", 9999, 9999);
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
		Object[] result = sc.insertReading("dec_5_2", "999.99", 9999, 9999);
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
		Object[] result = sc.insertReading("dec_8_2", "99999.99", 9999, 9999);
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
		Object[] result = sc.insertReading("dec_12_6", "99999.999999", 9999,
				9999);
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
		Object[] result = sc.insertReading("blob", "999.9", 9999, 9999);
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
		Object[] result = sc.insertReading("int", "9999", 9999, 9999);
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
		Object[] result = sc.insertReading("BADVALUE", "9999", 9999, 9999);
		Assert.assertEquals(null, result);
	}


	@Test
	public void testInsertImportRecord() {
		SpInsertionClient sc = new SpInsertionClientImpl(base, Level.INFO);
		Object[] result = sc.insertImportRecord(
				"a.txt", 9999, "2012-04-02 00:04:09");
		Assert.assertEquals("{lastrecord=2012-04-02 00:04:09}", result[0].toString());
	}
	
	


	@Test
	public void testInsertRecordsIntoReadingsets() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertRecentReadings() {
		fail("Not yet implemented");
	}

}
