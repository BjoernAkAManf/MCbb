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

package de.javakara.manf.boards;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.software.Software;
import de.javakara.manf.util.EncryptionManager;

public class vBulletin extends Software {
		public vBulletin(String name, FileConfiguration config, boolean a) throws SQLException, ClassNotFoundException {
			super(name,config,a);
		}
		
		public vBulletin(String name, FileConfiguration config) throws SQLException, ClassNotFoundException {
			super(name,config);
		}
		
		@Override
		public String getForumGroup(boolean b) {
			try {
				if (userId != 0) {
					ResultSet rs = database.executeQuery("SELECT servergroup FROM "
							+ this.config.getString("mysql.prefix")
							+ "usergroup WHERE usergroupid='" + userType + "'");
					if (rs.next()) {
						return rs.getString("servergroup");
					}
				
				} else {
					System.out.println("[MCbb] Sorry... Theres a fail in there!");
				}
			} catch (SQLException e) {
				System.out.println("ForumUserError: " + e.toString());
			}

			System.out.println("User Forum Group not recognised!");
			return null;
		}
		
		@Override
		public int getNewPosts() {
			return 0;
		}
		
		@Override
		protected boolean isRegisteredOld(boolean o) {
			try {
				ResultSet rs = database.executeQuery("SELECT userid,username,usergroupid FROM "
						+ this.config.getString("mysql.prefix")
						+ "user WHERE username="
						+ "lower('" + name + "')"
						+ " LIMIT 1");
				
				if (rs.next()) {
					userId = rs.getInt("userid");
					userType = rs.getInt("usergroupid");
					
					return (userType != 8);
				}
			} catch (SQLException e) {
				System.out.println("Qwertzy2");
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected boolean isCustomFieldRegistered(boolean o) {
			try {
				ResultSet rs = database.executeQuery("SELECT userid FROM "
						+ this.config.getString("mysql.prefix")
						+ "userfield WHERE field" + this.config.getString("field.id") + "'"
						+ name + "' LIMIT 1");
				if (rs.next()) {
					userId = rs.getInt("userid");
					rs = database.executeQuery("SELECT username,usergroupid FROM "
							+ this.config.getString("mysql.prefix")
							+ "user WHERE userid='" + userId + "' LIMIT 1");
					if (rs.next()) {
						userType = rs.getInt("usergroupid");
						return (userType != 8);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public boolean isPasswordCorrect(String password) {
			try {
				ResultSet rs;
				switch (authType) {
				case 0:
					rs = database.executeQuery("SELECT password,salt FROM "
							+ this.config.getString("mysql.prefix")
							+ "user WHERE username="
							+ "lower('" + name + "')"
							+ " LIMIT 1");
					break;
					
				case 1:
					rs = database.executeQuery("SELECT userid FROM "
							+ this.config.getString("mysql.prefix")
							+ "userfield WHERE field" + this.config.getString("field.id") + "'"
							+ name + "' LIMIT 1");
					if (rs.next()) {
						userId = rs.getInt("userid");
						rs = database.executeQuery("SELECT password,salt FROM "
								+ this.config.getString("mysql.prefix")
								+ "user WHERE userid='" + userId + "' LIMIT 1");
						break;
					}
				default:
					return false;
				}
				
				if (rs.next()) {
					String realpassword = rs.getString("password");
					String salt = rs.getString("salt");
					return realpassword.equals(sha256(sha256(password) + salt));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			return false;
		}

		private String sha256(String input) {
			return EncryptionManager.sha256(input);
		}

		@Override
		protected String getName() {
			// TODO Auto-generated method stub
			return null;
		}
}