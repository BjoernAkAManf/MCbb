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

package de.javakara.manf.software;

import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.boards.XenForo;
import de.javakara.manf.boards.myBB;
import de.javakara.manf.boards.phpBB;
import de.javakara.manf.boards.smf;
import de.javakara.manf.boards.vBulletin;

public class ForumSoftware {
	public static Software getSoftwareObject(String software,String playername,
													FileConfiguration config) {
		if (software != null) {
			try {
				if (software.equalsIgnoreCase("phpbb")) {
					return new phpBB(playername, config);
				}
				if (software.equalsIgnoreCase("mybb")) {
					return new myBB(playername, config);
				}
				if (software.equalsIgnoreCase("smf")) {
					return new smf(playername, config);
				}
				if (software.equalsIgnoreCase("XenForo")) {
					return new XenForo(playername, config);
				}
				if (software.equalsIgnoreCase("vBulletin")) {
					return new vBulletin(playername, config);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("ForumSoftware not Found!");
			return null;
		}
		System.out.println("Error while enabling Software!");
		return null;
	}
}
