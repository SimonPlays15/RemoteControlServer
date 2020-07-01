package me.simonplays15.development.general.packet.packets.permissions;

import java.net.Socket;
import java.util.UUID;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;
import me.simonplays15.development.remoteconsoleserver.Core;

public class CheckPermissionPacket extends Packet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4235447323638309459L;
	private String perm;
	private String username;
	
	public CheckPermissionPacket(Socket socket, String username, String perm) {
		super(new PacketInfo(CheckPermissionPacket.class, new Object[] {username, perm}));
		this.perm = perm;
		this.username = username;
	}
	
	@Override
	public void execute(Object... params) {
		if(params.length < 3) { 
			//TODO: Send info packet -> Params length is lower than 2
			Core.getInstance().getLogger().warning("Failed to authenticate last connection -> param length is lower than 2");
			return;
		}
		
		String uuid = Core.getInstance().getMySQLHandler().getUUIDFromUsername(username);
		
		if(Core.getInstance().getPermissionHandler(UUID.fromString(uuid)).hasPermission(perm)){
			Core.getInstance().getRemoteServer().sendPacket(username, new HasPermissionPacket(true));
		} else {
			Core.getInstance().getRemoteServer().sendPacket(username, new HasPermissionPacket(false));
		}
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPerm() {
		return perm;
	}

}
