package de.javakara.manf.software;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.boards.myBB;
import de.javakara.manf.boards.phpBB;
import de.javakara.manf.boards.smf;

public class ForumSoftware {
	public static Software getSoftwareObject(String software,String playername, FileConfiguration config){
		if(software != null){
			if(software.equalsIgnoreCase("phpbb")){
				return new phpBB(playername, config);
			}
			if(software.equalsIgnoreCase("mybb")){
				return new myBB(playername, config);
			}
			if(software.equalsIgnoreCase("smf")){
				return new smf(playername, config);
			}
		}
		System.out.println("ForumSoftware not Found!");
		return null;
	} 
}
