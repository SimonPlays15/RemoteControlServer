package me.simonplays15.development.remoteconsoleserver.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.utils.UUIDUtils;
import me.simonplays15.development.remoteconsoleserver.utils.player.PermissionHandler;

public class PlayerJoinHandler implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {		
		/**
		 * PermHandling
		 */
		
		PermissionHandler handler = Core.getInstance().getPermissionHandler(UUIDUtils.convertToUUID(event.getPlayer().getName()));
		
		handler.getUuid();

	}

}
