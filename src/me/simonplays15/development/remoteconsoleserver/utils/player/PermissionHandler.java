package me.simonplays15.development.remoteconsoleserver.utils.player;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json.JSONArray;

import me.simonplays15.development.remoteconsoleserver.Core;

public abstract class PermissionHandler {
	
	private UUID uuid;
	private boolean isOp;
	public PermissionHandler(UUID uuid) {
		if(uuid == null)
			throw new NullPointerException("UUID cannot be null");
		
		this.uuid = uuid;
		this.isOp = Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).isOp() : false;
		
		Core.getInstance().getMySQLHandler().createDefaults(getUuid(), new JSONArray());
		
	}
	
	public abstract boolean hasPermission(String perms);
	
	public abstract Set<String> getPermissions();

	/**
	 * @return the isOp
	 */
	public final boolean isOp() {
		return isOp;
	}

	/**
	 * @return the uuid
	 */
	public final UUID getUuid() {
		return uuid;
	}
	
	
	

}
