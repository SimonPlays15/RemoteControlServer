package me.simonplays15.development.remoteconsoleserver.handler;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.simonplays15.development.remoteconsoleserver.Core;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHandler {
	
	private PermissionManager pexManager;
	public PermissionsExHandler() {
		Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
			try {
				Class.forName("ru.tehkode.permissions.bukkit.PermissionsEx");
				pexManager = PermissionsEx.getPermissionManager();
			} catch(ClassNotFoundException ex) {
				Core.getInstance().getLogSystem().log(Level.WARNING, "Failed to load PermissionsEx from resource -> PermissionsEx is not installed", ex);
			}
		}, 1L);
	}
	
	public PermissionManager getPermissionManager() {
		return pexManager;
	}

}
