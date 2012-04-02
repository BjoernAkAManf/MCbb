package de.javakara.manf.software;
import org.bukkit.configuration.file.FileConfiguration;

public class Software {
	public String name;
	public FileConfiguration config;
	public int userType;
	public int userId;
	public int groupId;
	public String forumUserName;
	
	public Software(String name, FileConfiguration config, boolean a) {
		this.name = name.toLowerCase();
		this.config = config;
	}
	
	public Software(String name, FileConfiguration config) {
		this.name = name.toLowerCase();
		this.config = config;
	}
	
	public int getNewPosts() {
		return 0;
	}
	
	public boolean getRegistrationValue(boolean o) {
		if (config.getString("general.authType").equals("Username"))
			return this.isRegisteredOld(o);
		if (config.getString("general.authType").equals("Field"))
			return this.isCustomFieldRegistered(o);
		return false;
	}
	
	public boolean testMysql() {
		return this.isRegisteredOld(false);
	}
	
	public String getForumGroup(boolean b) {
		return "";
	}
	
	public String getForumGroup() {
		return this.getForumGroup(false);
	}
	
	protected boolean isRegisteredOld(boolean o) {
		return false;
	}
	
	protected boolean isCustomFieldRegistered(boolean o) {
		return false;
	}
}