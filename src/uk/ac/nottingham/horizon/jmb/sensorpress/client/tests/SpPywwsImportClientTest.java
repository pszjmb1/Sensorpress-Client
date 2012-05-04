package uk.ac.nottingham.horizon.jmb.sensorpress.client.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpBaseClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpPywwsImportClient;
import uk.ac.nottingham.horizon.jmb.sensorpress.client.SpXMLRpcClient;

public class SpPywwsImportClientTest {
	static String defaultURL = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user = "admin";
	static String pwrd = "qwerty";
	static String url2 = "http://192.168.56.101/wordpress/xmlrpc.php";
	static String user2 = "admin";
	static String pwrd2 = "qwerty";
	static String lastid = "-1";
	static SpBaseClient base;

	@BeforeClass
	public static void setLastIdValue() {
		base = new SpXMLRpcClient(user, pwrd, Level.INFO);
		base.simpleClient(defaultURL, true, true);
		String aQuery = "SELECT `idhorz_sp_reading` FROM `shadowpress`."
				+ "`horz_sp_reading` ORDER BY `idhorz_sp_reading` "
				+ "DESC LIMIT 1";

		Object[] results = base.doQueryXMLRPC(aQuery);

		Assert.assertEquals(1, results.length);
		lastid = results[0].toString().split("[=}]")[1];
		System.out.println(lastid);
	}

	@Test
	public void testSpPywwsImportClient() {
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
	public void testGetTimeThreshold() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTimeThreshold() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetlastrecord() {
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);
		String result = sc.getlastrecord(999999999, "b.txt");
		System.out.println(result);
		Assert.assertEquals("2012-03-12 11:37:27", result);

	}

	@Test
	public void testGetStartingLine() {
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);
		List<String> lines = new ArrayList<String>();
		lines.add("2012-04-02 00:04:09,5,35,22.6,85,5.3,1011.1,0.3,0.7,12,33.0,0");
		lines.add("2012-04-02 00:09:09,5,35,22.6,85,5.2,1011.0,0.3,1.0,10,33.0,0");
		lines.add("2012-04-02 00:14:09,5,35,22.6,85,5.2,1011.0,0,0.3,10,33.0,0");
		lines.add("2012-04-02 00:19:09,5,35,22.6,85,5.1,1010.8,0,0.3,14,33.0,0");
		String timestamp = "00:13:09";
		Integer i = sc.getStartingLine(lines, timestamp);
		System.out.println(i);
	}

	@Test
	public void testBuildInsertionStrings() {
		fail("Not yet implemented");
	}
	@Test
	public void testFileNeedsImporting(){
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);	
		String answer = sc.checkFileImport(1, "2012-04-25.txt");
		Assert.assertEquals("2012-04-25 13:34:32", answer);	
	}
	@Test
	public void testFileNeedsImportingNullCase(){
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);	
		String answer = sc.checkFileImport(1, "2012-04-24.txt");
		Assert.assertEquals(null, answer);	
	}
	@Test
	public void testFileNeedsImportingBlankCase(){
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);	
		String answer = sc.checkFileImport(1, "2012-04-27.txt");
		Assert.assertEquals("", answer);	
	}


	@Test
	public void testImportCsv() {
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);
		Object[] results = sc.importCsv("/home/pszjmb/experiments/sp",
				"2012-04-25.txt", 1, 1);
		if (null != results) {
			for (int i = 0; i < results.length; i++) {
				System.out.println(results[i]);
			}
		}
	}

	@Test
	public void testImportDirectory() {
		SpPywwsImportClient sc = new SpPywwsImportClient(base, Level.INFO);
		sc.importDirectory("/home/pszjmb/experiments/sp",
				1, 1);
	}

}
