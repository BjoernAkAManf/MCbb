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

import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.javakara.manf.api.Config;
import de.javakara.manf.api.ConfigItems;
import de.javakara.manf.api.ForumSoftware;
import de.javakara.manf.api.Software;
import de.javakara.manf.economy.EconomyManager;
import de.javakara.manf.listeners.GreyPlayerListener;
import de.javakara.manf.listeners.LoginPlayerListener;
import de.javakara.manf.listeners.RegisteredPlayerListener;
import de.javakara.manf.listeners.WhitePlayerListener;
import de.javakara.manf.util.LocalisationUtility;
import de.javakara.manf.util.Version;

import org.bukkit.entity.Player;

public class MCbb extends JavaPlugin {
	final static Version version = new Version("2.0.0.0");
	public State ac = State.Off;
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
		loadConfig();
		testMySql();
		
		if(Config.getString(ConfigItems.GENERAL_TYPE).equals("Greylist")){
			addGreyListeners();
			System.out.println(lang.get("System.info.greyActivated"));
		}
		else if(Config.getString(ConfigItems.GENERAL_TYPE).equals("Whitelist")){
			addWhiteListeners();
			System.out.println(lang.get("System.info.whiteActivated"));
		}
		else{
			System.out.println(lang.get("System.error.unknownType"));
			this.getPluginLoader().disablePlugin(this);
		}
		
		if(Config.getBoolean(ConfigItems.AUTH_LOGIN_ENABLED)){
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
	
	private void registerCommands(){
		MCbbExc = new MCbbCommands(this);
		getCommand("mcbb").setExecutor(MCbbExc);
		getCommand("login").setExecutor(MCbbExc);
		getCommand("logout").setExecutor(MCbbExc);
		ac = State.On;
	}
	
	private void testMySql() {
		ForumSoftware.init(getDataFolder() + "", forumType, "Forum");
		Software anonymous = ForumSoftware.getSoftwareObject();


		if(anonymous.testMysql()){
			System.out.println(lang.get("System.Validate.mysql.testedSucess"));
		}
	}

	private void loadConfig() {
		Config.initialise(getConfig(), lang, getDataFolder());
		forumType = Config.getString("mysql.forumtype");
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
		//return version.isOutdated();
		return false;
	}

	public static int getVersion() {
		return version.getMajor();
	}

	public static Version getFullVersion() {
		return version;
	}	
}
