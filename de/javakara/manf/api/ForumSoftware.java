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

import java.sql.SQLException;
import java.util.HashMap;

public class ForumSoftware {
	private static Software software;
	private static String pluginFolder,softwareName,type;
	private static HashMap<String, User> users = new HashMap<String,User>();
	
	public static void init(String pluginFolder,String softwareName,String type){
		ForumSoftware.pluginFolder = pluginFolder;
		ForumSoftware.softwareName = softwareName;
		ForumSoftware.type = type;
	}
	
	public static Software getSoftwareObject(){
		if(software == null){
			software = PluginManager.load(pluginFolder, softwareName, type);
	
			if(software == null){
				System.out.println("ForumSoftware not Found!");
				System.out.println("Be aware the Name of the Jar needs to be:!");
				System.out.println("NameOfTheSoftware-type.jar");
			}else{
				try {
					software.init();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return software;
	}
	
	public static User getUser(String name){
		if(!users.containsKey(name)){
			users.put(name, new User(name));
		}
		return users.get(name);
	}
}