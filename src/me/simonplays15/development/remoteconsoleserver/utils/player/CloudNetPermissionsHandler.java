package me.simonplays15.development.remoteconsoleserver.utils.player;

import java.util.Set;
import java.util.UUID;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import me.simonplays15.development.remoteconsoleserver.Core;

public class CloudNetPermissionsHandler extends PermissionHandler{
	
	private UUID uuid;
	
	public CloudNetPermissionsHandler(UUID uuid) {
		super(uuid);
		this.uuid = uuid;
		if(Core.getInstance().getCloudNetHandler() == null)
			throw new NullPointerException("CloudNetHandler is not initialized");
	}

	@Override
	public boolean hasPermission(String perms) {
		
		if(uuid != null)
			return CloudServer.getInstance().getCachedPlayer(uuid).getPermissionEntity().getPermissions().get(perms);
		else
			return CloudAPI.getInstance().getOfflinePlayer(uuid).getPermissionEntity().getPermissions().get(perms);
		
	}
	
	@Override
	public Set<String> getPermissions() {
		if(uuid != null) 
			return CloudServer.getInstance().getCachedPlayer(uuid).getPermissionEntity().getPermissions().keySet();
		 else 
			return CloudAPI.getInstance().getOfflinePlayer(uuid).getPermissionEntity().getPermissions().keySet();
		
	}

}
