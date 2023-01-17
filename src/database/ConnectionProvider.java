package database;
import java.sql.*;
import static database.Provider.*;

public class ConnectionProvider {
	static Connection con = null;
	
	public static void connect() throws Exception {
		Class.forName(DRIVER);
		con = DriverManager.getConnection(CONNECTION_URL,USERNAME,PASSWORD);
	}
	
	public static Connection getCon() throws Exception {
		if(con == null) {
			connect();
			if(con == null) throw new SQLException("Could not create SQL connection");
		}
		return con;
	}
}
