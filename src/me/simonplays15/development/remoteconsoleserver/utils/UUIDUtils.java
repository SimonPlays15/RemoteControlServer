package me.simonplays15.development.remoteconsoleserver.utils;

import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class UUIDUtils {
	
	private static final String API_URL = "https://api.mojang.com/users/profiles/minecraft/";
	
	public static UUID convertToUUID(String username) {
		
		try {
			
			String json = IOUtils.toString(new URL(API_URL+username));
			
			if(json.isEmpty()) 
				return null;
			
			JSONObject uuidJson = new JSONObject(json);
			
			if(uuidJson.has("id"))
				return UUID.fromString(formatUuid(uuidJson.get("id").toString()));
			else
				return null;
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private static String formatUuid(String uuid) {
		if(uuid.length() != 32) 
			return "invalid uuid";
		
		return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
	}
	
}
