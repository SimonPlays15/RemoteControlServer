package me.simonplays15.development.remoteconsoleserver.utils.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.simonplays15.development.remoteconsoleserver.Core;

public class PermissionsExPermissionHandler extends PermissionHandler{
	
	private UUID uuid;
	
	public PermissionsExPermissionHandler(UUID uuid) {
		super(uuid);
		this.uuid = uuid;
		
		if(Core.getInstance().getPermissionHandler() == null)
			throw new NullPointerException("PermissionsExHandler is not initialized");
		
	}
	
	@Override
	public boolean hasPermission(String perms) {
		
		if(Bukkit.getPlayer(uuid) != null)
			return Core.getInstance().getPermissionHandler().getPermissionManager().has(Bukkit.getPlayer(uuid), perms);
		else
			return Core.getInstance().getPermissionHandler().getPermissionManager().has(uuid, perms, /* missing world */ null);
	}
	
	@Override
	public Set<String> getPermissions() {
		Set<String> perms = new HashSet<String>();
		
		for(String str : Core.getInstance().getPermissionHandler().getPermissionManager().getUser(uuid).getPermissions(Bukkit.getWorlds().get(0).getName())){
			perms.add(str);
		}
		
		return perms;
	}

}
