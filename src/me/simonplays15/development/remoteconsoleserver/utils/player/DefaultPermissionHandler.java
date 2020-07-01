package me.simonplays15.development.remoteconsoleserver.utils.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONArray;
import org.json.JSONObject;

import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.utils.UUIDUtils;

public class DefaultPermissionHandler extends PermissionHandler implements Listener{
	
	private UUID uuid;
	private BukkitTask threadId;
	public DefaultPermissionHandler(UUID uuid) {
		super(uuid);
		this.uuid = uuid;
		Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
		checkPermissionsThread();
	}
	
	@Override
	public Set<String> getPermissions() {
		return readPerms().keySet();
	}
	
	@Override
	public boolean hasPermission(String perms) {
		if(readPerms().containsKey(perms))
			return readPerms().get(perms);
		
		return false;
	}
	
	private boolean hasControlAccount(UUID uuid) {
		return Core.getInstance().getMySQLHandler().hasSystemAccount(uuid);
	}
	
	@EventHandler
	public void joinHandler(PlayerJoinEvent event) {
		if(hasControlAccount(UUIDUtils.convertToUUID(event.getPlayer().getName())))
			writePermissions();
	}
	
	public void quitHandler(PlayerQuitEvent event) {
		if(threadId != null) {
			Core.getInstance().getLogger().info("AsyncCheckPermissionsThread("+threadId.getTaskId()+") cancelled for "+uuid.toString()+" -> Player leaved");
			threadId.cancel();
		}
	}
	
	private boolean writePermissions() {
		if(Bukkit.getPlayer(uuid) == null)
			return false;
		
		if(!hasControlAccount(getUuid()))
			return false;
		
		JSONArray base = new JSONArray();
		
		JSONObject player = new JSONObject();
		
		JSONObject perms = new JSONObject();
		
		for(Permission p : Bukkit.getPluginManager().getPermissions()) {
			perms.put(p.getName(), Bukkit.getPlayer(getUuid()).hasPermission(p));
		}
		
		
		for(PermissionAttachmentInfo p : Bukkit.getPlayer(getUuid()).getEffectivePermissions()) {
			perms.put(p.getPermission(), p.getValue());
		}
		
		player.put(uuid.toString(), perms.toMap());
		
		base.put(player.toMap());
		
		Core.getInstance().getMySQLHandler().writePermissions(getUuid(), base);
		
		return true;
	}
	
	private Map<String, Boolean> readPerms() {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		JSONArray base = Core.getInstance().getMySQLHandler().getJSON(getUuid());
		
		if(base.length() == 0) 
			return result;
		
		for(int i = 0; i < base.length(); i++) {
			JSONObject player = base.getJSONObject(i);
			Core.getInstance().getLogger().logp(Level.WARNING, this.getClass().getName(), "Map<String, Boolean> readPerms()", player.toString(1));
			for(String keys : player.getJSONObject(getUuid().toString()).toMap().keySet()) {
				result.put(keys, Boolean.valueOf(player.getJSONObject(getUuid().toString()).toMap().get(keys).toString()));
			}
		}
		
		return result;
	}
	
	private void checkPermissionsThread() {
		threadId = Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
			
			if(writePermissions() == false) {
				Core.getInstance().getLogger().warning("AsyncCheckPermissionsThread("+threadId.getTaskId()+") canceled for "+uuid.toString());
				threadId.cancel();
			} else {
				Core.getInstance().getLogger().logp(Level.INFO, DefaultPermissionHandler.class.getName(), "checkPermissionsThread()", "AsyncCheckPermissionsThread("+threadId.getTaskId()+") updated successfully");
			}
			
		}, 10L, 20*30);
	}

}
