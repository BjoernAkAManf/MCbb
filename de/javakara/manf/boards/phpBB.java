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

public class phpBB extends Software {

	public phpBB(String name, FileConfiguration config, boolean a)
			throws SQLException, ClassNotFoundException {
		super(name, config, a);
	}

	public phpBB(String name, FileConfiguration config) throws SQLException,
			ClassNotFoundException {
		super(name, config);
	}

	@Override
	public String getForumGroup(boolean b) {
		try {
			if (userId != 0) {
				ResultSet rs = database
						.executeQuery("SELECT group_id FROM "
								+ this.config.getString("mysql.prefix")
								+ "user_group WHERE user_id ='"
								+ userId
								+ "' ORDER BY group_leader DESC,user_pending ASC, group_id DESC");

				if (rs.next()) {
					groupId = rs.getInt("group_id");
					rs = database.executeQuery("SELECT servergroup FROM "
							+ this.config.getString("mysql.prefix")
							+ "groups WHERE group_id ='" + groupId + "'");

					if (rs.next()) {
						return rs.getString("servergroup");
					}
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
			ResultSet rs = database
					.executeQuery("SELECT user_id,username_clean,user_type FROM "
							+ this.config.getString("mysql.prefix")
							+ "users WHERE username_clean='"
							+ name
							+ "' LIMIT 1");
			if (rs.next()) {

				userId = rs.getInt("user_id");
				userType = rs.getInt("user_type");

				if (o)
					System.out.println("UserType: " + userType);
				return (userType != 1);
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
			ResultSet rs = database.executeQuery("SELECT user_id FROM "
					+ this.config.getString("mysql.prefix")
					+ "profile_fields_data WHERE pf_"
					+ getFieldName(Integer.parseInt(this.config
							.getString("field.id"))) + "='" + name
					+ "' LIMIT 1");
			if (rs.next()) {
				userId = rs.getInt("user_id");
				rs = database
						.executeQuery("SELECT username_clean,user_type FROM "
								+ this.config.getString("mysql.prefix")
								+ "users WHERE user_id='" + userId
								+ "' LIMIT 1");
				if (rs.next()) {
					userType = rs.getInt("user_type");
					if (o)
						System.out.println("UserType: " + userType);
					return (userType != 1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getFieldName(int id) throws ClassNotFoundException,
			SQLException {
		ResultSet rs = database.executeQuery("SELECT field_name FROM "
				+ this.config.getString("mysql.prefix")
				+ "profile_fields WHERE field_id='" + id + "' LIMIT 1");

		if (rs.next()) {
			return rs.getString("field_name");
		}
		return null;
	}

	@Override
	public boolean isPasswordCorrect(String password) {
		try {
			ResultSet rs;
			switch (authType) {
			case 0:
				rs = database.executeQuery("SELECT user_password,user_form_salt FROM "
								+ this.config.getString("mysql.prefix")
								+ "users WHERE username_clean='"
								+ name + "' LIMIT 1");
				
				break;
			case 1:
				rs = database.executeQuery("SELECT user_id FROM "
						+ this.config.getString("mysql.prefix")
						+ "profile_fields_data WHERE pf_"
						+ getFieldName(Integer.parseInt(this.config
								.getString("field.id"))) + "='" + name
						+ "' LIMIT 1");
				if (rs.next()) {
					userId = rs.getInt("user_id");
					rs = database
							.executeQuery("SELECT user_password FROM "
									+ this.config.getString("mysql.prefix")
									+ "users WHERE user_id='"
									+ userId
									+ "' LIMIT 1");
				}
				break;
			default:
				return false;
			}

			if (rs.next()) {
				String realpassword = rs.getString("user_password");
				if (phpbb_check_hash(password,realpassword)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/***
	 * Port of phpBB3 password handling to Java. 
	 * See phpBB3/includes/functions.php
	 * just used phpbb_check_hash and its sub
	 * http://larsho.blogspot.de/2008/02/passwords-in-phpbb-3.html
	 * @author lars
	 * @param password
	 * @param hash
	 * @return
	 */
	public boolean phpbb_check_hash(String password, String hash) {
		if (hash.length() == 34)
			return EncryptionManager._hash_crypt_private(password, hash).equals(hash);
		else
			return EncryptionManager.md5(password).equals(hash);
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
