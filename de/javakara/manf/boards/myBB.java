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

public class myBB extends Software {

	public myBB(String name, FileConfiguration config) throws SQLException,
			ClassNotFoundException {
		super(name, config);
	}

	public myBB(String name, FileConfiguration config, boolean a)
			throws SQLException, ClassNotFoundException {
		super(name, config, a);
	}

	@Override
	public String getForumGroup(boolean b) {
		try {
			if (userId != 0) {
				ResultSet rs = database.executeQuery("SELECT servergroup FROM "
						+ this.config.getString("mysql.prefix")
						+ "usergroups WHERE gid ='" + userType + "'");
				if (rs.next()) {
					return rs.getString("servergroup");
				}

			} else {
				System.out.println("[MCbb] Sorry... Theres a fail in there!");
				return null;
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
			ResultSet rs = database
					.executeQuery("SELECT uid,username,usergroup FROM "
							+ this.config.getString("mysql.prefix")
							+ "users WHERE username=" + "lower('" + name + "')"
							+ " LIMIT 1");

			if (rs.next()) {
				userId = rs.getInt("uid");
				userType = rs.getInt("usergroup");
				return (userType != 5);
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
			ResultSet rs = database.executeQuery("SELECT ufid FROM "
					+ this.config.getString("mysql.prefix")
					+ "userfields WHERE fid"
					+ this.config.getString("field.id") + "='" + name
					+ "' LIMIT 1");
			if (rs.next()) {
				userId = rs.getInt("ufid");
				rs = database.executeQuery("SELECT uid,usergroup FROM "
						+ this.config.getString("mysql.prefix")
						+ "users WHERE uid='" + userId + "' LIMIT 1");
				if (rs.next()) {
					userType = rs.getInt("usergroup");
					if (o)
						System.out.println("UserGroup: " + userType);
					return (userType != 5);
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
				  rs = database.executeQuery("SELECT salt,password FROM "
								+ this.config.getString("mysql.prefix")
								+ "users WHERE username=" + "lower('" + name
								+ "')" + " LIMIT 1");

				if (rs.next()) {
					String salt = rs.getString("salt");
					String realpassword = rs.getString("password");
					return realpassword.equals((md5(md5(salt)+md5(password))));
				}
				return false;
			case 1:
				rs = database.executeQuery("SELECT ufid FROM "
						+ this.config.getString("mysql.prefix")
						+ "userfields WHERE fid"
						+ this.config.getString("field.id") + "='" + name
						+ "' LIMIT 1");
				if (rs.next()) {
					userId = rs.getInt("ufid");
					rs = database.executeQuery("SELECT salt,password FROM "
							+ this.config.getString("mysql.prefix")
							+ "users WHERE uid='" + userId + "' LIMIT 1");
					if (rs.next()) {
						String salt = rs.getString("salt");
						String realpassword = rs.getString("password");
						return realpassword.equals((md5(md5(salt)+md5(password))));
					}
				}
				return false;
			default:
				return false;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String md5(String s) {
		return EncryptionManager.md5(s);
	}

	@Override
	protected String getName() {
		return null;
	}
}