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

package de.javakara.manf.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.javakara.manf.database.MySQLManager;
import de.javakara.manf.util.pluginloader.JarClassLoader;

public final class PluginManager {
	private static ArrayList<Class<?>> api = new ArrayList<Class<?>>();
	static{
		api.add(Software.class);
		api.add(Config.class);
		api.add(ConfigItems.class);
		api.add(EncryptionManager.class);
		api.add(User.class);
		api.add(EncryptionManager.class);
		api.add(MySQLManager.class);
	}
	
	public static Software load(String pluginFolder, String softwareName,String type){
		
		JarClassLoader jarLoader = new JarClassLoader(pluginFolder
				+ File.separator + softwareName + "-" + type +".jar");
		try {
			addApiClasses(jarLoader);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
	
	private static void addApiClasses(JarClassLoader jarLoader) throws ClassNotFoundException, IOException{
		for(Class<?> c:api){
			jarLoader.addApiClass(c.getName(), c);
		}
	}
}