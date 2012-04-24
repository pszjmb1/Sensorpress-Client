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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpPywwsImportClient implements SpCsvImportClient {
	SpBaseClient myClient;
	String timeThreshold = "23:55:00"; // presumes a five minute interval
										// between readings

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
	public String getlastrecord(String filename) {
		// TODO COMPLETE THIS!
		return null;
	}

	/**
	 * Compares two timestamps and returns -1, 0 or 1 depending on whether the 
	 * first one is lessthan, equalto, or greater than the second.
	 * @param timestamp1
	 * @param timestamp2
	 * @return- 1, 0 or 1 depending on whether the 
	 * first one is lessthan, equalto, or greater than the second.
	 */
	public Integer timestampComparison(String timestamp1,String timestamp2) {
		// TODO COMPLETE THIS!
		return -1;
	}

	public Integer getnextReadingSetId() {
		// TODO COMPLETE THIS!
		return null;
	}

	public Integer getStartingLine(List<String> lines, String timestamp){
		// TODO COMPLETE THIS!
		return null;
	}
	
	public String insertValues(String line, String readingSetInsert, String insertstringInt, String insertstringDec){
		Integer hum_in = 1, temp_in = 2, hum_out = 3, temp_out = 4, 
				abs_pressure = 5, wind_ave = 6, wind_gust = 7, 
				wind_dir = 8, rain = 9;
		
		String timestamp = null;
		/*
		 * timestamp = row.pop(0)
readingSetInsert = readingSetInsert + "(" + str(readingSetId) + ", '" + row.pop(0) + "'," + str(horz_sp_deviceinstance_idhorz_sp_deviceinstance) + "," + str(horz_sp_readingset_info_horz_sp_readingset_info_id) + "),"
                    #Add readings
                    # The following magic numbers are the db ids for the corresponding idhorz_sp_reading_type 
                    insertstringInt = insertstringInt + row[hum_in] + ", " + str(readingSetId) + ",2),("
                    insertstringInt = insertstringInt + row[hum_out] + ", " + str(readingSetId) + ",4),("
                    insertstringInt = insertstringInt + row[wind_dir] + ", " + str(readingSetId) + ",7),("
                    insertstringDec = insertstringDec + row[temp_in] + ", " + str(readingSetId) + ",3),("
                    insertstringDec = insertstringDec + row[temp_out] + ", " + str(readingSetId) + ",1),("
                    insertstringDec = insertstringDec + row[abs_pressure] + ", " + str(readingSetId) + ",5),("
                    insertstringDec = insertstringDec + row[wind_ave] + ", " + str(readingSetId) + ",6),("
                    insertstringDec = insertstringDec + row[wind_gust] + ", " + str(readingSetId) + ",8),("
                    insertstringDec = insertstringDec + row[rain] + ", " + str(readingSetId) + ",9),("
                    readingSetId = readingSetId + 1
		 */
		return timestamp;
	}
	
	public Object[] insertImportRecord(
			String filename, Integer deviceId, String timestamp){
		// todo complete!
		return null;
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
	 * @return the query result
	 */
	@Override
	public Object[] importCsv(String directory, String filename,
			Integer deviceId) {
		// Ensure that file hasn't been imported already
		String lastrecord = getlastrecord(filename);
		if (!(null == lastrecord)) {
			// If file has been completely imported ignore it
			if (0 >= timestampComparison(lastrecord,timeThreshold)) {
				return null;
			}
		}
		
		Integer readingSetId = getnextReadingSetId();
		Integer horz_sp_deviceinstance_idhorz_sp_deviceinstance = 1, 
				horz_sp_readingset_info_horz_sp_readingset_info_id = 1;
		String readingSetInsert =
				"INSERT IGNORE INTO horz_sp_readingset " +
				"(`readingset_id`, `timestamp`, " +
				"`horz_sp_deviceinstance_idhorz_sp_deviceinstance`, " +
				"`horz_sp_readingset_info_horz_sp_readingset_info_id`)" +
				" VALUES ";
		String insertstringDec = 
				"INSERT IGNORE INTO `horz_sp_reading`" +
				"(`value_dec_8_2`,`horz_sp_readingset_readingset_id`, " +
				"`horz_sp_reading_type_idhorz_sp_reading_type`) VALUES (";
		String insertstringInt = 
				"INSERT IGNORE INTO `horz_sp_reading`" +
				"(`value_int`, `horz_sp_readingset_readingset_id`," +
				" `horz_sp_reading_type_idhorz_sp_reading_type`) VALUES (";
		List<String> lines = readFile(directory + File.separator + filename);
		Integer startingLine = 0;
		if (!(null == lastrecord)){
			startingLine = getStartingLine(lines,lastrecord);
		}
		String timestamp = null;
		for(int i = startingLine; i < lines.size(); i++){
			timestamp = insertValues(lines.get(i), readingSetInsert, 
					insertstringInt, insertstringDec);
		}
		readingSetInsert.substring(0, readingSetInsert.length()-1);
        insertstringInt = insertstringInt.substring(0, insertstringInt.length()-2);
        insertstringDec = insertstringDec.substring(0, insertstringDec.length()-2);
        insertImportRecord(filename,deviceId, timestamp);
		
		return null;
	}

}
