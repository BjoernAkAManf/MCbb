package de.javakara.manf.software;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.boards.XenForo;
import de.javakara.manf.boards.myBB;
import de.javakara.manf.boards.phpBB;
import de.javakara.manf.boards.smf;
import de.javakara.manf.boards.vBulletin;

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
			if(software.equals("XenForo")){
				return new XenForo(playername,config);
			}
			if(software.equals("vBulletin")){
				return new vBulletin(playername,config);
			}
		}
		System.out.println("ForumSoftware not Found!");
		return null;
	} 
}
