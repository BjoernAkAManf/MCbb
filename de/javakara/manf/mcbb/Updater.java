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

package de.javakara.manf.mcbb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Updater {
	public static void download(MCbb f){
		//Not Implemented
	}
	
	public static boolean newUpdate(MCbb v,boolean o) {
		StringBuilder x = new StringBuilder();
		try {
			URL yahooURL = new URL(MCbb.lang.get("System.update.location"));
			Scanner in = new Scanner(yahooURL.openStream());
			if (in.hasNextLine()) {
				String line = in.nextLine();
				x.append(line);
			}
			in.close();
			if(o){
				System.out.println(MCbb.lang.get("System.update.version"));
				System.out.println(MCbb.lang.get("System.update.currversion") + v.version.toString());
				System.out.println(MCbb.lang.get("System.update.latestversion") + x.toString());
			}
		} catch (MalformedURLException me) {
			System.err.println(me);

		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		if(v.version.toString().equals(x.toString()))
			return false;
		else
			return true;
	}
	public static String getVersion() {
		StringBuilder x = new StringBuilder();
		try {
			URL yahooURL = new URL(MCbb.lang.get("System.update.location"));
			Scanner in = new Scanner(yahooURL.openStream());
			if (in.hasNextLine()) {
				String line = in.nextLine();
				x.append(line);
			}
			in.close();
			return x.toString();
		} catch (MalformedURLException me) {
			System.err.println(me);

		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		return null;
	}
}
