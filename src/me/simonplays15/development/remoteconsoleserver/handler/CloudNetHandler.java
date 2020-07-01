package me.simonplays15.development.remoteconsoleserver.handler;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.CloudServer;
import me.simonplays15.development.remoteconsoleserver.Core;

public class CloudNetHandler {
	
	private CloudAPI cloudApi;
	private CloudServer cloudServer;
	
	public CloudNetHandler() {
		Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
			try {
				Class.forName("de.dytanic.cloudnet.api.CloudAPI");
				cloudApi = CloudAPI.getInstance();
			} catch(ClassNotFoundException ex) {
				Core.getInstance().getLogSystem().log(Level.WARNING, "Failed to load CloudAPI from resource -> CloudAPI is not installed", ex);
			}
		}, 1L);
		
		Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
			try {
				Class.forName("de.dytanic.cloudnet.bridge.CloudServer");
				cloudServer = CloudServer.getInstance();
			} catch(ClassNotFoundException ex) {
				Core.getInstance().getLogSystem().log(Level.WARNING, "Failed to load CloudServer from resource -> CloudServer is not installed", ex);
			}
		}, 1L);
	}
	
	public CloudAPI getCloudAPI() {
		return cloudApi;
	}
	
	public CloudServer getCloudServer() {
		return cloudServer;
	}

}
