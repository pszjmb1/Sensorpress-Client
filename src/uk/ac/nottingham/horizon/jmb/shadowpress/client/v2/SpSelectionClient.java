/* Interface defining selection client operations Shadowpress.
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

public interface SpSelectionClient extends SpBaseClient {
	/**
	 * Routine to call select statements by XML-RPC
	 * @param type is the type of table to select from
	 * @param limit is the numebr of records to return
	 * @return the resultant rowset
	 */
	public Object[] select(String type, Integer limit);
	/**
	 * Retrieves recent blog posts. 
	 * @return the resultant rowset with the recent blog posts
	 */
	public Object[] selectRecentBlogPosts();
	/**
	 * Select readingsets from url1 that are more recent than those in url2 
	 * @param url1 is a url to select readingsets from
	 * @param user1 is the user for url1
	 * @param pwrd1 is the password for url1
	 * @param url2 is a url to check readingsets against
	 * @param user1 is the user for url2
	 * @param pwrd1 is the password for url2
	 * @return the more recent readingsets
	 */
	public Object[] intersectRecentReadingsets(String url1, String user1,
			String pwrd1, String url2, String user2, String pwrd2);
	/**
	 *  Select reading.id for earliest selected url1.readingset
	 * @return String for the readingid
	 */
	public String selectLowestReadingIdForReadingSetTimestamp();

}
