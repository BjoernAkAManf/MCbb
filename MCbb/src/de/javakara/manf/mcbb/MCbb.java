package de.javakara.manf.mcbb;

import java.io.File;
import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;
import de.javakara.manf.util.LocalisationUtility;
import de.javakara.manf.util.Saver;
import de.javakara.manf.util.Version;

import org.bukkit.entity.Player;

public class MCbb extends JavaPlugin {
	final Version Version = new Version("1.9.0.1");
	public State ac;
	public ArrayList<Player> grey = new ArrayList<Player>();
	public static LocalisationUtility lang;
	private final GreyPlayerListener gPlayerListener = new GreyPlayerListener(this);
	private final WhitePlayerListener wPlayerListener = new WhitePlayerListener(this);
	private final RegisteredPlayerListener rPlayerListener = new RegisteredPlayerListener(this);
	private MCbbCommands MCbbExc;
    public static Economy economy = null;
    public static Permission permission = null;
    public static State debug;
    public static String ForumType;
	@Override
	public void onEnable() {
		debug = State.Off;
		if(getServer().getPluginManager().getPlugin("Vault") != null){
			if(setupEconomy()){
				System.out.println("Economy Support Activated!");
			}
			if(setupPermissions()){
				System.out.println("Permissions Support Activated!");
			}
		}
		lang = new LocalisationUtility(this);
		System.out.println(lang.get("System.misc.enabled") + Version);
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
		addRegisteredListeners();
		registerCommands();
		Updater.newUpdate(this, true);
	}
	
	 private Boolean setupPermissions()
	    {
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
		System.out.println(lang.get("System.misc.disabled")  + Version);
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
		val("economy.moneyPerPost", "0");
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
	
	private void registerCommands(){
		MCbbExc = new MCbbCommands(this);
		getCommand("mcbb").setExecutor(MCbbExc);
		ac = State.On;
	}
	
	private void testMySql() {
		Software anonymous;
		anonymous = ForumSoftware.getSoftwareObject(this.getConfig().getString("mysql.forumtyp"), this.getConfig().getString("mysql.verifyuser"), this.getConfig());

		if(anonymous.testMysql()){
			System.out.println(lang.get("System.Validate.mysql.testedSucess"));
		}
	}

	private void loadConfig() {
		Saver.load(this.getConfig(), lang, this.getDataFolder());
		ForumType = this.getConfig().getString("mysql.forumtype");
		System.out.println("ForumType: " + ForumType);
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
	
	public void save() {
		Saver.save(this.getConfig(),getDataFolder() + "/c.yml");
	}	
}
