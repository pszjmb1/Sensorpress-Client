package uk.ac.nottingham.horizon.jmb.shadowpress.client.test;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import uk.ac.nottingham.horizon.jmb.shadowpress.client.SimpleClient;

public class ClientTests {
	String defaultURL = "http://192.168.56.101/wordpress/xmlrpc.php";
	String user = "admin";
	String pwrd = "qwerty";

	@Test
	public void testQuerySelect() {
		SimpleClient sc = new SimpleClient();
		String aQuery = "SELECT * FROM `shadowpress`.`horz_sp_readingset_info` ORDER BY `horz_sp_readingset_info_id` DESC LIMIT 10";
		Object[] results = sc.doQueryXMLRPC("http://192.168.56.101/wordpress/xmlrpc.php", 
				"admin","qwerty",aQuery);						

		Assert.assertEquals(1, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" status=0",outs[1]);
		/*for(int i = 0; i < outs.length; i++){
			System.out.println(outs[i]);
		}*/
	}

	@Test
	public void testSelectReadings() {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC(
				defaultURL,user,pwrd,"reading",10);
		Assert.assertEquals(10, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" idhorz_sp_reading=26109",outs[7]);
	}

	@Test
	public void testSelectReadingSets() {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC(
				defaultURL,user,pwrd,"readingset",10);
		Assert.assertEquals(10, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" readingset_id=2902",outs[1]);
		/*for(int i = 0; i < outs.length; i++){
			System.out.println(outs[i]);
		}*/
	}

	@Test
	public void testSelectReadingtype() {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC(
				defaultURL,user,pwrd,"readingtype",10);
		Assert.assertEquals(9, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals(" min_value_dec_4_1=-40.0",outs[1]);
		/*for(int i = 0; i < outs.length; i++){
			System.out.println(outs[i]);
		}*/
	}

	@Test
	public void testSelectReadingSet_info() {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC(
				defaultURL,user,pwrd,"readingset_info",10);
		Assert.assertEquals(1, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals("{frq_unit=minutes",outs[0]);
		/*for(int i = 0; i < outs.length; i++){
			System.out.println(outs[i]);
		}*/
	}

	@Test
	public void testSelectDeviceInstance() {
		SimpleClient sc = new SimpleClient();
		Object[] results = sc.selectFromXMLRPC(
				defaultURL,user,pwrd,"deviceinstance",10);
		Assert.assertEquals(1, results.length);
		String[] outs = results[0].toString().split(",");
		Assert.assertEquals("{idhorz_sp_deviceinstance=1",outs[0]);
		/*for(int i = 0; i < outs.length; i++){
			System.out.println(outs[i]);
		}*/
	}

}
