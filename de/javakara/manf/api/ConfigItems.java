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

import de.javakara.manf.mcbb.MCbb;

/**
 * 
 * @author 
 *
 */
public final class ConfigItems {
	public static final String MYSQL_FORUMTYPE = "mysql.forumtype";
	public static final String MYSQL_VERIFYUSER = "mysql.verifyuser";
	public static final String MYSQL_HOST = "mysql.host";
	public static final String MYSQL_PORT = "mysql.port";
	public static final String MYSQL_USER = "mysql.user";
	public static final String MYSQL_PASSWORD = "mysql.password";
	public static final String MYSQL_DATABASE = "mysql.database";
	public static final String MYSQL_PREFIX = "mysql.prefix";
	public static final String GENERAL_TYPE = "general.type"; 
	public static final String GENERAL_GREYLIST_PROTECTION_DAMAGEENTITIES = "general.greylist.protection.damageentities";
	public static final String GENERAL_GREYLIST_PROTECTION_LOOTITEMS = "general.greylist.protection.lootitems";
	public static final String GENERAL_GREYLIST_PROTECTION_DROPITEMS = "general.greylist.protection.dropitems";
	public static final String GENERAL_GREYLIST_PROTECTION_CHAT = "general.greylist.protection.chat";
	public static final String GENERAL_GREYLIST_PROTECTION_INTERACT = "general.greylist.protection.interact";
	public static final String GENERAL_GREYLIST_PROTECTION_COMMAND = "general.greylist.protection.command";
	public static final String ECONOMY_REWARD = "economy.reward";
	public static final String SYNCGROUPS_ENABLED = "syncgroup.enabled";
	public static final String SYNCGROUPS_TYPE = "syncgroup.type";
	public static final String AUTH_TYPE = "auth.type";
	public static final String AUTH_FIELD_ID = "auth.field.id";
	public static final String AUTH_LOGIN_ENABLED = "auth.login.enabled";
	public static final String MCBB_VERSION = "mcbb.version";
	
	public static final Object getDefaultValue(String node){
		node = node.toLowerCase();
		if(node.endsWith("enabled")){
			return false;
		}else if(node.contains("greylist")){
			return true;
		}else if(node.startsWith("mysql")){
			if(node.endsWith("forumtype")){
				return "phpbb";
			}else if(node.endsWith("verifyuser")){
				return "anonymous";
			}else if(node.endsWith("host")){
				return "localhost";
			}else if(node.endsWith("port")){
				return 3306;
			}else if(node.endsWith("user")){
				return "root";
			}else if(node.endsWith("password")){
				return "password";
			}else if(node.endsWith("database")){
				return "mc";
			}else if(node.endsWith("prefix")){
				return "phpbb3_";
			}
		}else if(node.startsWith("economy")){
			if(node.endsWith("reward")){
				return new String[] {"1|M|100","2|M|200"};
			}
		}else if(node.endsWith("type")){
			if(node.startsWith("general")){
				return "Greylist";
			}
			if(node.startsWith("syncgroups")){
				return "syncForum";
			}
			if(node.startsWith("auth")){
				return "Field";
			}
		}else if(node.endsWith("id")){
			return 1;
		}else if(node.startsWith("mcbb")){
			if(node.endsWith("version")){
				return MCbb.getVersion();
			}
		}
		return "no-default-value";
	}
}
