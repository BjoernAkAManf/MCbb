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

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.mcbb.MCbb;

public class Saver {
	public MCbb plugin;
	public Saver(MCbb p)
	{	
		plugin = p;
	}
	public void save(FileConfiguration fc) {
		try {
			fc.save(plugin.getDataFolder() + "/c.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save(FileConfiguration fc, String path) {
		try {
			fc.save(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void load(FileConfiguration fc,LocalisationUtility lang,File dataFolder) {
		System.out.println(lang.get("System.conf.loading"));
		try {
			fc.load(dataFolder + "/c.yml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(lang.get("System.conf.loadSucess"));
	}
}
