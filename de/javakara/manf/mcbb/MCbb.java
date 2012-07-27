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

package de.javakara.manf.mcbb;

import java.io.File;
import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.javakara.manf.economy.EconomyManager;
import de.javakara.manf.listeners.GreyPlayerListener;
import de.javakara.manf.listeners.LoginPlayerListener;
import de.javakara.manf.listeners.RegisteredPlayerListener;
import de.javakara.manf.listeners.WhitePlayerListener;
import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;
import de.javakara.manf.util.LocalisationUtility;
import de.javakara.manf.util.Saver;
import de.javakara.manf.util.Version;

import org.bukkit.entity.Player;

public class MCbb extends JavaPlugin {
	final Version version = new Version("2.0.0.0");
	public State ac;
	public ArrayList<Player> grey = new ArrayList<Player>();
	public static LocalisationUtility lang;
	private final GreyPlayerListener gPlayerListener = new GreyPlayerListener(this);
	private final WhitePlayerListener wPlayerListener = new WhitePlayerListener(this);
	private final RegisteredPlayerListener rPlayerListener = new RegisteredPlayerListener(this);
	private final LoginPlayerListener lPlayerListener = new LoginPlayerListener(); 
	private MCbbCommands MCbbExc;
    private static Economy economy = null;
    public static Permission permission = null;
    public static State debug;
    public static String forumType;
	
    @Override
	public void onEnable() {
		debug = State.Off;
		if(getServer().getPluginManager().getPlugin("Vault") != null){
			if(setupEconomy()){
				System.out.println("Economy Support Activated!");
				EconomyManager.initialise(economy);
			}
			if(setupPermissions()){
				System.out.println("Permissions Support Activated!");
			}
		}
		lang = new LocalisationUtility(this);
		System.out.println(lang.get("System.misc.enabled") + version);
		if (!(new File(getDataFolder() + "/c.yml").exists()))
			save();
		loadConfig();
		validateConfig();
		testMySql();
		
		if(this.getConfig().getString("general.type").equals("Greylist")){
			addGreyListeners();
			System.out.println(lang.get("System.info.greyActivated"));
		}
		else if(this.getConfig().getString("general.type").equals("Whitelist")){
			addWhiteListeners();
			System.out.println(lang.get("System.info.whiteActivated"));
		}
		else{
			System.out.println(lang.get("System.error.unknownType"));
			this.getPluginLoader().disablePlugin(this);
		}
		
		if(this.getConfig().getString("sec.login.on").equals("true")){
			addLoginListener();
		}
		
		addRegisteredListeners();
		registerCommands();
		Updater.newUpdate(this, true);
		

	}
	
	 private Boolean setupPermissions(){
	        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	            permission = permissionProvider.getProvider();
	        }
	        return (permission != null);
	}
	 
	@Override
	public void onDisable() {
		for (Player p: grey) {
			   p.kickPlayer(lang.get("System.info.kick."));
			}
		System.out.println(lang.get("System.misc.disabled")  + version);
	}
	
	private void addGreyListeners(){
		Bukkit.getServer().getPluginManager().registerEvents(gPlayerListener, this);
	}
	
	private void addWhiteListeners(){
		Bukkit.getServer().getPluginManager().registerEvents(wPlayerListener, this);
	}
	
	private void addRegisteredListeners(){
		Bukkit.getServer().getPluginManager().registerEvents(rPlayerListener, this);
	}
	
	private void addLoginListener(){
		Bukkit.getServer().getPluginManager().registerEvents(lPlayerListener, this);
	}
	
	private void validateConfig(){
		System.out.println(lang.get("System.Validate.start"));
		val("mysql.forumtype", "phpbb");
		val("mysql.verifyuser", "anonymous");
		val("mysql.host", "localhost");
		val("mysql.port", "3306");
		val("mysql.user", "root");
		val("mysql.password", "test");
		val("mysql.database", "mc");
		val("mysql.prefix", "phpbb3_");
		val("general.type", "Greylist");//Greylist , Whitelist
		val("general.authType", "Field");//Field , Username 
		val("field.id","1");
		val("general.syncGroups","false");
		val("general.greylist.protection.damageEntities", "true"); //true , false
		val("general.greylist.protection.lootItems", "true"); //true , false
		val("general.greylist.protection.dropItems", "true"); //true , false
		val("general.greylist.protection.chat", "true"); //true , false
		val("general.greylist.protection.interact", "true"); //true , false
		val("general.greylist.protection.command", "true"); //true , false
		val("economy.reward", new String[] {"1|M|100","2|M|200"});
		
		val("sec.login.on","false"); //true, false
		System.out.println(lang.get("System.Validate.confSucess"));
		save();
	}
	
	private void val(String node,String defaultValue){
		if(this.getConfig().getString(node) == null){
			if(lang.get("System.Validate." + node) != null)
				System.out.println(lang.get("System.Validate." + node));
			else
				lang.get("[MCbb] Err 3: Unknown Lang-NodeValidation: " + node);
			this.getConfig().set(node, defaultValue);	
		}
	}
	
	private void val(String node, String[] defaultValue){
		if(this.getConfig().getStringList(node) == null){
			if(lang.get("System.Validate." + node) != null)
				System.out.println(lang.get("System.Validate." + node));
			else
				lang.get("[MCbb] Err 3: Unknown Lang-NodeValidation: " + node);
			this.getConfig().set(node, defaultValue);	
		}	
	}
	
	private void registerCommands(){
		MCbbExc = new MCbbCommands(this);
		getCommand("mcbb").setExecutor(MCbbExc);
		ac = State.On;
	}
	
	private void testMySql() {
		ForumSoftware.init(getDataFolder() + "", forumType, "Forum", getConfig());
		Software anonymous = ForumSoftware.getSoftwareObject();


		if(anonymous.testMysql()){
			System.out.println(lang.get("System.Validate.mysql.testedSucess"));
		}
	}

	private void loadConfig() {
		Saver.load(this.getConfig(), lang, this.getDataFolder());
		forumType = this.getConfig().getString("mysql.forumtype");
		System.out.println("ForumType: " + forumType);
	}
	
	private Boolean setupEconomy(){
	    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	    if (economyProvider != null) {
	    	economy = economyProvider.getProvider();
	    }
	    return (economy != null);
	}
	
	public boolean setOn() {
		ac = State.On;
		System.out.println(lang.get("System.state.seton"));
		return true;
	
	}

	public boolean status(CommandSender sender) {
		if(ac == State.On)
			sender.sendMessage(lang.get("System.state.on"));
		if(ac == State.Off)
			sender.sendMessage(lang.get("System.state.off"));
		return true;
	}

	public boolean setOff() {
		ac = State.Off;
		System.out.println(lang.get("System.state.setoff"));
		return true;
	}
	
	public boolean isOutdated(){
		return version.isOutdated();
	}
	
	public void save() {
		Saver.save(this.getConfig(),getDataFolder() + "/c.yml");
	}	
}
