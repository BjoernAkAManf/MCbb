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

package de.javakara.manf.software;

import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.database.MySQLManager;

public abstract class Software {
	protected FileConfiguration config;
	protected MySQLManager database;
	protected int authType;

	public void init(FileConfiguration config) throws SQLException, ClassNotFoundException{
		this.config = config;
		database = new MySQLManager(config.getString("mysql.host"),
				config.getString("mysql.port"),
				config.getString("mysql.database"),
				config.getString("mysql.user"),
				config.getString("mysql.password"));
		setAuthTyp();
	}

	private void setAuthTyp() {
		if (config.getString("general.authType").equals("Username")) {
			authType = 0;
			return;
		}

		if (config.getString("general.authType").equals("Field")) {
			authType = 1;
			return;
		}
		authType = -1;
	}

	public boolean getRegistrationValue(User user) {
		switch (authType) {
		case 1:
			return this.isRegisteredOld(user);
		case 2:
			return this.isCustomFieldRegistered(user);
		default:
			return false;
		}
	}

	public boolean testMysql() {
		return this.isRegisteredOld(new User(config.getString("mysql.verifyuser")));
	}

	public abstract int getNewPosts();

	public abstract String getForumGroup(User user);

	public abstract boolean isPasswordCorrect(User user,String password);

	protected abstract String getName();
	
	protected abstract boolean isRegisteredOld(User user);

	protected abstract boolean isCustomFieldRegistered(User user);

}