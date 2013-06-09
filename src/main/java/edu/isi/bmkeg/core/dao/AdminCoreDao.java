package edu.isi.bmkeg.core.dao;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.context.ApplicationContext;

import edu.isi.bmkeg.utils.springContext.AppContext;
import edu.isi.bmkeg.utils.springContext.BmkegProperties;

public class AdminCoreDao {

	/**
	 * Administration function to drop and regenerate the local database, 
	 * named in  
	 * Note that this 
	 * @throws Exception
	 */
	public static void regenerateDatabase() throws Exception {

		String localHostName = InetAddress.getLocalHost().getHostName();
		
		String dbName = BmkegProperties.readDbUrl();
		int l = dbName.lastIndexOf("/");
		if( l != -1 )
			dbName = dbName.substring(l+1, dbName.length());
		
		String login = BmkegProperties.readDbUser();
		String passwd = BmkegProperties.readDbPassword();
		
		//
		// Log on to local system.
		//
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Connection dbConnection = null;
		try {
			dbConnection = DriverManager.getConnection("jdbc:mysql://"
					+ localHostName + ":3306/", login, passwd);

			if (dbConnection == null) {
				throw new Exception("Can't connect!");
			}

			Statement quickStat = dbConnection.createStatement(
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			String sql = "drop database " + dbName;
			sql = sql.toLowerCase();

			quickStat.execute(sql);
			
			sql = "create database " + dbName + ";";
			sql = sql.toLowerCase();

			quickStat.execute(sql);
						
		} 
		catch (Exception e) {
		
			e.printStackTrace();
			throw e;

		} finally {
		
			dbConnection.close();		
		
		}	
	
	}
	
}
