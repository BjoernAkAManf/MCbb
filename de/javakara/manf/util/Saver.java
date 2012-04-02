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
