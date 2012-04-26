package de.javakara.manf.boards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;

import de.javakara.manf.software.Software;

public class XenForo extends Software {
		public XenForo(String name, FileConfiguration config, boolean a) {
			super(name,config,a);
		}
		
		public XenForo(String name, FileConfiguration config) {
			super(name,config);
		}
		
		@Override
		public int getNewPosts() {
			return 0;
		}
		
		@Override
		public String getForumGroup(boolean b) {
			try {
				if (userId != 0) {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://" + config.getString("mysql.host")
							+ ":" + config.getString("mysql.port") + "/"
							+ config.getString("mysql.database");
					Connection con = DriverManager.getConnection(url,
							config.getString("mysql.user"),
							config.getString("mysql.password"));
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT servergroup FROM "
							+ this.config.getString("mysql.prefix")
							+ "user_group WHERE user_group_id='" + userType + "'");
					if (rs.next()) {
						return rs.getString("servergroup");
					}
				
				} else {
					System.out.println("[MCbb] Sorry... Theres a fail in there!");
				}
			} catch (ClassNotFoundException e) {
				System.out.println("ForumUserError: " + e.toString());
			} catch (SQLException e) {
				System.out.println("ForumUserError: " + e.toString());
			}

			System.out.println("User Forum Group not recognised!");
			return null;
		}
		
		@Override
		public String getForumGroup() {
			return this.getForumGroup(false);
		}
		
		@Override
		protected boolean isRegisteredOld(boolean o) {
			String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
			+ config.getString("mysql.port") + "/"
			+ config.getString("mysql.database");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con;
				con = DriverManager.getConnection(url,
						config.getString("mysql.user"),
						config.getString("mysql.password"));
				Statement stmt;
				stmt = con.createStatement();
				ResultSet rs;
				String query = ("SELECT user_id,username,user_group_id FROM "
						+ this.config.getString("mysql.prefix")
						+ "user WHERE username="
						+ "lower('" + name + "')"
						+ " LIMIT 1");
				rs = stmt.executeQuery(query);
				
				if (rs.next()) {
					userId = rs.getInt("user_id");
					userType = rs.getInt("user_group_id");
					int userBanned = rs.getInt("is_banned");
					if (o)
						System.out.println("UserGroup: " + userType);
					return (userBanned != 1);
				}
			} catch (SQLException e) {
				System.out.println("Qwertzy2");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Qwertzy");
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected boolean isCustomFieldRegistered(boolean o) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
						+ config.getString("mysql.port") + "/"
						+ config.getString("mysql.database");
				Connection con = DriverManager.getConnection(url,
						config.getString("mysql.user"),
						config.getString("mysql.password"));
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT user_id FROM "
						+ this.config.getString("mysql.prefix")
						+ "user_identity WHERE account_name='" + name
						+ "' AND identity_service_id='" + this.config.getString("field.id")
						+ "' LIMIT 1");
				if (rs.next()) {
					userId = rs.getInt("user_id");
					rs = stmt.executeQuery("SELECT username,user_group_id,is_banned FROM "
							+ this.config.getString("mysql.prefix")
							+ "user WHERE user_id='" + userId + "' LIMIT 1");
					if (rs.next()) {
						userType = rs.getInt("user_group_id");
						if (o)
							System.out.println("UserGroup: " + userType);
						return (rs.getInt("is_bammed") != 1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
}