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

import java.sql.SQLException;

import de.javakara.manf.database.MySQLManager;

public abstract class Software {
	protected MySQLManager database;
	protected int authType;

	public void init() throws SQLException, ClassNotFoundException{
		database = new MySQLManager(Config.getString(ConfigItems.MYSQL_HOST),
				Config.getString(ConfigItems.MYSQL_PORT),
				Config.getString(ConfigItems.MYSQL_DATABASE),
				Config.getString(ConfigItems.MYSQL_USER),
				Config.getString(ConfigItems.MYSQL_PASSWORD));
		setAuthTyp();
		System.out.println("Using: " + Config.getString(ConfigItems.AUTH_TYPE) + "for AuthType");
	}

	private void setAuthTyp() {
		if (Config.getString(ConfigItems.AUTH_TYPE).equals("Username")) {
			authType = 0;
			return;
		}

		if (Config.getString(ConfigItems.AUTH_TYPE).equals("Field")) {
			authType = 1;
			return;
		}
		authType = -1;
	}

	public boolean getRegistrationValue(User user) {
		switch (authType) {
		case 0:
			return this.isRegisteredOld(user);
		case 1:
			return this.isCustomFieldRegistered(user);
		default:
			return false;
		}
	}

	public boolean testMysql() {
		return this.isRegisteredOld(new User(Config.getString(ConfigItems.MYSQL_VERIFYUSER)));
	}

	public abstract int getNewPosts();

	public abstract String getForumGroup(User user);

	public abstract boolean isPasswordCorrect(User user,String password);

	protected abstract String getName();
	
	protected abstract boolean isRegisteredOld(User user);

	protected abstract boolean isCustomFieldRegistered(User user);

}