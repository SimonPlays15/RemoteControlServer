package me.simonplays15.development.remoteconsoleserver.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import me.simonplays15.development.remoteconsoleserver.Core;

public class MySQL {

	private Connection con;
	private String hostname, username, password, database;

	public MySQL(String host, String username, String password) {

		this.username = username;
		this.hostname = host;
		this.password = password;

		this.connect();

	}

	public void connect() {

		if (isConnected())
			return;

		try {
			con = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":"
					+ Core.getInstance().getConfig().getInt("mysql.port") + "/" + "?autoReconnect=true", this.username,
					this.password);
			Core.getInstance().getLogger().info("MySQL connection established");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception ex) {
			Core.getInstance().getLogger().info("MySQL connection not connected...");
		}

		sqlQuery("CREATE DATABASE IF NOT EXISTS `remoteconsole` DEFAULT CHARACTER SET utf8 COLLATE utf8_german2_ci;"); // CREATING DATABASE

		sqlQuery("USE `remoteconsole`;"); // DATABASE SELECT
		
		sqlQuery("CREATE TABLE IF NOT EXISTS `remoteconsole`.`remoteconsole_users`"
				+ " ( `username` VARCHAR(16) NOT NULL ,"
				+ " `password` VARCHAR(255) NOT NULL ,"
				+ " `uuid` VARCHAR(255) NOT NULL , "
				+ "`isAdmin` INT NOT NULL , "
				+ "`userId` INT NOT NULL AUTO_INCREMENT , "
				+ "PRIMARY KEY (`userId`),  UNIQUE KEY `uuid` (`uuid`)) ENGINE = InnoDB;");
		
		sqlQuery("CREATE TABLE IF NOT EXISTS `remoteconsole`.`player_permissions` ( `uuid` VARCHAR(255) NOT NULL , `permissions` JSON NOT NULL, UNIQUE KEY `uuid` (`uuid`) ) ENGINE = InnoDB;");
		sqlQuery("CREATE TABLE IF NOT EXISTS`remoteconsole`.`server_audit_log` ( `logdate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP , `logmessage` LONGTEXT NOT NULL ) ENGINE = InnoDB;");
		
		//TODO: CREATE TABLE logs (website database)
		
		sqlQuery("COMMIT;");

	}

	public boolean isConnected() {
		return con != null;
	}

	public void disconnect() {
		if (!isConnected())
			return;

		try {
			this.con.close();
		} catch (SQLException e) {
		}

	}

	public void sqlQuery(String sql) {

		if (sql.trim().length() == 0) {
			return;
		}

		if (!isConnected()) {
			// Core.getInstance().getLogger().warning("No MySQL connection -> Connection
			// closed?");
			return;
		}

		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.execute(sql);
			statement.close();
		} catch (Exception ex) {
			if(ex.getMessage().contains("Duplicate entry")) {
				Core.getInstance().getLogger().log(Level.SEVERE, ex.getMessage());
			} else {
				Core.getInstance().getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

	}

	public void sqlQueryMultiple(String sql) {

		if (sql.trim().length() == 0) {
			return;
		}

		if (!isConnected()) {
			// Core.getInstance().getLogger().warning("No MySQL connection -> Connection
			// closed?");
			return;
		}

		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.executeQuery(sql);
			statement.close();
		} catch (Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		}

	}

	public ResultSet sqlResultQuery(String sql) {

		if (sql.trim().length() == 0) {
			return null;
		}

		if (!isConnected()) {
			// Core.getInstance().getLogger().warning("No MySQL connection -> Connection
			// closed?");
			return null;
		}

		ResultSet result = null;

		try {
			PreparedStatement statement = con.prepareStatement(sql);
			statement.execute(sql);
			return statement.getResultSet();
		} catch (Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, "An error occurred", ex);
		}

		return result;
	}

	/**
	 * @return the con
	 */
	public final Connection getCon() {
		return con;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return hostname;
	}

	/**
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * @return the database
	 */
	public final String getDatabase() {
		return database;
	}

}
