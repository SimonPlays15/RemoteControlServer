package me.simonplays15.development.remoteconsoleserver.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import me.simonplays15.development.remoteconsoleserver.Core;

public class PlayerUtils {
	
	public static int getOPPermissionLevel(OfflinePlayer target) {
		
		File ops = new File("ops.json");
		
		try {
			StringBuilder var = new StringBuilder();
			
			try(BufferedReader reader = Files.newBufferedReader(ops.toPath())){
				
				String g;
				while((g = reader.readLine()) != null)
					var.append(g);
				
			} catch(IOException ex) {
				Core.getInstance().getLogger().log(Level.WARNING, "Failed to read operator level from player "+target.getName()+"("+target.getUniqueId().toString()+")", ex);
				return -1;
			}
			JSONArray array = new JSONArray(var.toString());
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject player = array.getJSONObject(i);
				
				if(player.has("uuid") && player.getString("uuid").equals(target.getUniqueId().toString())) {
					return (player.has("level") ?  player.getInt("level") : -1);
				}
				
			}
			
		} catch(Exception ex) {
			Core.getInstance().getLogger().log(Level.WARNING, "Failed to read operator level from player "+target.getName()+"("+target.getUniqueId().toString()+")", ex);
			return -1;
		}
		
		return -1;
	}
	
	public static boolean hasPermission(OfflinePlayer target, String permission) {
		
		if((target.isOp() && getOPPermissionLevel(target) >= Core.getInstance().getConfig().getInt("min-op-lvl")))
			return true;
		
		if(target.getPlayer() != null) {
			if(target.getPlayer().hasPermission(permission)){
				return true;
			}
			
			if(target.getPlayer().hasPermission("bansystem.*")) {
				return true;
			}
		}
		
		if(Core.getInstance().getPermissionHandler() != null) {
			if(Core.getInstance().getPermissionHandler().getPermissionManager().getUser(target.getName()).has(permission)) {
				return true;
			}
			
			if(Core.getInstance().getPermissionHandler().getPermissionManager().getUser(target.getName()).has("remoteconsole.*")) {
				return true;
			}
		}
		
		return false;
	}

}
