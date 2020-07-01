package me.simonplays15.development.general.packet.packets.permissions;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;

public class HasPermissionPacket extends Packet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -250861904159423209L;
	private boolean hasPermission;
	
	public HasPermissionPacket(boolean hasPermission) {
		super(new PacketInfo(HasPermissionPacket.class, new Object[] {hasPermission}));
		this.hasPermission = hasPermission;
	}
	
	public boolean hasPermission() {
		return this.hasPermission;
	}

}
