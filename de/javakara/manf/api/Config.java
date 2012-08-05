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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.mcbb.MCbb;
import de.javakara.manf.util.LocalisationUtility;
import de.javakara.manf.util.Saver;

public final class Config {
	private static FileConfiguration config;
	private static LocalisationUtility language;
	private static File dataFolder;
	//private static File configFile;
	
	public static boolean initialise(FileConfiguration config,LocalisationUtility language,File dataFolder){
		Config.config = config;
		Config.language = language;
		Config.dataFolder = dataFolder;
		//Config.configFile = new File(dataFolder + "/c.yml");
		System.out.println(language.get("System.Validate.start"));
		if (!config.isSet("mcbb.version")){
			validate();
			save();
			System.out.println("asdf");
		}
		if(!(config.getInt("mcbb.version") >= MCbb.getVersion())){
			System.out.println("Break in Config found. Delete Config to load Mcbb!");
			return false;
		}else{
			validate();
			save();
		}
		return true;
		
		
	}
	
	private static void validate(){
		Field[] fields = ConfigItems.class.getDeclaredFields();
		for(Field f :fields){
			if(Modifier.isPublic(f.getModifiers())){
				val(f.getName().replace("_", ".").toLowerCase(),ConfigItems.getDefaultValue(f.getName()));
			}
		}
	}
	public static int getInt(String node){
		return config.getInt(node);
	}
	
	public static String getString(String node){
		return config.getString(node);
	}

	public static boolean getBoolean(String node) {
		return config.getBoolean(node);
	}
	
	public static List<String> getStringList(String node){
		return config.getStringList(node);
	}
		
	private static void val(String node,Object defaultValue){
		if(!config.isSet(node)){
			config.set(node, defaultValue);	
		}
	}
	
	public static String loadConfig() {
		Saver.load(config, language, dataFolder);
		String forumType = Config.getString(ConfigItems.MYSQL_FORUMTYPE);
		System.out.println("ForumType: " + forumType);
		return forumType;
	}
	
	public static void save() {
		Saver.save(config,dataFolder + "/config.yml");
	}
}
