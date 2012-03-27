package de.javakara.manf.boards;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;
import de.javakara.manf.software.Software;

public class myBB extends Software{
	/*done*/
	public myBB(String name, FileConfiguration config, boolean a) {
		super(name,config,a);
	}
	/*done*/
	public myBB(String name, FileConfiguration config) {
		super(name,config);
	}
	/*notImplemented*/
	public int getNewPosts() {
		return 0;
	}
	/*done*/
	public boolean getRegistrationValue(boolean o) {
		if (config.getString("general.authType").equals("Username"))
			return this.isRegisteredOld(o);
		if (config.getString("general.authType").equals("Field"))
			return this.isCustomFieldRegistered(o);
		return false;
	}
	/*done*/
	public boolean testMysql() {
		return this.isRegisteredOld(false);
	}
	/*done*/
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
						+ "usergroups WHERE gid ='" + userType + "'");
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
	/*done*/
	public String getForumGroup() {
		return this.getForumGroup(false);
	}
	/*done*/
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
			String query = ("SELECT uid,username,usergroup FROM "
					+ this.config.getString("mysql.prefix")
					+ "users WHERE username="
					+ "lower('" + name + "')"
					+ " LIMIT 1");
			rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				userId = rs.getInt("uid");
				userType = rs.getInt("usergroup");
				if (o)
					System.out.println("UserGroup: " + userType);
				return (userType != 5);
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
	/*done*/
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
			ResultSet rs = stmt.executeQuery("SELECT ufid FROM "
					+ this.config.getString("mysql.prefix")
					+ "userfields WHERE fid"
					+ this.config
					.getString("field.id") + "='" + name
					+ "' LIMIT 1");
			if (rs.next()) {
				userId = rs.getInt("ufid");
				rs = stmt.executeQuery("SELECT uid,usergroup FROM "
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
}