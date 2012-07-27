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

package de.javakara.manf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLManager {
	private String url;
	private Statement stmt;
	
	public MySQLManager(String host,String port,String db,String user,String pw) throws SQLException, ClassNotFoundException{ 
		Class.forName("com.mysql.jdbc.Driver");
		url = "jdbc:mysql://" + host + ":" + port + "/" + db;
		Connection con = DriverManager.getConnection(url,user,pw);
		stmt = con.createStatement();
	}
	
	public ResultSet executeQuery(String query) throws SQLException{
		return stmt.executeQuery(query);
	}
}
