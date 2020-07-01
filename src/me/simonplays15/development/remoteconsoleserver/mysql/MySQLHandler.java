package me.simonplays15.development.remoteconsoleserver.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import org.json.JSONArray;

import me.simonplays15.development.remoteconsoleserver.Core;

public class MySQLHandler {

	private MySQL mysql;
	
	public MySQLHandler(MySQL sql) {
		this.mysql = sql;
	}
	
	public MySQL getMySQL() {
		return this.mysql;
	}
	
	public boolean exists(String uuid, String table) {
		
		ResultSet result = this.mysql.sqlResultQuery("SELECT * from `"+table+"` WHERE `uuid`='"+uuid+"'");
		
		try {
			if(result.next()) {
				return true;
			}
		} catch (SQLException e) {
			Core.getInstance().getLogSystem().log(Level.SEVERE, "An error occurred", e);
		}
		
		return false;
	}
	
	public void addLogEntry(String message) {
		if(mysql.isConnected())
			mysql.sqlQuery("INSERT INTO `server_audit_log` (`logmessage`) VALUES ('"+message+"');");
	}
	
	public void createDefaults(UUID key, JSONArray array) {
		if(exists(key.toString(), "player_permissions"))
			return;
		mysql.sqlQuery("INSERT INTO `player_permissions` (`permissions`, `uuid`) VALUES ('"+array.toString()+"', '"+key.toString()+"')");
	}
	
	public String getUUIDFromUsername(String username) {
		ResultSet result = this.mysql.sqlResultQuery("SELECT * from `player_permissions` WHERE `username`='"+username+"'");
		
		try {
			if(result.next()) {
				return result.getString("uuid");
			}
		} catch (SQLException e) {
			Core.getInstance().getLogSystem().log(Level.SEVERE, "An error occurred", e);
		}
		
		return username;
	}
	
	public void writePermissions(UUID key, JSONArray array) {
		mysql.sqlQuery("UPDATE `player_permissions` SET `permissions`='"+array.toString()+"' WHERE `uuid`='"+key.toString()+"'");
	}
	
	public JSONArray getJSON(UUID key) {
		ResultSet result = mysql.sqlResultQuery("SELECT `permissions` FROM `player_permissions` WHERE `uuid`='"+key.toString()+"'");
		String json = "[]";
		try {
			if(result.next()) {
				json = result.getString("permissions");
			}
		} catch (SQLException e) {
			return new JSONArray();
		}
		
		return new JSONArray(json);
	}
	
	public boolean hasSystemAccount(UUID uuid) {
		
		ResultSet set = mysql.sqlResultQuery("SELECT * FROM `player_permissions` WHERE `uuid`='"+uuid.toString()+"'");
		
		try {
			if(set.next()) {
				return true;
			}
		} catch (SQLException e) {
			Core.getInstance().getLogger().log(Level.SEVERE, "An error occurred while checking for SystemAccount in MySQL Database", e);
		}
		
		return false;
	}
	
}
