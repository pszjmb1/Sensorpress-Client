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
	 * Checks a list of Pywws strings to see which line index is greater than the given timestamp 
	 * @param lines is a list of pywws csv content
	 * @param timestamp is in the format "hh:mm:ss"
	 * @return the index to begin copying from
	 */
	public Integer getStartingLine(List<String> lines, String timestamp) {
		Iterator<String> it = lines.iterator();
		String line;
		int i = 0;
		while(it.hasNext()){
			line = it.next();
			String[] parts = line.split(" |,");
			if(0 > timestamp.compareTo(parts[1])){
				return i; 
			}
			i++;
		}
		return null;
	}

	public String insertValues(String line, String readingSetInsert,
			String insertstringInt, String insertstringDec) {
		Integer hum_in = 1, temp_in = 2, hum_out = 3, temp_out = 4, abs_pressure = 5, wind_ave = 6, wind_gust = 7, wind_dir = 8, rain = 9;

		String timestamp = null;
		/*
		 * timestamp = row.pop(0) readingSetInsert = readingSetInsert + "(" +
		 * str(readingSetId) + ", '" + row.pop(0) + "'," +
		 * str(horz_sp_deviceinstance_idhorz_sp_deviceinstance) + "," +
		 * str(horz_sp_readingset_info_horz_sp_readingset_info_id) + ")," #Add
		 * readings # The following magic numbers are the db ids for the
		 * corresponding idhorz_sp_reading_type insertstringInt =
		 * insertstringInt + row[hum_in] + ", " + str(readingSetId) + ",2),("
		 * insertstringInt = insertstringInt + row[hum_out] + ", " +
		 * str(readingSetId) + ",4),(" insertstringInt = insertstringInt +
		 * row[wind_dir] + ", " + str(readingSetId) + ",7),(" insertstringDec =
		 * insertstringDec + row[temp_in] + ", " + str(readingSetId) + ",3),("
		 * insertstringDec = insertstringDec + row[temp_out] + ", " +
		 * str(readingSetId) + ",1),(" insertstringDec = insertstringDec +
		 * row[abs_pressure] + ", " + str(readingSetId) + ",5),("
		 * insertstringDec = insertstringDec + row[wind_ave] + ", " +
		 * str(readingSetId) + ",6),(" insertstringDec = insertstringDec +
		 * row[wind_gust] + ", " + str(readingSetId) + ",8),(" insertstringDec =
		 * insertstringDec + row[rain] + ", " + str(readingSetId) + ",9),("
		 * readingSetId = readingSetId + 1
		 */
		return timestamp;
	}

	public Object[] insertImportRecord(String filename, Integer deviceId,
			String timestamp) {
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
	 * @param horz_sp_readingset_info_id
	 *            is the readingset_info_id to insert for the records
	 * @return the query result
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
		String readingSetInsert = "INSERT IGNORE INTO horz_sp_readingset "
				+ "(`readingset_id`, `timestamp`, "
				+ "`horz_sp_deviceinstance_idhorz_sp_deviceinstance`, "
				+ "`horz_sp_readingset_info_horz_sp_readingset_info_id`)"
				+ " VALUES ";
		String insertstringDec = "INSERT IGNORE INTO `horz_sp_reading`"
				+ "(`value_dec_8_2`,`horz_sp_readingset_readingset_id`, "
				+ "`horz_sp_reading_type_idhorz_sp_reading_type`) VALUES (";
		String insertstringInt = "INSERT IGNORE INTO `horz_sp_reading`"
				+ "(`value_int`, `horz_sp_readingset_readingset_id`,"
				+ " `horz_sp_reading_type_idhorz_sp_reading_type`) VALUES (";
		List<String> lines = readFile(directory + File.separator + filename);
		Integer startingLine = 0;
		if (!(null == lastrecord)) {
			startingLine = getStartingLine(lines, lastrecord);
		}
		String timestamp = null;
		for (int i = startingLine; i < lines.size(); i++) {
			timestamp = insertValues(lines.get(i), readingSetInsert,
					insertstringInt, insertstringDec);
		}
		readingSetInsert.substring(0, readingSetInsert.length() - 1);
		insertstringInt = insertstringInt.substring(0,
				insertstringInt.length() - 2);
		insertstringDec = insertstringDec.substring(0,
				insertstringDec.length() - 2);
		insertImportRecord(filename, deviceId, timestamp);

		return null;
	}

}
