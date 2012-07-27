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

import java.io.File;

import de.javakara.manf.util.pluginloader.JarClassLoader;

public final class PluginManager {
	public static Software load(String pluginFolder, String softwareName,String type) {
		
		JarClassLoader jarLoader = new JarClassLoader(pluginFolder
				+ File.separator + softwareName + "-" + type +".jar");
		Class<?> c = null;
		try {
			c = jarLoader.loadClass(softwareName, true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Object o = null;
		try {
			o = c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Software software = null;
		if (o instanceof Software) {
			software = (Software) o;
			System.out.println(software.getName() + "found and used");
		}
		return software;
	}
}