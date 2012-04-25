/* Pywws data CSV import client for Shadowpress.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpPywwsImportClient implements SpCsvImportClient {
	SpBaseClient myClient;
	String timeThreshold = "23:55:00"; // presumes a five minute interval
										// between readings
	String readingSetInsert = "", insertstringInt = "", insertstringDec= "";
	Integer readingSetId=-1;

	private final static Logger LOGGER = Logger
			.getLogger(SpPywwsImportClient.class.getName());

	public SpPywwsImportClient(SpBaseClient aClient, Level aLevel) {
		myClient = aClient;
		LOGGER.setLevel(aLevel);
	}

	public SpBaseClient getClient() {
		return myClient;
	}

	public void setMyClient(SpBaseClient Client) {
		this.myClient = Client;
	}

	public String getTimeThreshold() {
		return timeThreshold;
	}

	public void setTimeThreshold(String timeThreshold) {
		this.timeThreshold = timeThreshold;
	}

	/**
	 * Reads a file into a list of strings (one per row)
	 * 
	 * @param filepath
	 * @return the file as a list of strings
	 */
	public List<String> readFile(String filepath) {
		List<String> lines = new ArrayList<String>();
		String aLine = "";
		FileReader aFile;
		try {
			aFile = new FileReader(filepath);
			BufferedReader reader = new BufferedReader(aFile);
			while ((aLine = reader.readLine()) != null) {
				lines.add(aLine);
			}
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return lines;
	}

	/**
	 * Checks Sp DB to see if this file's been imported. If so the
	 * horz_sp_import.lastrecord value is returned
	 * 
	 * @param filename
	 * @return horz_sp_import.lastrecord or null if file has not been imported
	 */
	public String getlastrecord(Integer device, String filename) {
		Object[] result = new SpSelectionClientImpl(myClient, LOGGER.getLevel())
				.selectImportLastRecord(device, filename);
		if (result.length == 1) {
			return result[0].toString().replace("{lastrecord=", "")
					.replace("}", "");
		} else {
			return null;
		}
	}

	/**
	 * Checks a list of Pywws strings to see which line index is greater than
	 * the given timestamp
	 * 
	 * @param lines
	 *            is a list of pywws csv content
	 * @param timestamp
	 *            is in the format "hh:mm:ss"
	 * @return the index to begin copying from
	 */
	public Integer getStartingLine(List<String> lines, String timestamp) {
		Iterator<String> it = lines.iterator();
		String line;
		int i = 0;
		while (it.hasNext()) {
			line = it.next();
			String[] parts = line.split(" |,");
			if (0 > timestamp.compareTo(parts[1])) {
				return i;
			}
			i++;
		}
		return null;
	}
	
	private String getValue(String aVal){
		if(null == aVal || "".equals(aVal)){
			return "null";
		}else{
			return aVal;
		}
	}

	/**
	 * Adds content to Strings for dataabase insertion  
	 * @param csvRow is a row from a CSV file with Pywws content in the format:
	 * 2012-04-18 00:28:42,5,33,22.4,85,5.1,983.4,1.0,2.0,10,77.4,0 
	 * @param readingSetInsert is the insertion String for reading sets
	 * @param insertstringInt is the insertion String for horz_sp_reading.value_int
	 * @param insertstringDec is the insertion String for horz_sp_reading.value_dec_8_2
	 * @param readingSetId is self explanatory 
	 * @param deviceInstanceId is self explanatory
	 * @param readingset_info_id is self explanatory
	 * @return the timestamp value
	 */
	public String buildInsertionStrings(String csvRow, 
			Integer deviceInstanceId, Integer readingset_info_id) {
		Integer hum_in = 2, temp_in = 3, hum_out = 4, temp_out = 5, abs_pressure = 6, wind_ave = 7, wind_gust = 8, wind_dir = 9, rain = 10;

		String[] values = csvRow.split(",");
		/*for(int i = 0; i < values.length; i++){
			System.out.println(i+": " +values[i]);
		}*/
		String timestamp = values[0];
		readingSetInsert = readingSetInsert + "(" + readingSetId + ", '"
				+ timestamp + "'," + deviceInstanceId + ","
				+ readingset_info_id + "),";
		insertstringInt = insertstringInt + getValue(values[hum_in]) + ", "
				+ readingSetId + ",2),(";
		insertstringInt = insertstringInt + getValue(values[hum_out]) + ", "
				+ readingSetId + ",4),(";
		insertstringInt = insertstringInt + getValue(values[wind_dir]) + ", "
				+ readingSetId + ",7),(";
		insertstringDec = insertstringDec + getValue(values[temp_in]) + 
				", " + readingSetId + ",3),(";
        insertstringDec = insertstringDec + getValue(values[temp_out]) + 
        		", " + readingSetId + ",1),(";
        insertstringDec = insertstringDec + getValue(values[abs_pressure]) + 
        		", " + readingSetId + ",5),(";
        insertstringDec = insertstringDec + getValue(values[wind_ave]) + 
        		", " + readingSetId + ",6),(";
        insertstringDec = insertstringDec + getValue(values[wind_gust]) + 
        		", " + readingSetId + ",8),(";
        insertstringDec = insertstringDec + getValue(values[rain]) + 
        		", " + readingSetId + ",9),(";
        readingSetId = readingSetId + 1;	    

		return timestamp;
	}
	
	private boolean doInsertions(){
		Object[] results = myClient.doQueryXMLRPC(readingSetInsert);
		if(null==results){
			return false;
		}	
		//System.out.println(insertstringInt);
		results = myClient.doQueryXMLRPC(insertstringInt);
		if(null==results){
			return false;
		}
		results = myClient.doQueryXMLRPC(insertstringDec);
		if(null==results){
			return false;
		}
		return true;
	}

	/**
	 * Import a file with readings into SP Each row resembles this: 2012-04-18
	 * 00:03:42,5,33,22.5,85,5.2,983.7,1.4,2.0,4,77.4,0
	 * 
	 * @param directory
	 *            is the full directory path containing the file to import
	 * @param filename
	 *            is the name of the file to import
	 * @param deviceId
	 *            is the id of the device whose values are imported for
	 * @param horz_sp_readingset_info_id
	 *            is the readingset_info_id to insert for the records
	 * @return the query result or null if an error occurred
	 */
	@Override
	public Object[] importCsv(String directory, String filename,
			Integer deviceId, Integer horz_sp_readingset_info_id) {
		// Ensure that file hasn't been imported already
		String lastrecord = getlastrecord(deviceId, filename);
		if (!(null == lastrecord)) {
			// If file has been completely imported ignore it
			if (0 >= lastrecord.compareTo(timeThreshold)) {
				return null;
			}
		}
		readingSetInsert = "INSERT IGNORE INTO horz_sp_readingset "
				+ "(`readingset_id`, `timestamp`, "
				+ "`horz_sp_deviceinstance_idhorz_sp_deviceinstance`, "
				+ "`horz_sp_readingset_info_horz_sp_readingset_info_id`)"
				+ " VALUES ";
		insertstringDec = "INSERT IGNORE INTO `horz_sp_reading`"
				+ "(`value_dec_8_2`,`horz_sp_readingset_readingset_id`, "
				+ "`horz_sp_reading_type_idhorz_sp_reading_type`) VALUES (";
		insertstringInt = "INSERT IGNORE INTO `horz_sp_reading`"
				+ "(`value_int`, `horz_sp_readingset_readingset_id`,"
				+ " `horz_sp_reading_type_idhorz_sp_reading_type`) VALUES (";
		if(!directory.endsWith(File.separator)){
			directory  = directory + File.separator;
		}
		LOGGER.log(Level.INFO, "Importing: "+directory+filename);
		List<String> lines = readFile(directory + filename);
		if(lines.isEmpty()){
			LOGGER.log(Level.SEVERE, "No CSV rows found. Aborting import process.");
			return null;
		}
		Integer startingLine = 0;
		if (!(null == lastrecord)) {
			startingLine = getStartingLine(lines, lastrecord);
		}
		String timestamp = null;
		//TODO - Make a db transaction from nextReadingsetid to insertImportRecord
		readingSetId = new SpSelectionClientImpl(
				myClient, LOGGER.getLevel()).
				selectLatestReadingsetIdForDevice(deviceId) + 1;
		for (int i = startingLine; i < lines.size(); i++) {
			timestamp = buildInsertionStrings(lines.get(i), 
					deviceId, horz_sp_readingset_info_id );
		}
		readingSetInsert = readingSetInsert.substring(0, readingSetInsert.length() - 1);
		insertstringInt = insertstringInt.substring(0,
				insertstringInt.length() - 2);
		insertstringDec = insertstringDec.substring(0,
				insertstringDec.length() - 2);
		if(doInsertions()){
			return new SpInsertionClientImpl(myClient, Level.INFO).
					insertImportRecord(filename, deviceId, timestamp);
		}else{
			return null;
		}
	}
	


	/**
	 * Recursively imports all pywws files in a given directory
	 * 
	 * @param directory
	 *            is the full directory path containing the file to import
	 * @param deviceId
	 *            is the id of the device whose values are imported for
	 * @param horz_sp_readingset_info_id
	 *            is the readingset_info_id to insert for the records
	 * @return the query result or null if an error occurred
	 */
	@Override
	public void importDirectory(String directory,
			Integer deviceId, Integer horz_sp_readingset_info_id) {
		File dir = new File(directory);
		File allFiles[] = dir.listFiles();
		for (File aFile : allFiles) {
			if(aFile.isDirectory()){
				importDirectory(aFile.getAbsolutePath(), deviceId, horz_sp_readingset_info_id);
			}else{
				importCsv(directory, aFile.getName(),
						deviceId, horz_sp_readingset_info_id);
			}				
		}
	}
}
