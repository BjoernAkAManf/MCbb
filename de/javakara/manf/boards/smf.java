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

public class smf extends Software {
	public smf(String name, FileConfiguration config, boolean a)
			throws SQLException, ClassNotFoundException {
		super(name, config, a);
	}

	public smf(String name, FileConfiguration config) throws SQLException,
			ClassNotFoundException {
		super(name, config);
	}

	@Override
	public String getForumGroup(boolean b) {
		try {
			if (userId != 0) {
				ResultSet rs = database
						.executeQuery("SELECT id_group,servergroup FROM "
								+ this.config.getString("mysql.prefix")
								+ "membergroups WHERE id_group='" + groupId
								+ "'");
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
			ResultSet rs = database
					.executeQuery("SELECT id_member,member_name,id_group,is_activated FROM "
							+ this.config.getString("mysql.prefix")
							+ "members WHERE member_name="
							+ "lower('"
							+ name
							+ "')" + " LIMIT 1");

			if (rs.next()) {
				userId = rs.getInt("id_member");
				userType = rs.getInt("is_activated");
				groupId = rs.getInt("id_group");
				return (userType == 1);
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
			ResultSet rs = database
					.executeQuery("SELECT id_field,col_name FROM "
							+ this.config.getString("mysql.prefix")
							+ "custom_fields WHERE id_field" + "='"
							+ this.config.getString("field.id") + "' LIMIT 1");
			if (rs.next()) {
				String field = rs.getString("col_name");
				rs = database
						.executeQuery("SELECT id_member,variable,value FROM "
								+ this.config.getString("mysql.prefix")
								+ "themes WHERE variable =" + "'cust_" + field
								+ "'" + "AND value='" + name + "' " + "LIMIT 1");
				if (rs.next()) {
					userId = rs.getInt("id_member");
					String query = ("SELECT id_member,member_name,id_group,is_activated FROM "
							+ this.config.getString("mysql.prefix")
							+ "members WHERE id_member=" + "'" + userId + "')" + " LIMIT 1");
					rs = database.executeQuery(query);

					if (rs.next()) {
						userType = rs.getInt("is_activated");
						groupId = rs.getInt("id_group");
						return (userType == 1);
					}
				}
			}
		} catch (SQLException e) {
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
				rs = database.executeQuery("SELECT member_name,passwd FROM "
						+ this.config.getString("mysql.prefix")
						+ "members WHERE member_name=" + "lower('" + name
						+ "')" + " LIMIT 1");

				if (rs.next()) {
					String realpassword = rs.getString("passwd");
					return realpassword.equals(EncryptionManager
							.sha1(rs.getString("member_name ").toLowerCase()
									+ password));
				}
				return false;
			case 1:
				rs = database.executeQuery("SELECT id_field,col_name FROM "
						+ this.config.getString("mysql.prefix")
						+ "custom_fields WHERE id_field" + "='"
						+ this.config.getString("field.id") + "' LIMIT 1");
				if (rs.next()) {
					String field = rs.getString("col_name");
					rs = database.executeQuery("SELECT id_member,variable,value FROM "
									+ this.config.getString("mysql.prefix")
									+ "themes WHERE variable =" + "'cust_" + field
									+ "'" + "AND value='" + name + "' " + "LIMIT 1");
					if (rs.next()) {
						userId = rs.getInt("id_member");
						rs = database.executeQuery("SELECT member_name,passwd  FROM "
								+ this.config.getString("mysql.prefix")
								+ "members WHERE id_member=" + "'"
								+ userId + "')" + " LIMIT 1");
						if (rs.next()) {
							String realpassword = rs.getString("passwd");
							return realpassword.equals(EncryptionManager
									.sha1(rs.getString("member_name ").toLowerCase()
											+ password));
						}
					}
				}
				return false;
			default:
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}