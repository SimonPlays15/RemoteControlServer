package me.simonplays15.development.general.utils;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.utils.UUIDUtils;

public class RemoteConsoleCommandSender implements CommandSender {

	private String name;
	private UUID uuid;
	
	public RemoteConsoleCommandSender(String name) {
		
		if(name == null) 
			throw new NullPointerException("Name in field 'name' cannot be blank");
		
		this.name = name;
		try {
		this.uuid = UUIDUtils.convertToUUID(name);
		} catch(Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, "UUID cannot be set -> "+ex.getMessage(), ex);
		} finally {
			this.uuid = UUID.randomUUID();
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0) {
		return Bukkit.getConsoleSender().addAttachment(arg0);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return Bukkit.getConsoleSender().addAttachment(arg0, arg1);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		return Bukkit.getConsoleSender().addAttachment(arg0, arg1, arg2);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		return Bukkit.getConsoleSender().addAttachment(arg0, arg1, arg2, arg3);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return Bukkit.getConsoleSender().getEffectivePermissions();
	}

	@Override
	public boolean hasPermission(String arg0) {
		return Bukkit.getConsoleSender().hasPermission(arg0);
	}

	@Override
	public boolean hasPermission(Permission arg0) {
		return Bukkit.getConsoleSender().hasPermission(arg0);
	}

	@Override
	public boolean isPermissionSet(String arg0) {
		return Bukkit.getConsoleSender().isPermissionSet(arg0);
	}

	@Override
	public boolean isPermissionSet(Permission arg0) {
		return Bukkit.getConsoleSender().isPermissionSet(arg0);
	}

	@Override
	public void recalculatePermissions() {
		Bukkit.getConsoleSender().recalculatePermissions();
	}

	@Override
	public void removeAttachment(PermissionAttachment arg0) {
		Bukkit.getConsoleSender().removeAttachment(arg0);
	}

	@Override
	public boolean isOp() {
		return Bukkit.getConsoleSender().isOp();
	}

	@Override
	public void setOp(boolean arg0) {
		Bukkit.getConsoleSender().setOp(arg0);
	}

	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}

	@Override
	public void sendMessage(String arg0) {
		Bukkit.getConsoleSender().sendMessage(arg0);
	}

	@Override
	public void sendMessage(String[] arg0) {
		Bukkit.getConsoleSender().sendMessage(arg0);
	}
	
}
