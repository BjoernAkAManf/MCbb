/**************************************************************************
 * This file is part of MCbb.                                              
 * MCbb is free software: you can redistribute it and/or modify            
 * it under the terms of the GNU General Public License as published by    
 * the Free Software Foundation, either version 3 of the License, or       
 * (at your option) any later version.                                     
 * MCbb is distributed in the hope that it will be useful,                 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           
 * GNU General Public License for more details.                            
 * You should have received a copy of the GNU General Public License       
 * along with MCbb.  If not, see <http://www.gnu.org/licenses/>.           
 *************************************************************************/

package de.javakara.manf.util;

public class parseLang {
	/**
	 * parse the String to add color support
	 * @param p  
	 * @return
	 */
	public static String parseColor(String parseString){
	        if(parseString == null){
	        	return null;
	        }
	        return parseClassicColorCodes(parseString);
	}
	/**
	 * parse the Classic Color Codes starting with & and the ID 1-9 + a-f
	 * @param parseString the String that should be passed
	 * @return parsed String
	 */
	private static String parseClassicColorCodes(String parseString){
		return parseString.replaceAll("&(?<!&&)([0-9a-fA-F])", "\u00A7$1").replace("&&","&");
	}
}
