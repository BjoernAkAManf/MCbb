package de.javakara.manf.util;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.mcbb.MCbb;

public class LocalisationUtility {
	public FileConfiguration config;
	public File dataFolder;
	public LocalisationUtility(MCbb p){
		config = p.getConfig();
		dataFolder = p.getDataFolder();
		load(config);
		validateConfig();
	}

	private void validateConfig(){
		System.out.println("[MCbb] Language Config Check");
		config.set("System.lang.teach", "Teaching MCbb ");
		val("System.Validate.start","[MCbb] Validating Configs");
		val("System.Validate.confSucess","[MCbb] Configs validated");
		val("System.Validate.mysql.host","[MCbb] Creating MySQL Host");
		val("System.Validate.mysql.port","[MCbb] Creating MySQL Port");
		val("System.Validate.mysql.user","[MCbb] Creating MySQL User");
		val("System.Validate.mysql.password","[MCbb] Creating MySQL Password");
		val("System.Validate.mysql.database","[MCbb] Creating MySQL Database");
		val("System.Validate.mysql.prefix","[MCbb] Creating MySQL Prefix");
		val("System.Validate.mysql.testedSucess","[MCbb] MySql was tested by User Verifikation Anonymous!");
		val("System.conf.loading","[MCbb] Loading Configs");
		val("System.conf.loadSucess","[MCbb] Configs loaded");
		val("System.state.seton","[MCbb] turned on");
		val("System.state.on","[MCbb] Plugin is currently activated");
		val("System.state.off","[MCbb] Plugin is currently deactivated");
		val("System.state.setoff","[MCbb] turned off");
		val("System.info.registerFirst","[MCbb] First register on the Forum!");
		val("System.info.auth","[MCBB] Player <player> is authenticated!");
		val("System.info.kick","Server is Reloading; Please register on the Forum!");
		val("System.info.whitelist","Not on Whitelist! Register on the Forum;");
		val("System.info.newUpdate","[MCbb] New Update!");
		val("System.info.greyActivated","[MCbb] Greylist activated");
		val("System.info.whiteActivated","[MCbb] Whitelist activated");
		val("System.error.unknownType","Type is unknown! Disabeling Plugin");
		val("System.error.noPerm","&2[MCbb] Permission error!");
		val("System.update.version","Version");
		val("System.update.currversion","Current Version: ");
		val("System.update.latestversion","Latest Version: ");
		val("System.update.location","http://exo-network.de/manf/index.php");
		val("System.misc.enabled","[MCbb] loaded ->");
		val("System.misc.disabled","[MCbb] unloaded ->");
		
		System.out.println("[MCbb] Language Config Check sucessfull");
		Saver.save(config,dataFolder + "/c.yml");
	}
	
	private void val(String node,String defaultValue){
		if(config.getString(node) == null)
		{
			System.out.println(this.get("System.lang.teach") + node);
			config.set(node, defaultValue);
		}
	}
	
	public String get(String node){
		return parseLang.parseColor(config.getString(node));
	}
	
	private void load(FileConfiguration fc) {
		System.out.println("[MCbb] Loading Language");
		try {
			fc.load(dataFolder + "/c.yml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[MCbb] Language loaded");
	}
}
